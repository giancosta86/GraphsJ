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

package info.gianlucacosta.graphsj3.stages.newscenario;

import info.gianlucacosta.graphsj3.scenarios.Scenario;

import java.io.File;

/**
 * Used by NewScenarioDialog to return all the required information about the
 * scenario being created.
 * <p>
 * In particular, such information includes:
 * <ul>
 * <li>The scenario name, provided by the scenario descriptor</li>
 * <li>The scenario itself</li>
 * <li>The jar file where the scenario and its supporting classes reside - or null, if it's a built-in scenario</li>
 * </ul>
 */
public class ScenarioContext {

    private final String scenarioName;
    private final Scenario scenario;
    private final File referenceJarFile;

    public ScenarioContext(String scenarioName, Scenario scenario, File referenceJarFile) {
        this.scenarioName = scenarioName;
        this.scenario = scenario;
        this.referenceJarFile = referenceJarFile;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public File getReferenceJarFile() {
        return referenceJarFile;
    }

}
