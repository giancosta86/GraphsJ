/*§
  ===========================================================================
  GraphsJ
  ===========================================================================
  Copyright (C) 2009-2016 Gianluca Costa
  ===========================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ===========================================================================
*/

package info.gianlucacosta.graphsj.windows.about

import javafx.fxml.FXML

import info.gianlucacosta.eighthbridge.util.DesktopUtils
import info.gianlucacosta.graphsj.AppInfo

class AboutBoxController {
  def initialize {
    titleLabel.setText(AppInfo.name)

    versionLabel.setText(s"Version ${AppInfo.version}")

    copyrightLabel.setText(s"Copyright © ${AppInfo.copyrightYears} Gianluca Costa.")

    licenseLabel.setText(
      "This software is released under the following license:\n"
        + "\n"
        + s"\t${AppInfo.license}"
    )

    additionalInfoLabel.setText(
      "For further information, please refer to the LICENSE and README files."
    )
  }


  def showWebsite() {
    DesktopUtils.openBrowser(AppInfo.website)
  }


  def showFacebookPage() {
    DesktopUtils.openBrowser(AppInfo.facebookPage)
  }


  @FXML
  private var titleLabel: javafx.scene.control.Label = _

  @FXML
  private var versionLabel: javafx.scene.control.Label = _

  @FXML
  private var copyrightLabel: javafx.scene.control.Label = _

  @FXML
  private var licenseLabel: javafx.scene.control.Label = _

  @FXML
  private var additionalInfoLabel: javafx.scene.control.Label = _
}