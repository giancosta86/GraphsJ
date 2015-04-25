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

package info.gianlucacosta.graphsj3.stages.newscenario;

import info.gianlucacosta.helios.exceptions.ValidationException;
import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import info.gianlucacosta.helios.fx.dialogs.DialogOptions;
import info.gianlucacosta.helios.fx.dialogs.NodeDialog;
import info.gianlucacosta.helios.fx.fxml.FXMLLoaderUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.util.EnumSet;

/**
 * Implementation of NewScenarioDialog
 */
class DefaultNewScenarioDialog extends NodeDialog<ScenarioContext> implements NewScenarioDialog {

    private final FXMLLoader fxmlLoader;

    public DefaultNewScenarioDialog(FXMLLoader fxmlLoader, AppInfoService appInfoService) {
        super(
                "New scenario...",
                EnumSet.noneOf(DialogOptions.class),
                appInfoService.getMainIcons().values()
        );
        this.fxmlLoader = fxmlLoader;
    }

    @Override
    protected Node createContentNode() {
        return FXMLLoaderUtils.loadClassRelatedFxml(fxmlLoader, getClass());
    }

    @Override
    protected ScenarioContext retrieveValue() throws ValidationException {
        DefaultNewScenarioDialogController controller = fxmlLoader.getController();

        return controller.getScenarioContext();
    }

}
