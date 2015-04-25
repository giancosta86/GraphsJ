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

package info.gianlucacosta.graphsj3.stages.about;

import info.gianlucacosta.graphsj3.DesktopUtils;
import info.gianlucacosta.helios.application.io.CommonOutputService;
import info.gianlucacosta.helios.exceptions.ValidationException;
import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import info.gianlucacosta.helios.fx.dialogs.ButtonsPane;
import info.gianlucacosta.helios.fx.dialogs.DialogOptions;
import info.gianlucacosta.helios.fx.dialogs.NodeDialog;
import info.gianlucacosta.helios.fx.dialogs.buttonspane.CommonButtonsPane;
import info.gianlucacosta.helios.fx.fxml.FXMLLoaderUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.EnumSet;

/**
 * Implementation of AboutDialog
 */
class DefaultAboutDialog extends NodeDialog<Void> implements AboutDialog {

    private final FXMLLoader fxmlLoader;
    private final AppInfoService appInfoService;
    private final CommonOutputService commonOutputService;

    public DefaultAboutDialog(FXMLLoader fxmlLoader, AppInfoService appInfoService, CommonOutputService commonOutputService) {
        super(
                String.format("About %s...", appInfoService.getName()),
                EnumSet.noneOf(DialogOptions.class),
                appInfoService.getMainIcons().values());

        this.fxmlLoader = fxmlLoader;
        this.appInfoService = appInfoService;
        this.commonOutputService = commonOutputService;

        fxmlLoader.setLocation(getClass().getResource(""));
    }

    @Override
    protected Node createContentNode() {
        return FXMLLoaderUtils.loadClassRelatedFxml(fxmlLoader, getClass());
    }

    @Override
    protected ButtonsPane createButtonsPane(Stage dialogStage) {
        CommonButtonsPane buttonsPane = new CommonButtonsPane(dialogStage);

        buttonsPane.addOkButton();


        Button officialWebsiteButton = new Button("Official website");
        officialWebsiteButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DesktopUtils.openBrowser(appInfoService.getWebsite());
            }
        });

        buttonsPane.addButton(officialWebsiteButton);


        Button facebookPageButton = new Button("Facebook page");
        facebookPageButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DesktopUtils.openBrowser(appInfoService.getFacebookPage());
            }
        });

        buttonsPane.addButton(facebookPageButton);


        return buttonsPane;
    }


    @Override
    protected Void retrieveValue() throws ValidationException {
        return null;
    }
}
