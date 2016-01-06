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

import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.GraphContextProxy;
import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.helios.beans.events.TriggerEvent;
import info.gianlucacosta.helios.beans.events.TriggerListener;
import info.gianlucacosta.helios.metainfo.MetaInfoRepository;
import info.gianlucacosta.helios.reflection.Locator;
import info.gianlucacosta.helios.workspace.AbstractDocument;

import java.util.Objects;

/**
 * Implementation of ScenarioDocument
 */
public class DefaultScenarioDocument extends AbstractDocument implements ScenarioDocument {

    private final String scenarioName;
    private final Scenario scenario;
    private final TriggerEvent graphContextChangedEvent = new TriggerEvent();
    private final GraphContextProxy graphContextProxy;
    private GraphContext graphContext;

    DefaultScenarioDocument(String scenarioName, Scenario scenario) {
        this.scenarioName = scenarioName;
        this.scenario = scenario;
        this.graphContext = scenario.createGraphContext();

        this.graphContextProxy = new GraphContextProxy(
                new Locator<Graph>() {
                    @Override
                    public Graph locate() {
                        return graphContext.getGraph();
                    }
                },
                new Locator<MetaInfoRepository>() {
                    @Override
                    public MetaInfoRepository locate() {
                        return graphContext.getMetaInfoRepository();
                    }
                }
        );
    }

    @Override
    public String getScenarioName() {
        return scenarioName;
    }

    @Override
    public Scenario getScenario() {
        return scenario;
    }

    @Override
    public GraphContext getGraphContext() {
        return graphContextProxy;
    }

    void setGraphContext(GraphContext source) {
        boolean changed = !Objects.equals(graphContext, source);
        graphContext = source;

        if (changed) {
            graphContextChangedEvent.fire();
        }
    }

    @Override
    public void addGraphContextChangedListener(TriggerListener listener) {
        graphContextChangedEvent.addListener(listener);
    }

    @Override
    public void removeGraphContextChangedListener(TriggerListener listener) {
        graphContextChangedEvent.removeListener(listener);
    }

}
