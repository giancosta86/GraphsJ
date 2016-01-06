/*§
  ===========================================================================
  GraphsJ
  ===========================================================================
  Copyright (C) 2009-2016 Gianluca Costa
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

package info.gianlucacosta.graphsj3.stages.main;

import info.gianlucacosta.arcontes.algorithms.*;
import info.gianlucacosta.arcontes.algorithms.run.AlgorithmRunService;
import info.gianlucacosta.arcontes.algorithms.run.AlgorithmRunState;
import info.gianlucacosta.arcontes.fx.algorithms.TextAreaAlgorithmOutput;
import info.gianlucacosta.arcontes.fx.canvas.GraphCanvas;
import info.gianlucacosta.arcontes.fx.canvas.GraphCanvasPermissions;
import info.gianlucacosta.arcontes.fx.canvas.GraphCanvasPermissionsSnapshot;
import info.gianlucacosta.arcontes.fx.canvas.GraphCanvasSelection;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.graphsj3.DesktopUtils;
import info.gianlucacosta.graphsj3.GraphsJPreferences;
import info.gianlucacosta.graphsj3.Main;
import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.graphsj3.stages.about.AboutDialog;
import info.gianlucacosta.graphsj3.stages.help.HelpDialog;
import info.gianlucacosta.graphsj3.stages.newscenario.NewScenarioDialog;
import info.gianlucacosta.graphsj3.workspace.DefaultScenarioWorkspace;
import info.gianlucacosta.graphsj3.workspace.ScenarioDocument;
import info.gianlucacosta.graphsj3.workspace.ScenarioWorkspace;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.application.io.CommonOutputService;
import info.gianlucacosta.helios.application.io.CommonQuestionOutcome;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.beans.events.ValueChangedListener;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.conditions.PredicateBasedCondition;
import info.gianlucacosta.helios.conversions.StreamToStringConverter;
import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import info.gianlucacosta.helios.fx.dialogs.filechooser.DefaultExtensionFileChooser;
import info.gianlucacosta.helios.predicates.IntegerRangePredicate;
import info.gianlucacosta.helios.preferences.PreferencesService;
import info.gianlucacosta.helios.recentfiles.RecentFilesService;
import info.gianlucacosta.helios.reflection.Locator;
import info.gianlucacosta.helios.serialization.SerializationService;
import info.gianlucacosta.helios.undo.UndoRedoService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller underlying the main window
 */
