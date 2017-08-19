/*§
  ===========================================================================
  GraphsJ
  ===========================================================================
  Copyright (C) 2009-2017 Gianluca Costa
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

import java.nio.file.{Path, WatchEvent}
import java.time.Duration

import info.gianlucacosta.graphsj.windows.main.MainWindowController
import info.gianlucacosta.helios.files.DirectoryWatcher


private class ScenariosDirectoryWatcher(
                                         mainWindowController: MainWindowController[_, _, _],
                                         baseDirectory: Path
                                       ) extends DirectoryWatcher(
  baseDirectory,
  Duration.ofSeconds(8)) {

  override def onEvents(events: List[WatchEvent[_]]): Unit =
    mainWindowController.scenarioRepository =
      new ScenarioRepository(baseDirectory.toFile)

}


