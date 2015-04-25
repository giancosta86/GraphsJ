/*§
  ===========================================================================
  GraphsJ
  ===========================================================================
  Copyright (C) 2009-2015 Gianluca Costa
  ===========================================================================
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public
  License along with this program.  If not, see
  <http://www.gnu.org/licenses/gpl-3.0.html>.
  ===========================================================================
*/

package info.gianlucacosta.graphsj3.workspace;

import info.gianlucacosta.graphsj3.GraphsJPreferences;
import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.graphsj3.stages.newscenario.NewScenarioDialog;
import info.gianlucacosta.graphsj3.stages.newscenario.ScenarioContext;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.application.io.CommonOutputService;
import info.gianlucacosta.helios.application.io.CommonQuestionOutcome;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.fx.dialogs.filechooser.DefaultExtensionFileChooser;
import info.gianlucacosta.helios.jar.JarClassLoader;
import info.gianlucacosta.helios.preferences.PreferencesService;
import info.gianlucacosta.helios.recentfiles.RecentFilesService;
import info.gianlucacosta.helios.reflection.Locator;
import info.gianlucacosta.helios.serialization.LoadingContext;
import info.gianlucacosta.helios.serialization.SavingContext;
import info.gianlucacosta.helios.serialization.SerializationService;
import info.gianlucacosta.helios.undo.UndoRedoService;
import info.gianlucacosta.helios.workspace.CloseRequestOutcome;
import info.gianlucacosta.helios.workspace.singledocument.AbstractFileBasedWorkspace;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Implementation of ScenarioWorkspace
 */
public class DefaultScenarioWorkspace extends AbstractFileBasedWorkspace<ScenarioDocument> implements ScenarioWorkspace {

    private static final Logger logger = LoggerFactory.getLogger(DefaultScenarioWorkspace.class);

    private static final int CURRENT_DOCUMENT_VERSION = 3;
    private static final String DEFAULT_DOCUMENT_EXTENSION = ".gj3";
    private static final String DOCUMENT_DESCRIPTOR_ENTRY_NAME = "Header";
    private static final String DOCUMENT_ENTRY_NAME = "Document";

    private final Locator<Stage> mainStageLocator;
    private final NewScenarioDialog newScenarioDialog;
    private final DefaultExtensionFileChooser scenarioFileChooser;
    private final DefaultExtensionFileChooser jarFileChooser;
    private final RecentFilesService recentFilesService;
    private final CommonOutputService commonOutputService;
    private final CommonInputService commonInputService;
    private final SerializationService serializationService;

    private UndoRedoService undoRedoService;
    private File referenceJarFile;
    private JarClassLoader referenceJarClassLoader;

