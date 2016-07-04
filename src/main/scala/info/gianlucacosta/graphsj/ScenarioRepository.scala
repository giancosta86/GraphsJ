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
import java.lang.reflect.Modifier

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder

import scala.collection.JavaConversions._
import scala.reflect.internal.util.ScalaClassLoader.URLClassLoader

class ScenarioRepository(baseDirectory: File) {
  private val jarFiles: Array[File] =
    if (baseDirectory.isDirectory)
      baseDirectory
        .listFiles()
        .filter(_.getName.toLowerCase.endsWith(".jar"))
    else
      Array[File]()


  val scenariosClassLoader = new URLClassLoader(
    jarFiles.map(_.toURI.toURL),
    getClass.getClassLoader
  )


  val scenarioFactories =
    jarFiles
      .flatMap(jarFile =>
        try {
          val configuration =
            new ConfigurationBuilder()
              .addClassLoader(scenariosClassLoader)
              .setUrls(jarFile.toURI.toURL)

          val reflections =
            new Reflections(configuration)

          reflections
            .getSubTypesOf(classOf[ScenarioFactory[_, _, _]])
            .filter(scenarioFactoryClass =>
              !Modifier.isAbstract(scenarioFactoryClass.getModifiers) &&
                !scenarioFactoryClass.isInterface
            )
            .map(scenarioFactoryClass =>
              scenarioFactoryClass.newInstance()
            )
        } catch {
          case ex: Exception =>
            ex.printStackTrace(System.err)
            List()
        }
      )
      .sortBy(_.scenarioName)
}
