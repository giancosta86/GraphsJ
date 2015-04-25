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

package info.gianlucacosta.graphsj3.stages.newscenario;

import info.gianlucacosta.graphsj3.scenarios.ScenarioDescriptor;
import info.gianlucacosta.graphsj3.scenarios.cpm.CpmScenarioDescriptor;
import info.gianlucacosta.graphsj3.scenarios.fordfulkerson.FordFulkersonScenarioDescriptor;
import info.gianlucacosta.graphsj3.scenarios.spp.SppScenarioDescriptor;
import info.gianlucacosta.graphsj3.scenarios.sst.SstScenarioDescriptor;
import info.gianlucacosta.helios.jar.JarReflector;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of ScenarioDiscoveryService
 */
class DefaultScenarioDiscoveryService implements ScenarioDiscoveryService {

    @Override
    public Collection<ScenarioDescriptor> getBuiltInScenarios() {
        List<ScenarioDescriptor> result = new ArrayList<>();

        result.add(new SstScenarioDescriptor());
        result.add(new SppScenarioDescriptor());
        result.add(new CpmScenarioDescriptor());
        result.add(new FordFulkersonScenarioDescriptor());

        return result;
    }

    @Override
    public Collection<ScenarioDescriptor> getScenariosInJar(File jarFile) {

        try (JarReflector jarReflector = new JarReflector(jarFile)) {
            List<ScenarioDescriptor> scenarioDescriptors = new ArrayList<>();

            for (Class<?> jarClass : jarReflector.getClasses()) {
                if (ScenarioDescriptor.class.isAssignableFrom(jarClass)
                        && !jarClass.isInterface()
                        && !Modifier.isAbstract(jarClass.getModifiers())) {

                    ScenarioDescriptor scenarioDescriptor;
                    try {
                        scenarioDescriptor = (ScenarioDescriptor) jarClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                    scenarioDescriptors.add(scenarioDescriptor);
                }
            }

            return scenarioDescriptors;
        }
    }

}
