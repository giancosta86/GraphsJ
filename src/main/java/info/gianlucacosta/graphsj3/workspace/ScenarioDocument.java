/*§
  ===========================================================================
  GraphsJ
  ===========================================================================
  Copyright (C) 2009-2016 Gianluca Costa
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

import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.workspace.Document;

/**
 * A GraphsJ document.
 * <p>
 * It contains information such as:
 * <ul>
 * <li>the scenario name</li> <li>the scenario itself</li>
 * <li>the graph context on which the scenario operates</li>
 * </ul>
 */
public interface ScenarioDocument extends Document {

    String getScenarioName();

    Scenario getScenario();

    GraphContext getGraphContext();

    void addGraphContextChangedListener(TriggerListener listener);

    void removeGraphContextChangedListener(TriggerListener listener);

}
