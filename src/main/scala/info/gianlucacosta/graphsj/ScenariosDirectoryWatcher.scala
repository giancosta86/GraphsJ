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
import java.nio.file.{FileSystems, StandardWatchEventKinds}
import java.util.concurrent.TimeUnit

import info.gianlucacosta.graphsj.windows.main.MainWindowController

import scala.collection.JavaConversions._

private class ScenariosDirectoryWatcher(mainWindowController: MainWindowController, baseDirectory: File) extends Thread {
  private val watcher = FileSystems.getDefault.newWatchService()

  private val TimeOutInSeconds = 8

  setDaemon(true)


  override def run(): Unit = {
    while (true) {
      if (baseDirectory.isDirectory) {
        baseDirectory.toPath.register(
          watcher,
          StandardWatchEventKinds.ENTRY_CREATE,
          StandardWatchEventKinds.ENTRY_MODIFY,
          StandardWatchEventKinds.ENTRY_DELETE)

        var keyValid = true

        while (keyValid) {
          val watchKey = watcher.poll(TimeOutInSeconds, TimeUnit.SECONDS)

          if (watchKey != null) {
            val events = watchKey.pollEvents()

            if (events.nonEmpty) {
              mainWindowController.scenarioRepository = new ScenarioRepository(baseDirectory)
            }

            keyValid = watchKey.reset()
          } else {
            keyValid = baseDirectory.isDirectory
          }
        }
      }

      Thread.sleep(TimeOutInSeconds * 1000)
    }
  }
}
