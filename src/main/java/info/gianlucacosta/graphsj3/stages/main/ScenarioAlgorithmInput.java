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

package info.gianlucacosta.graphsj3.stages.main;

import info.gianlucacosta.arcontes.algorithms.AlgorithmInput;
import info.gianlucacosta.arcontes.graphs.Graph;
import info.gianlucacosta.arcontes.graphs.GraphContext;
import info.gianlucacosta.arcontes.graphs.Link;
import info.gianlucacosta.arcontes.graphs.Vertex;
import info.gianlucacosta.arcontes.graphs.wrappers.LinkWrapper;
import info.gianlucacosta.arcontes.graphs.wrappers.VertexWrapper;
import info.gianlucacosta.graphsj3.scenarios.Scenario;
import info.gianlucacosta.graphsj3.workspace.ScenarioDocument;
import info.gianlucacosta.helios.application.io.CommonInputService;
import info.gianlucacosta.helios.application.io.CommonOutputService;
import info.gianlucacosta.helios.application.io.CommonQuestionOutcome;
import info.gianlucacosta.helios.collections.general.SortedCollection;

import java.util.Collection;

/**
 * Implementation of AlgorithmInput based on the GUI provided by GraphsJ
 */
class ScenarioAlgorithmInput implements AlgorithmInput {

    private final CommonOutputService commonOutputService;
    private final CommonInputService commonInputService;
    private final ScenarioDocument scenarioDocument;

    public ScenarioAlgorithmInput(CommonOutputService commonOutputService, CommonInputService commonInputService, ScenarioDocument scenarioDocument) {
        this.commonOutputService = commonOutputService;
        this.commonInputService = commonInputService;
        this.scenarioDocument = scenarioDocument;
    }

    @Override
    public String askForString(String prompt, String initialValue) {
        return commonInputService.askForString(prompt, initialValue);
    }

    @Override
    public Integer askForInteger(String prompt, Integer initialValue) {
        return commonInputService.askForInteger(prompt, initialValue);
    }

    @Override
    public Double askForDouble(String prompt, Double initialValue) {
        return commonInputService.askForDouble(prompt, initialValue);
    }

    @Override
    public <T> T askForItem(String prompt, Collection<T> sourceItems) {
        return askForItem(prompt, sourceItems, null);
    }

    @Override
    public <T> T askForItem(String prompt, Collection<T> sourceItems, T initialValue) {
        return commonInputService.askForItem(prompt, sourceItems, initialValue);
    }

    @Override
    public Vertex askForVertex(String prompt, Collection<Vertex> sourceVertexes) {
        return askForVertex(prompt, sourceVertexes, null);
    }

    @Override
    public Vertex askForVertex(String prompt, Collection<Vertex> sourceVertexes, Vertex initialVertex) {
        GraphContext graphContext = scenarioDocument.getGraphContext();
        Scenario scenario = scenarioDocument.getScenario();

        Collection<VertexWrapper> vertexWrappers = new SortedCollection<>();
        for (Vertex vertex : sourceVertexes) {
            VertexWrapper vertexWrapper = scenario.createInputVertexWrapper(graphContext, vertex);
            vertexWrappers.add(vertexWrapper);
        }

        if (vertexWrappers.isEmpty()) {
            commonOutputService.showWarning("No vertexes available!");
            return null;
        }

        VertexWrapper initialVertexWrapper = null;
        if (initialVertex != null) {
            initialVertexWrapper = scenario.createInputVertexWrapper(graphContext, initialVertex);
        }

        VertexWrapper chosenVertexWrapper = commonInputService.askForItem(prompt, vertexWrappers, initialVertexWrapper);

        if (chosenVertexWrapper == null) {
            return null;
        }

        return chosenVertexWrapper.getVertex();
    }

    @Override
    public Vertex askForVertex(Graph graph, String prompt) {
        return askForVertex(graph, prompt, null);
    }

    @Override
    public Vertex askForVertex(Graph graph, String prompt, Vertex initialVertex) {
        return askForVertex(prompt, graph.getVertexes(), initialVertex);
    }

    @Override
    public Link askForLink(String prompt, Collection<Link> sourceLinks) {
        return askForLink(prompt, sourceLinks, null);
    }

    @Override
    public Link askForLink(String prompt, Collection<Link> sourceLinks, Link initialLink) {
        GraphContext graphContext = scenarioDocument.getGraphContext();
        Scenario scenario = scenarioDocument.getScenario();

        Collection<LinkWrapper> linkWrappers = new SortedCollection<>();
        for (Link link : sourceLinks) {
            LinkWrapper linkWrapper = scenario.createInputLinkWrapper(graphContext, link);
            linkWrappers.add(linkWrapper);
        }

        if (linkWrappers.isEmpty()) {
            commonOutputService.showWarning("No links available!");
            return null;
        }

        LinkWrapper initialLinkWrapper = null;
        if (initialLink != null) {
            initialLinkWrapper = scenario.createInputLinkWrapper(graphContext, initialLink);
        }

        LinkWrapper chosenLinkWrapper = commonInputService.askForItem(prompt, linkWrappers, initialLinkWrapper);

        if (chosenLinkWrapper == null) {
            return null;
        }

        return chosenLinkWrapper.getLink();
    }

    @Override
    public Link askForLink(Graph graph, String prompt) {
        return askForLink(graph, prompt, null);
    }

    @Override
    public Link askForLink(Graph graph, String prompt, Link initialLink) {
        return askForLink(prompt, graph.getLinks(), initialLink);
    }

    @Override
    public CommonQuestionOutcome askYesNoQuestion(String prompt) {
        return commonInputService.askYesNoQuestion(prompt);
    }

    @Override
    public CommonQuestionOutcome askYesNoCancelQuestion(String prompt) {
        return commonInputService.askYesNoCancelQuestion(prompt);
    }
}
