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

package info.gianlucacosta.graphsj.windows

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ProgressIndicator
import scalafx.scene.layout.BorderPane
import scalafx.stage.{Modality, Stage, WindowEvent}

class BusyDialog(owner: Stage, windowTitle: String) extends Stage {
  initOwner(owner)
  initModality(Modality.WINDOW_MODAL)
  title = windowTitle

  resizable = false

  handleEvent(WindowEvent.WindowCloseRequest) {
    (event: WindowEvent) => {
      event.consume()
    }
  }

  scene = new Scene {
    root = new BorderPane {
      center = new ProgressIndicator {
        prefWidth = 150
        prefHeight = 150
        margin = Insets(50)
      }
    }
  }

  centerOnScreen()


  def run(action: => Unit): Unit = {
    new BusyThread(this, action).start()
  }


  private class BusyThread(busyDialog: BusyDialog, action: => Unit) extends Thread {
    setDaemon(true)

    override def run(): Unit = {
      Platform.runLater {
        busyDialog.show()
      }

      try {
        action
      } finally {
        Platform.runLater {
          busyDialog.close()
        }
      }
    }
  }

}
