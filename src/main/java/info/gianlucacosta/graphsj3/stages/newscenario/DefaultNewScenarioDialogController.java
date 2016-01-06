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

package info.gianlucacosta.graphsj3.stages.newscenario;

import info.gianlucacosta.graphsj3.scenarios.ScenarioDescriptor;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.application.io.CommonOutputService;
import info.gianlucacosta.helios.collections.general.CollectionItems;
import info.gianlucacosta.helios.collections.general.SortedCollection;
import info.gianlucacosta.helios.jar.JarClassLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller underlying NewScenarioDialog
 */
public class DefaultNewScenarioDialogController implements Initializable {

    private final ScenarioDiscoveryService scenarioDiscoveryService;
    private final CommonOutputService commonOutputService;
    private final CommonInputService commonInputService;
    private final FileChooser jarFileChooser;

    @FXML
    private RadioButton builtinScenarioRadio;
    @FXML
    private Parent builtinScenarioBox;
    @FXML
    private RadioButton customScenarioRadio;
    @FXML
    private Parent customScenarioBox;
    @FXML
    private ComboBox<ScenarioDescriptorItem> builtinScenariosCombo;
    @FXML
    private TextField jarField;
    @FXML
    private TextField scenarioDescriptorClassField;

    public DefaultNewScenarioDialogController(ScenarioDiscoveryService scenarioDiscoveryService,
                                              CommonOutputService commonOutputService,
                                              CommonInputService commonInputService) {
        this.scenarioDiscoveryService = scenarioDiscoveryService;
        this.commonOutputService = commonOutputService;
        this.commonInputService = commonInputService;

        jarFileChooser = new FileChooser();
        jarFileChooser.setTitle("Browse for jar file...");
        jarFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Jar file (*.jar)", "*.jar"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        builtinScenariosCombo.getItems().clear();

        for (ScenarioDescriptor builtinScenarioDescriptor : scenarioDiscoveryService.getBuiltInScenarios()) {
            builtinScenariosCombo.getItems().add(new ScenarioDescriptorItem(builtinScenarioDescriptor));
        }

        builtinScenariosCombo.setValue(builtinScenariosCombo.getItems().get(0));

        builtinScenarioBox.disableProperty().bind(builtinScenarioRadio.selectedProperty().not());
        customScenarioBox.disableProperty().bind(customScenarioRadio.selectedProperty().not());
    }

    protected ScenarioContext getScenarioContext() {
        if (builtinScenarioRadio.isSelected()) {
            ScenarioDescriptor selectedScenarioDescriptor = builtinScenariosCombo.getValue().getScenarioDescriptor();

            return new ScenarioContext(
                    selectedScenarioDescriptor.getName(),
                    selectedScenarioDescriptor.createScenario(),
                    null);

        } else if (customScenarioRadio.isSelected()) {
            File jarFile = new File(jarField.getText());

            if (!jarFile.exists()) {
                commonOutputService.showWarning("The specified JAR file does not exist!");
                return null;
            }

            JarClassLoader jarClassLoader = JarClassLoader.create(jarFile);
            try {
                Class<?> scenarioDescriptorClass = jarClassLoader.loadClass(scenarioDescriptorClassField.getText());
                ScenarioDescriptor scenarioDescriptor = (ScenarioDescriptor) scenarioDescriptorClass.newInstance();

                return new ScenarioContext(
                        scenarioDescriptor.getName(),
                        scenarioDescriptor.createScenario(),
                        jarFile);

            } catch (ClassNotFoundException ex) {
                commonOutputService.showWarning("Could not find the specified class!");
                return null;
            } catch (InstantiationException | IllegalAccessException ex) {
                commonOutputService.showWarning("Could not instantiate the specified class!");
                return null;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public void browseForJarFile() {
        File jarFile = jarFileChooser.showOpenDialog(null);
        if (jarFile == null) {
            return;
        }

        jarField.setText(jarFile.getAbsolutePath());
    }

    public void scanJarFile() {
        String jarFilePath = jarField.getText();

        File jarFile = new File(jarFilePath);

        if (!jarFile.exists()) {
            commonOutputService.showWarning("The jar file does not exist!");
            return;
        }

        Collection<ScenarioDescriptor> scenarioDescriptors;
        try {
            scenarioDescriptors = scenarioDiscoveryService.getScenariosInJar(jarFile);
        } catch (Exception ex) {
            commonOutputService.showWarning(ex.getMessage());
            return;
        }

        String scenarioDescriptorClassName;
        switch (scenarioDescriptors.size()) {
            case 0:
                scenarioDescriptorClassName = "";
                commonOutputService.showWarning("No scenarios were found in the jar file");
                break;

            case 1:
                scenarioDescriptorClassName = CollectionItems.getSingle(scenarioDescriptors).getClass().getName();
                break;

            default: {
                List<ScenarioDescriptorItem> scenarioDescriptorItems = new ArrayList<>();

                for (ScenarioDescriptor scenarioDescriptor : scenarioDescriptors) {
                    scenarioDescriptorItems.add(new ScenarioDescriptorItem(scenarioDescriptor));
                }

                Collection<ScenarioDescriptorItem> sortedScenarioDescriptorItems = new SortedCollection<>(scenarioDescriptorItems);
                ScenarioDescriptorItem chosenScenarioDescriptorItem = commonInputService.askForItem("Please, choose a scenario from the jar file:", sortedScenarioDescriptorItems);

                if (chosenScenarioDescriptorItem == null) {
                    return;
                }

                ScenarioDescriptor scenarioDescriptor = chosenScenarioDescriptorItem.getScenarioDescriptor();

                scenarioDescriptorClassName = scenarioDescriptor.getClass().getName();

                break;
            }
        }

        scenarioDescriptorClassField.setText(scenarioDescriptorClassName);
    }
}