public class DefaultMainWindowController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMainWindowController.class);

    private final DefaultExtensionFileChooser consoleFileChooser;
    private final ScenarioWorkspace scenarioWorkspace;
    private final AppInfoService appInfoService;
    private final CommonOutputService commonOutputService;
    private final CommonInputService commonInputService;
    private final AboutDialog aboutDialog;
    private final PreferencesService<GraphsJPreferences> preferencesService;
    private final AlgorithmRunService algorithmRunService;
    private final RecentFilesService recentFilesService;

    //-----
    @FXML
    private MenuItem newMenuItem;
    @FXML
    private Button newButton;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private Button openButton;
    @FXML
    private Menu recentFilesMenu;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private Button saveButton;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private Button saveAsButton;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private Menu editMenu;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private Button undoButton;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private Button redoButton;
    @FXML
    private Menu selectionMenu;
    @FXML
    private MenuItem selectEverythingMenuItem;
    @FXML
    private MenuItem selectNothingMenuItem;
    @FXML
    private MenuItem editSelectedItemMenuItem;
    @FXML
    private MenuItem removeSelectedItemsMenuItem;
    @FXML
    private Menu scenarioMenu;
    @FXML
    private MenuItem showScenarioNameMenuItem;
    @FXML
    private MenuItem showScenarioHelpMenuItem;
    @FXML
    private MenuItem showScenarioSettingsMenuItem;
    @FXML
    private Menu consoleMenu;
    @FXML
    private MenuItem saveConsoleAsMenuItem;
    @FXML
    private MenuItem clearConsoleMenuItem;
    @FXML
    private Menu runMenu;
    @FXML
    private MenuItem startRunMenuItem;
    @FXML
    private Button startRunButton;
    @FXML
    private MenuItem runStepMenuItem;
    @FXML
    private Button runStepButton;
    @FXML
    private MenuItem runToEndMenuItem;
    @FXML
    private Button runToEndButton;
    @FXML
    private MenuItem stopRunMenuItem;
    @FXML
    private Button stopRunButton;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private Button helpButton;
    @FXML
    private MenuItem showWebsiteMenuItem;
    @FXML
    private Button showWebsiteButton;
    @FXML
    private MenuItem aboutMenuItem;
    @FXML
    private Button aboutButton;
    @FXML
    private SplitPane workspaceArea;
    @FXML
    private ScrollPane graphCanvasPane;
    @FXML
    private TextArea consoleArea;
    //-----

    private Stage stage;
    private AlgorithmInput algorithmInput;
    private AlgorithmOutput algorithmOutput;
    private HelpDialog helpDialog;
    private GraphCanvas graphCanvas;

    private GraphCanvasPermissionsSnapshot canvasPermissionsSnapshot;

    public DefaultMainWindowController(AppInfoService appInfoService,
                                       NewScenarioDialog newScenarioDialog,
                                       CommonOutputService commonOutputService,
                                       CommonInputService commonInputService,
                                       AboutDialog aboutDialog,
                                       PreferencesService<GraphsJPreferences> preferencesService,
                                       SerializationService serializationService,
                                       AlgorithmRunService algorithmRunService,
                                       RecentFilesService recentFilesService) {

        this.appInfoService = appInfoService;
        this.commonOutputService = commonOutputService;
        this.commonInputService = commonInputService;
        this.aboutDialog = aboutDialog;
        this.preferencesService = preferencesService;
        this.algorithmRunService = algorithmRunService;
        this.recentFilesService = recentFilesService;

        consoleFileChooser = new ConsoleFileChooser(commonInputService);

        scenarioWorkspace = new DefaultScenarioWorkspace(
                new Locator<Stage>() {

                    @Override
                    public Stage locate() {
                        return stage;
                    }

                },
                newScenarioDialog,
                recentFilesService,
                commonOutputService,
                commonInputService,
                preferencesService,
                serializationService
        );

        initWorkspace();

        algorithmRunService.addRunStateChangedListener(new ValueChangedListener<AlgorithmRunState>() {
            @Override
            public void onChanged(AlgorithmRunState oldValue, AlgorithmRunState newValue) {
                updateMenus();
                graphCanvas.render();
            }

        });

        recentFilesService.addFilesChangedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                updateRecentFilesMenu();
            }
        });
    }

    private void updateRecentFilesMenu() {
        recentFilesMenu.getItems().clear();

        for (final File file : recentFilesService.getFiles()) {
            MenuItem currentFileMenuItem = new MenuItem();
            currentFileMenuItem.setText(file.getAbsolutePath());

            currentFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    scenarioWorkspace.openDocument(file);
                }

            });

            recentFilesMenu.getItems().add(0, currentFileMenuItem);
        }

        recentFilesMenu.setDisable(recentFilesMenu.getItems().isEmpty());
    }

    private void initWorkspace() {
        scenarioWorkspace.addDocumentReplacedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                consoleArea.clear();

                final ScenarioDocument scenarioDocument = scenarioWorkspace.getDocument();

                if (scenarioDocument != null) {
                    Scenario scenario = scenarioDocument.getScenario();

                    scenario.setOutputService(commonOutputService);
                    scenario.setInputService(commonInputService);

                    scenarioDocument.addModifiedChangedListener(new TriggerListener() {
                        @Override
                        public void onTriggered() {
                            updateStageTitle();
                            saveMenuItem.setDisable(!scenarioDocument.isModified());
                        }

                    });
                }

                updateGuiAccordingToTheDocument();
            }

        });

        scenarioWorkspace.addDocumentFileChangedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                updateStageTitle();
            }

        });
    }

    private void updateGuiAccordingToTheDocument() {
        final ScenarioDocument scenarioDocument = scenarioWorkspace.getDocument();
        setupGraphCanvasPane(scenarioDocument);

        updateStageTitle();
        updateMenus();

        workspaceArea.setVisible(false);

        if (scenarioDocument == null) {
            return;
        }

        scenarioDocument.addGraphContextChangedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                graphCanvas.setGraphContext(scenarioDocument.getGraphContext());
                graphCanvas.render();
            }

        });

        algorithmInput = new ScenarioAlgorithmInput(commonOutputService, commonInputService, scenarioDocument);

        algorithmOutput = new TextAreaAlgorithmOutput(consoleArea);

        setupUndoRedoService(scenarioDocument);

        workspaceArea.setVisible(true);
    }

    private void setupUndoRedoService(final ScenarioDocument scenarioDocument) {
        final UndoRedoService undoRedoService = scenarioWorkspace.getUndoRedoService();

        undoRedoService.addSnapshotTakenListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                undoMenuItem.setDisable(!undoRedoService.canUndo());
                redoMenuItem.setDisable(!undoRedoService.canRedo());
            }

        });

        undoRedoService.addTrimmedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                undoMenuItem.setDisable(!undoRedoService.canUndo());
                redoMenuItem.setDisable(!undoRedoService.canRedo());
            }

        });

        undoRedoService.addSnapshotRestoredListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                undoMenuItem.setDisable(!undoRedoService.canUndo());
                redoMenuItem.setDisable(!undoRedoService.canRedo());

                scenarioWorkspace.getDocument().setModified(true);
                graphCanvas.setGraphContext(scenarioDocument.getGraphContext());
                graphCanvas.render();
            }
        });
    }

    private void setupGraphCanvasPane(final ScenarioDocument scenarioDocument) {
        if (scenarioDocument == null) {
            return;
        }

        Scenario scenario = scenarioDocument.getScenario();
        GraphContext graphContext = scenarioDocument.getGraphContext();

        graphCanvas = scenario.createGraphCanvas(graphContext);
        setupGraphCanvas();
        graphCanvas.setGraphContext(graphContext);
        graphCanvas.render();

        graphCanvasPane.setContent(graphCanvas.asNode());

        graphCanvasPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                graphCanvasPane.requestFocus();
            }
        });

        workspaceArea.autosize();
    }

    private void setupGraphCanvas() {
        graphCanvas.addManualModificationListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                if (algorithmRunService.getRunState() == AlgorithmRunState.NOT_STARTED) {
                    scenarioWorkspace.getDocument().setModified(true);
                    scenarioWorkspace.getUndoRedoService().takeSnapshot();
                }
            }
        });

        graphCanvas.getSelection().addChangedListener(new TriggerListener() {
            @Override
            public void onTriggered() {
                updateMenus();
            }
        });
    }

    private void updateStageTitle() {
        if (stage == null) {
            return;
        }

        String stageTitle = appInfoService.getTitle();

        if (!scenarioWorkspace.isEmpty()) {
            File documentFile = scenarioWorkspace.getDocumentFile();

            String documentFileTitlePart;
            if (documentFile != null) {
                documentFileTitlePart = scenarioWorkspace.getDocumentFile().getName();
            } else {
                documentFileTitlePart = "[unnamed]";
            }

            stageTitle += " - " + documentFileTitlePart;

            if (scenarioWorkspace.getDocument().isModified()) {
                stageTitle += "*";
            }
        }

        stage.setTitle(stageTitle);
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (!scenarioWorkspace.closeDocument()) {
                    event.consume();
                }
            }

        });

        updateStageTitle();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        helpDialog = new HelpDialog(appInfoService);

        initMenuShortcuts();
        initToolbar();
        updateGuiAccordingToTheDocument();
        updateRecentFilesMenu();
    }

    private void initMenuShortcuts() {
        newMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCodeCombination.SHIFT_DOWN));

        undoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        redoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        selectEverythingMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        selectNothingMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        editSelectedItemMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.ENTER));
        removeSelectedItemsMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));

        showScenarioHelpMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.ALT_DOWN));

        startRunMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F6));
        runStepMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F7));
        runToEndMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F8));
        stopRunMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F9));

        helpMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        aboutMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12));
    }

    public void initToolbar() {
        newButton.disableProperty().bind(newMenuItem.disableProperty());
        newButton.setTooltip(new Tooltip(newMenuItem.getText()));

        openButton.disableProperty().bind(openMenuItem.disableProperty());
        openButton.setTooltip(new Tooltip(openMenuItem.getText()));

        saveButton.disableProperty().bind(saveMenuItem.disableProperty());
        saveButton.setTooltip(new Tooltip(saveMenuItem.getText()));

        saveAsButton.disableProperty().bind(saveAsMenuItem.disableProperty());
        saveAsButton.setTooltip(new Tooltip(saveAsMenuItem.getText()));

        undoButton.disableProperty().bind(undoMenuItem.disableProperty());
        undoButton.setTooltip(new Tooltip(undoMenuItem.getText()));

        redoButton.disableProperty().bind(redoMenuItem.disableProperty());
        redoButton.setTooltip(new Tooltip(redoMenuItem.getText()));

        startRunButton.disableProperty().bind(startRunMenuItem.disableProperty());
        startRunButton.setTooltip(new Tooltip(startRunMenuItem.getText()));

        runStepButton.disableProperty().bind(runStepMenuItem.disableProperty());
        runStepButton.setTooltip(new Tooltip(runStepMenuItem.getText()));

        runToEndButton.disableProperty().bind(runToEndMenuItem.disableProperty());
        runToEndButton.setTooltip(new Tooltip(runToEndMenuItem.getText()));

        stopRunButton.disableProperty().bind(stopRunMenuItem.disableProperty());
        stopRunButton.setTooltip(new Tooltip(stopRunMenuItem.getText()));

        helpButton.disableProperty().bind(helpMenuItem.disableProperty());
        helpButton.setTooltip(new Tooltip(helpMenuItem.getText()));

        showWebsiteButton.disableProperty().bind(showWebsiteMenuItem.disableProperty());
        showWebsiteButton.setTooltip(new Tooltip(showWebsiteMenuItem.getText()));

        aboutButton.disableProperty().bind(aboutMenuItem.disableProperty());
        aboutButton.setTooltip(new Tooltip(aboutMenuItem.getText()));
    }

    public void updateMenus() {
        boolean inRuntimeMode;
        AlgorithmRunState runState;

        if (scenarioWorkspace.isEmpty()) {
            inRuntimeMode = false;
            runState = null;
        } else {
            runState = algorithmRunService.getRunState();
            inRuntimeMode = (runState != AlgorithmRunState.NOT_STARTED);

        }

        editMenu.setDisable(scenarioWorkspace.isEmpty());
        scenarioMenu.setDisable(scenarioWorkspace.isEmpty());
        runMenu.setDisable(scenarioWorkspace.isEmpty());
        consoleMenu.setDisable(scenarioWorkspace.isEmpty());

        newMenuItem.setDisable(inRuntimeMode);
        openMenuItem.setDisable(inRuntimeMode);
        recentFilesMenu.setDisable(recentFilesService.getFiles().isEmpty() || inRuntimeMode);
        saveMenuItem.setDisable(scenarioWorkspace.isEmpty() || !scenarioWorkspace.getDocument().isModified());
        saveAsMenuItem.setDisable(scenarioWorkspace.isEmpty());
        closeMenuItem.setDisable(scenarioWorkspace.isEmpty());

        UndoRedoService undoRedoService = scenarioWorkspace.getUndoRedoService();
        undoMenuItem.setDisable(scenarioWorkspace.isEmpty() || !undoRedoService.canUndo() || inRuntimeMode);
        redoMenuItem.setDisable(scenarioWorkspace.isEmpty() || !undoRedoService.canRedo() || inRuntimeMode);
        selectionMenu.setDisable(scenarioWorkspace.isEmpty() || inRuntimeMode);
        selectEverythingMenuItem.setDisable(scenarioWorkspace.isEmpty() || inRuntimeMode);
        selectNothingMenuItem.setDisable(scenarioWorkspace.isEmpty() || graphCanvas.getSelection().isEmpty() || inRuntimeMode);
        editSelectedItemMenuItem.setDisable(scenarioWorkspace.isEmpty() || !graphCanvas.getSelection().hasOneItem() || inRuntimeMode);
        removeSelectedItemsMenuItem.setDisable(scenarioWorkspace.isEmpty() || graphCanvas.getSelection().isEmpty() || inRuntimeMode);

        showScenarioNameMenuItem.setDisable(scenarioWorkspace.isEmpty());
        showScenarioHelpMenuItem.setDisable(scenarioWorkspace.isEmpty());
        showScenarioSettingsMenuItem.setDisable(scenarioWorkspace.isEmpty() || inRuntimeMode);

        startRunMenuItem.setDisable(scenarioWorkspace.isEmpty() || inRuntimeMode);
        runStepMenuItem.setDisable(!inRuntimeMode || runState != AlgorithmRunState.STARTED);
        runToEndMenuItem.setDisable(runStepMenuItem.isDisable());
        stopRunMenuItem.setDisable(!inRuntimeMode || runState == AlgorithmRunState.RUNNING);

        saveConsoleAsMenuItem.setDisable(scenarioWorkspace.isEmpty());
        clearConsoleMenuItem.setDisable(scenarioWorkspace.isEmpty());
    }

    public void createNewScenario(ActionEvent event) {
        scenarioWorkspace.createNewDocument();
    }

    public void openScenario(ActionEvent event) {
        scenarioWorkspace.openDocument();
    }

    public void saveScenario(ActionEvent event) {
        scenarioWorkspace.saveDocument();
    }

    public void saveScenarioAs(ActionEvent event) {
        scenarioWorkspace.saveDocumentAs();
    }

    public void closeScenario(ActionEvent event) {
        scenarioWorkspace.closeDocument();
    }

    public void exitApplication(ActionEvent event) {
        if (scenarioWorkspace.closeDocument()) {
            stage.close();
        }
    }

    public void undo() {
        scenarioWorkspace.getUndoRedoService().undo();
    }

    public void redo() {
        scenarioWorkspace.getUndoRedoService().redo();
    }

    public void selectAll() {
        graphCanvas.selectAll();
    }

    public void selectNothing() {
        graphCanvas.selectNothing();
    }

    public void editSelectedItem() {
        GraphCanvasSelection selection = graphCanvas.getSelection();

        if (!selection.hasOneItem()) {
            return;
        }

        if (selection.getVertexes().size() == 1) {
            Vertex vertex = CollectionItems.getSingle(selection.getVertexes());

            if (graphCanvas.getPermissions().isCanEditVertexes()) {
                graphCanvas.editVertex(vertex);
            }
        } else if (selection.getLinks().size() == 1) {
            Link link = CollectionItems.getSingle(selection.getLinks());

            if (graphCanvas.getPermissions().isCanEditLinks()) {
                graphCanvas.editLink(link);
            }
        } else {
            throw new IllegalStateException("Cannot edit the current selection");
        }
    }

    public void removeSelectedItems() {
        if (graphCanvas.getPermissions().isCanRemoveSelectedItems()) {
            graphCanvas.removeSelectedItems();
        }

        graphCanvas.fireManualModificationEvent();
    }

    public void saveConsoleAs(ActionEvent event) {
        File targetFile = consoleFileChooser.showSaveDialog(stage);

        if (targetFile == null) {
            return;
        }

        try {
            try (FileWriter fileWriter = new FileWriter(targetFile)) {
                fileWriter.append(consoleArea.getText());
            }
        } catch (IOException ex) {
            commonOutputService.showWarning(ex.getMessage());
        }
    }

    public void clearConsole(ActionEvent event) {
        consoleArea.clear();
    }

    public void clearRecentFilesMenu() {
        if (commonInputService.askYesNoQuestion("Do you really want to clear the list of recent files?") != CommonQuestionOutcome.YES) {
            return;
        }

        recentFilesService.clear();
    }

    public void setMaxSizeForUndoStack() {
        GraphsJPreferences preferences = preferencesService.getPreferences();
        int currentMaxSize = preferences.getUndoStackMaxSize();

        Integer newMaxSize = commonInputService.askForInteger(
                "Maximum number of actions that can be undone:",
                currentMaxSize,
                new PredicateBasedCondition<>(
                        new IntegerRangePredicate(0, Integer.MAX_VALUE),
                        "Please, enter a number >= 0"
                )
        );

        if (newMaxSize == null) {
            return;
        }

        preferences.setUndoStackMaxSize(currentMaxSize);

        UndoRedoService undoRedoService = scenarioWorkspace.getUndoRedoService();
        if (undoRedoService != null) {
            undoRedoService.setMaxSize(newMaxSize);
        }

    }

    public void setMaxSizeForRecentFilesMenu() {
        Integer newMaxSize = commonInputService.askForInteger(
                "Maximum number of recent files to show:",
                recentFilesService.getMaxSize(),
                new PredicateBasedCondition<>(
                        new IntegerRangePredicate(0, Integer.MAX_VALUE),
                        "Please, enter a number >= 0")
        );

        if (newMaxSize != null) {
            recentFilesService.setMaxSize(newMaxSize);
        }
    }

    public void startAlgorithmRun() {
        ScenarioDocument document = scenarioWorkspace.getDocument();

        Scenario scenario = document.getScenario();

        final GraphContext runtimeGraphContext = scenario.cloneGraphContext(document.getGraphContext());
        scenario.initRuntimeGraphContext(runtimeGraphContext);

        GraphCanvasPermissions canvasPermissions = graphCanvas.getPermissions();
        canvasPermissionsSnapshot = canvasPermissions.takeSnapshot();
        scenario.setRuntimeCanvasPermissions(canvasPermissions);

        Algorithm algorithm = scenario.createAlgorithm(runtimeGraphContext, algorithmInput, algorithmOutput);

        algorithm.addAlgorithmListener(new AlgorithmAdapter() {
            @Override
            public void onInited() {
                /*
                 * The runtime graph context is assigned to the graph canvas only
                 * at the end of the algorithm's initialization phase, so as to
                 * allow the algorithm to attach any useful metadata that might
                 * be required to render the graph
                 */
                graphCanvas.setGraphContext(runtimeGraphContext);
                graphCanvas.render();
            }

            @Override
            public void onStepStarted(int step) {
                graphCanvas.render();
            }

            @Override
            public void onStepCompleted(int step, AlgorithmStepOutcome stepOutcome) {
                graphCanvas.render();
            }

            @Override
            public void onInterrupted() {
                graphCanvas.render();
            }

            @Override
            public void onFinished() {
                graphCanvas.render();
            }
        });

        try {
            algorithmRunService.start(algorithm);
        } catch (AlgorithmException ex) {
            commonOutputService.showWarning(ex.getMessage());
        }
    }

    public void runAlgorithmStep() {
        try {
            algorithmRunService.runStep();
        } catch (AlgorithmException ex) {
            commonOutputService.showWarning(ex.getMessage());
        }
    }

    public void runAlgorithmToEnd() {
        try {
            algorithmRunService.runToEnd();
        } catch (AlgorithmException ex) {
            commonOutputService.showWarning(ex.getMessage());
        }
    }

    public void stopAlgorithmRun() {
        try {
            algorithmRunService.stop();
        } finally {
            graphCanvas.setGraphContext(scenarioWorkspace.getDocument().getGraphContext());
            graphCanvas.getPermissions().restoreSnapshot(canvasPermissionsSnapshot);
        }
    }

    public void showScenarioName() {
        commonOutputService.showInfo(scenarioWorkspace.getDocument().getScenarioName());
    }

    public void showScenarioHelp() {
        String scenarioHelpSource = scenarioWorkspace.getDocument().getScenario().getHtmlHelpSource();

        if (scenarioHelpSource != null) {
            helpDialog.getWebEngine().loadContent(scenarioHelpSource);
            helpDialog.show();
        } else {
            commonOutputService.showInfo("This scenario does not provide online help");
        }
    }

    public void showScenarioSettings() {
        ScenarioDocument document = scenarioWorkspace.getDocument();
        Scenario scenario = document.getScenario();

        if (scenario.editSettings(document.getGraphContext())) {
            logger.debug("The user has confirmed the settings editing! Time to render the GraphCanvas!");

            graphCanvas.render();

            document.setModified(true);
        } else {
            logger.debug("No changes performed to the scenario settings....");
        }
    }

    public void showHelp() {
        try (InputStream helpInputStream = Main.class.getResourceAsStream("Help.htm")) {
            String helpContentString = new StreamToStringConverter().convert(helpInputStream);
            helpDialog.getWebEngine().loadContent(helpContentString);
            helpDialog.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void showWebsite() {
        DesktopUtils.openBrowser(appInfoService.getWebsite());
    }

    public void showAboutBox() {
        aboutDialog.show();
    }
}
