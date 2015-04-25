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

package info.gianlucacosta.graphsj3.workspace;

import info.gianlucacosta.helios.undo.UndoRedoSnapshotProvider;

/**
 * Takes snapshots for GraphsJ's undo/redo service
 */
class ScenarioUndoRedoSnapshotProvider implements UndoRedoSnapshotProvider<ScenarioDocumentSnapshot> {

    private final DefaultScenarioDocument scenarioDocument;

    ScenarioUndoRedoSnapshotProvider(DefaultScenarioDocument scenarioDocument) {
        this.scenarioDocument = scenarioDocument;
    }

    @Override
    public ScenarioDocumentSnapshot createSnapshot() {
        return new ScenarioDocumentSnapshot(scenarioDocument);
    }

}
