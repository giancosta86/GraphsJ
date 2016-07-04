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

package info.gianlucacosta.graphsj.windows.main

import info.gianlucacosta.eighthbridge.fx.canvas.basic.{BasicLink, BasicVertex}
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualGraph
import info.gianlucacosta.graphsj.Scenario

private case class GraphDocument[V <: BasicVertex[V], L <: BasicLink[L], G <: VisualGraph[V, L, G]]
(scenario: Scenario[V, L, G], designGraph: G)
