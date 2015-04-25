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

import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the "About..." dialog
 */
public class DefaultAboutDialogController implements Initializable {

    private final AppInfoService appInfoService;
    @FXML
    private ImageView appIconView;
    @FXML
    private Label appNameLabel;
    @FXML
    private Label appVersionLabel;
    @FXML
    private Label appCopyrightLabel;
    @FXML
    private Label appLicenseLabel;

    public DefaultAboutDialogController(AppInfoService appInfoService) {
        this.appInfoService = appInfoService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appIconView.setImage(appInfoService.getMainIcon(128));
        appNameLabel.setText(appInfoService.getName());
        appVersionLabel.setText(String.format("Version %s", appInfoService.getVersion()));
        appCopyrightLabel.setText(String.format("Copyright © %s Gianluca Costa", appInfoService.getCopyrightYears()));
        appLicenseLabel.setText(String.format(appInfoService.getLicense()));
    }

}
