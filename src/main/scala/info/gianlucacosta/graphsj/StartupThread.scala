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

import javafx.fxml.FXMLLoader
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import info.gianlucacosta.eighthbridge.util.fx.dialogs.Alerts
import info.gianlucacosta.graphsj.windows.about.AboutBox
import info.gianlucacosta.graphsj.windows.main.MainWindowController

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.image.Image


private class StartupThread(primaryStage: Stage) extends Thread {
  override def run(): Unit = {
    var splashStage: SplashStage = null

    Platform.runLater {
      splashStage = new SplashStage()
      splashStage.show()

      primaryStage.getIcons.setAll(
        new Image(getClass.getResourceAsStream("icons/mainIcon16.png")),
        new Image(getClass.getResourceAsStream("icons/mainIcon32.png")),
        new Image(getClass.getResourceAsStream("icons/mainIcon64.png")),
        new Image(getClass.getResourceAsStream("icons/mainIcon128.png"))
      )
    }


    setupMainWindowController()


    Platform.runLater {
      primaryStage.width = 800
      primaryStage.height = 600

      primaryStage.centerOnScreen()
      primaryStage.show()
      splashStage.close()
    }
  }


  private def setupMainWindowController(): Unit = {
    val loader = new FXMLLoader(classOf[MainWindowController].getResource("MainWindow.fxml"))

    val rootComponent: BorderPane = loader.load()

    val mainWindowController = loader.getController.asInstanceOf[MainWindowController]
    mainWindowController.stage() = primaryStage


    try {
      AppInfo.ensureScenariosDirectory()
    } catch {
      case ex: Exception =>
        Platform.runLater {
          Alerts.showError(ex.getMessage)
          System.exit(1)
        }
    }


    mainWindowController.scenarioRepository = new ScenarioRepository(AppInfo.ScenariosDirectory)

    new ScenariosDirectoryWatcher(mainWindowController, AppInfo.ScenariosDirectory) {
      start()
    }

    val scene = new Scene {
      root = rootComponent
    }

    Platform.runLater {
      val aboutBox = new AboutBox()
      mainWindowController.aboutBox() = aboutBox

      primaryStage.setScene(scene)
    }
  }
}