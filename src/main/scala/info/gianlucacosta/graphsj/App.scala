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

package info.gianlucacosta.graphsj

import java.io.File
import javafx.fxml.FXMLLoader
import javafx.stage.Stage

import info.gianlucacosta.graphsj.icons.MainIcon
import info.gianlucacosta.graphsj.windows.main.MainWindowController
import info.gianlucacosta.helios.apps.{AppInfo, AuroraAppInfo}
import info.gianlucacosta.helios.desktop.DesktopUtils
import info.gianlucacosta.helios.fx.apps.{AppBase, AppMain, SplashStage}

import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane


object App extends AppMain[App](classOf[App]) {
  private val MajorVersion =
    ArtifactInfo.version.split('.').head


  val DefaultExtension =
    s".gj${MajorVersion}"


  val ScenariosDirectory = new File(
    DesktopUtils.homeDirectory.get,
    s".${ArtifactInfo.name}${MajorVersion}"
  )

  def ensureScenariosDirectory(): Unit = {
    if (!ScenariosDirectory.isDirectory && !ScenariosDirectory.mkdirs()) {
      throw new RuntimeException(s"Cannot create the scenarios directory:\n'${ScenariosDirectory}'")
    }
  }
}


class App extends AppBase(AuroraAppInfo(ArtifactInfo, MainIcon)) {
  override def startup(appInfo: AppInfo, splashStage: SplashStage, primaryStage: Stage): Unit = {
    App.ensureScenariosDirectory()

    val loader =
      new FXMLLoader(
        classOf[MainWindowController[_, _, _]].getResource("MainWindow.fxml")
      )


    val rootComponent: javafx.scene.layout.BorderPane =
      loader.load()


    val mainWindowController =
      loader.getController.asInstanceOf[MainWindowController[_, _, _]]


    val scenarioRepository =
      new ScenarioRepository(App.ScenariosDirectory)


    mainWindowController.setup(primaryStage, appInfo, scenarioRepository)


    new ScenariosDirectoryWatcher(mainWindowController, App.ScenariosDirectory.toPath) {
      start()
    }


    val scene = new Scene {
      root =
        new BorderPane(rootComponent)
    }


    Platform.runLater {
      primaryStage.setMaximized(true)

      primaryStage.setScene(scene)
    }
  }
}