    public DefaultScenarioWorkspace(
            Locator<Stage> mainStageLocator,
            NewScenarioDialog newScenarioDialog,
            RecentFilesService recentFilesService,
            CommonOutputService commonOutputService,
            CommonInputService commonInputService,
            final PreferencesService<GraphsJPreferences> preferencesService,
            SerializationService serializationService
    ) {
        this.mainStageLocator = mainStageLocator;
        this.newScenarioDialog = newScenarioDialog;
        this.recentFilesService = recentFilesService;
        this.commonOutputService = commonOutputService;
        this.commonInputService = commonInputService;
        this.serializationService = serializationService;

        scenarioFileChooser = new DefaultExtensionFileChooser(commonInputService, DEFAULT_DOCUMENT_EXTENSION);
        scenarioFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GraphsJ 3 document", "*" + DEFAULT_DOCUMENT_EXTENSION));

        jarFileChooser = new DefaultExtensionFileChooser(commonInputService, ".jar");
        jarFileChooser.setTitle("Find missing jar file...");
        jarFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Jar file", "*.jar"));

        addDocumentReplacedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                final DefaultScenarioDocument document = (DefaultScenarioDocument) getDocument();

                if (document != null) {
                    GraphsJPreferences preferences = preferencesService.getPreferences();

                    undoRedoService = new ScenarioUndoRedoService(
                            document,
                            preferences.getUndoStackMaxSize());
                } else {
                    undoRedoService = null;
                }
            }
        });
    }

    @Override
    public UndoRedoService getUndoRedoService() {
        return undoRedoService;
    }

    @Override
    protected DefaultScenarioDocument doCreateNewDocument() {
        ScenarioContext scenarioContext = newScenarioDialog.show();

        if (scenarioContext == null) {
            return null;
        }

        Scenario newScenario = scenarioContext.getScenario();
        referenceJarFile = scenarioContext.getReferenceJarFile();

        if (referenceJarFile != null) {
            referenceJarClassLoader = JarClassLoader.create(referenceJarFile);
        } else {
            referenceJarClassLoader = null;
        }

        return new DefaultScenarioDocument(scenarioContext.getScenarioName(), newScenario);
    }

    @Override
    protected File askForFileToOpen() {
        return scenarioFileChooser.showOpenDialog(mainStageLocator.locate());
    }

    @Override
    protected OpeningResult<ScenarioDocument> doOpenDocument(File fileToOpen) {
        boolean referenceJarFileWasChanged = false;

        try (ZipFile zipFile = new ZipFile(fileToOpen)) {

            try (LoadingContext documentDescriptorLoadingContext = serializationService.createLoadingContext(zipFile.getInputStream(zipFile.getEntry(DOCUMENT_DESCRIPTOR_ENTRY_NAME)))) {
                ObjectInputStream documentDescriptorInputStream = documentDescriptorLoadingContext.getInputStream();
                ScenarioDocumentDescriptor scenarioDocumentDescriptor = (ScenarioDocumentDescriptor) documentDescriptorInputStream.readObject();

                if (scenarioDocumentDescriptor.getDocumentVersion() != CURRENT_DOCUMENT_VERSION) {
                    throw new IOException("Unsupported document version!");
                }

                referenceJarFile = scenarioDocumentDescriptor.getReferenceJarFile();
                if (referenceJarFile != null) {
                    if (!referenceJarFile.exists()) {
                        commonOutputService.showWarning(String.format("The referenced JAR file ('%s') does not exist.\nPlease, choose its new location in the next dialog.", referenceJarFile.getAbsolutePath()));

                        referenceJarFile = jarFileChooser.showOpenDialog(mainStageLocator.locate());

                        if (referenceJarFile == null) {
                            return null;
                        }

                        referenceJarFileWasChanged = true;
                    }

                    referenceJarClassLoader = JarClassLoader.create(referenceJarFile);
                } else {
                    referenceJarClassLoader = null;
                }
            }

            try (LoadingContext documentLoadingContext = serializationService.createLoadingContext(zipFile.getInputStream(zipFile.getEntry(DOCUMENT_ENTRY_NAME)))) {
                if (referenceJarClassLoader != null) {
                    documentLoadingContext.setClassLoader(referenceJarClassLoader);
                }

                ObjectInputStream documentInputStream = documentLoadingContext.getInputStream();

                final DefaultScenarioDocument scenarioDocument = (DefaultScenarioDocument) documentInputStream.readObject();

                recentFilesService.add(fileToOpen);

                final boolean finalReferenceJarFileWasChanged = referenceJarFileWasChanged;

                return new OpeningResult<ScenarioDocument>() {
                    @Override
                    public DefaultScenarioDocument getDocumentImplementation() {
                        return scenarioDocument;
                    }

                    @Override
                    public void postAction() {
                        if (finalReferenceJarFileWasChanged) {
                            scenarioDocument.setModified(true);
                        }
                    }
                };
            }
        } catch (FileNotFoundException ex) {
            recentFilesService.remove(fileToOpen);
            commonOutputService.showWarning("The requested file does not exist");
        } catch (Exception ex) {
            recentFilesService.remove(fileToOpen);
            commonOutputService.showThrowable("An error occured while opening the document", ex);
        }

        return null;
    }

    @Override
    protected File askForFileToSave() {
        return scenarioFileChooser.showSaveDialog(mainStageLocator.locate());
    }

    @Override
    protected boolean doSaveDocumentTo(File fileToSave) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fileToSave))) {
            zipOutputStream.putNextEntry(new ZipEntry(DOCUMENT_DESCRIPTOR_ENTRY_NAME));
            ScenarioDocumentDescriptor scenarioDocumentDescriptor = new ScenarioDocumentDescriptor(CURRENT_DOCUMENT_VERSION, referenceJarFile);

            SavingContext documentDescriptorSavingContext = serializationService.createSavingContext(zipOutputStream);
            ObjectOutputStream documentDescriptorOutputStream = documentDescriptorSavingContext.getOutputStream();
            documentDescriptorOutputStream.writeObject(scenarioDocumentDescriptor);
            documentDescriptorOutputStream.flush();

            zipOutputStream.putNextEntry(new ZipEntry(DOCUMENT_ENTRY_NAME));
            try (SavingContext documentSavingContext = serializationService.createSavingContext(zipOutputStream)) {
                ObjectOutputStream outputStream = documentSavingContext.getOutputStream();
                outputStream.writeObject(getDocument());
            }
        } catch (Exception ex) {
            commonOutputService.showThrowable("An error occured while saving the document", ex);
            return false;
        }

        recentFilesService.add(fileToSave);
        return true;
    }

    @Override
    public boolean closeDocument() {
        boolean result = super.closeDocument();

        if (result && referenceJarClassLoader != null) {
            try {
                referenceJarClassLoader.close();
            } catch (IOException ex) {
                logger.error("Exception while closing the reference jar classloader: \n{}", ex);
            } finally {
                referenceJarClassLoader = null;
            }
        }

        return result;
    }

    @Override
    protected CloseRequestOutcome askToClose() {
        CommonQuestionOutcome savingDialogOutcome = commonInputService.askYesNoCancelQuestion("There are unsaved changes!\nDo you wish to save?");

        switch (savingDialogOutcome) {
            case YES:
                return CloseRequestOutcome.CLOSE_SAVING;

            case NO:
                return CloseRequestOutcome.CLOSE_WITHOUT_SAVING;

            case CANCEL:
                return CloseRequestOutcome.CANCEL;

            default:
                throw new IllegalStateException();
        }
    }

}
