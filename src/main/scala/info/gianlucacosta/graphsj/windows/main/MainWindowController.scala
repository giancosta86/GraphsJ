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

import java.io._
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.concurrent.Callable
import javafx.beans.Observable
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.MouseEvent

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.ConversionException
import com.thoughtworks.xstream.io.xml.StaxDriver
import info.gianlucacosta.eighthbridge.fx.canvas.GraphCanvas
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualGraph
import info.gianlucacosta.eighthbridge.util.DesktopUtils
import info.gianlucacosta.eighthbridge.util.fx.dialogs.{Alerts, InputDialogs}
import info.gianlucacosta.graphsj._
import info.gianlucacosta.graphsj.windows.about.AboutBox

import scalafx.Includes._
import scalafx.beans.property.{BooleanProperty, ObjectProperty}
import scalafx.geometry.Dimension2D
import scalafx.scene.control._
import scalafx.scene.image.{Image, ImageView}
import scalafx.stage.{FileChooser, Stage, WindowEvent}


class MainWindowController {
  private var xstream: XStream = _

  private val scenarioRepositoryLock = new Object

  val stage = ObjectProperty[Stage](null.asInstanceOf[Stage])

  val aboutBox = ObjectProperty[AboutBox](null.asInstanceOf[AboutBox])


  private var _scenarioRepository: ScenarioRepository = _

  def scenarioRepository: ScenarioRepository = {
    scenarioRepositoryLock.synchronized {
      _scenarioRepository
    }
  }


  def scenarioRepository_=(newValue: ScenarioRepository): Unit = {
    scenarioRepositoryLock.synchronized {
      _scenarioRepository = newValue

      xstream = new XStream(new StaxDriver)
      xstream.setClassLoader(newValue.scenariosClassLoader)
    }
  }


  stage.addListener((observable: Observable) => {
    require(stage() != null)

    stage().onCloseRequest = (windowEvent: WindowEvent) => {
      if (!canLeaveDocument) {
        windowEvent.consume()
      }
    }


    stage().title <== Bindings.createStringBinding(new Callable[String] {
      override def call(): String = {
        String.format("%s%s%s",
          AppInfo.name,

          scenarioFile().map(
            file => " - " + file.getName
          ).getOrElse(
            ""
          ),

          if (modified()) " *" else ""
        )
      }
    },

      scenarioFile,
      modified
    )


    setupMenusAndToolbar()
  })


  private val scenarioFile = ObjectProperty[Option[File]](None)
  scenarioFile.addListener((observable: Observable) => {
    modified() = false
  })

  private val modified = BooleanProperty(false)

  private val runState = ObjectProperty[RunState](NotRunning)

  private val running = BooleanProperty(false)
  running <== runState =!= NotRunning

  running.addListener((o: javafx.beans.value.ObservableValue[_ <: java.lang.Boolean], oldValue: java.lang.Boolean, newValue: java.lang.Boolean) => {
    if (newValue != oldValue) {
      if (running()) {
        algorithm = scenario().get.createAlgorithm()
        stepIndex = 0
        runtimeCanvas() = Some(new GraphCanvas(
          scenario().get.createRuntimeController(),
          designGraph)
        )

        graphCanvasPane.content = runtimeCanvas().get
      } else {
        graphCanvasPane.content = designCanvas().get
      }
    }
  })


  private val scenario = ObjectProperty[Option[Scenario]](None)

  private val designCanvas = ObjectProperty[Option[GraphCanvas]](None)
  designCanvas.addListener((observable: Observable) => {
    designCanvas().get.graph.addListener((graphObservable: Observable) => {
      modified() = true
    })

    graphCanvasPane.content = designCanvas().get
    designCanvas().get.requestFocus()

    designCanvas().get.addEventHandler(MouseEvent.ANY, new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = {
        designCanvas().get.requestFocus()

        event.consume()
      }
    })


    ()
  })

  private val runtimeCanvas = ObjectProperty[Option[GraphCanvas]](None)
  runtimeCanvas.addListener((observable: Observable) => {
    runtimeCanvas().get.requestFocus()

    runtimeCanvas().get.addEventHandler(MouseEvent.ANY, new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = {
        runtimeCanvas().get.requestFocus()

        event.consume()
      }
    })

    ()
  })


  private var algorithm: Algorithm = _
  private var stepIndex: Int = _


  private def designGraph: VisualGraph = designCanvas().get.graph()

  private def designGraph_=(newGraph: VisualGraph): Unit = {
    designCanvas().get.graph() = newGraph
  }


  private def runtimeGraph: VisualGraph = runtimeCanvas().get.graph()

  private def runtimeGraph_=(newGraph: VisualGraph): Unit = {
    runtimeCanvas().get.graph() = newGraph
  }


  private val scenarioFileChooser: FileChooser = {
    val fileChooser = new FileChooser

    fileChooser.extensionFilters.setAll(
      new FileChooser.ExtensionFilter("Scenario", s"*${AppInfo.DefaultExtension}")
    )

    fileChooser.title = AppInfo.name

    fileChooser
  }


  private val consoleFileChooser: FileChooser = {
    val fileChooser = new FileChooser

    fileChooser.extensionFilters.setAll(
      new FileChooser.ExtensionFilter("Text file", "*.txt"),
      new FileChooser.ExtensionFilter("Any file", "*.*")
    )

    fileChooser.title = AppInfo.name

    fileChooser
  }


  private var latestScenarioFile: Option[File] = None
  private var latestConsoleFile: Option[File] = None


  private lazy val outputConsole: OutputConsole = new OutputConsole {
    override def write(value: Any): Unit = {
      consoleArea.appendText(value.toString)
    }

    override def writeln(value: Any): Unit = {
      consoleArea.appendText(value.toString + "\n")
    }

    override def writeln(): Unit = {
      consoleArea.appendText("\n")
    }


    override def writeHeader(header: String): Unit = {
      writeln("------------")
      writeln(header)
      writeln("------------")
    }
  }


  private def setupMenusAndToolbar() {
    newMenuItem.disable <== running
    bindButton(newButton, newMenuItem)

    openMenuItem.disable <== running
    bindButton(openButton, openMenuItem)

    saveMenuItem.disable <== (scenario === None) || running || (!modified)
    bindButton(saveButton, saveMenuItem)

    saveAsMenuItem.disable <== (scenario === None) || running
    bindButton(saveAsButton, saveAsMenuItem)


    fullRunMenuItem.disable <== (scenario === None) || (runState === InFullRun) || (runState === Complete)
    bindButton(fullRunButton, fullRunMenuItem)

    runStepMenuItem.disable <== (scenario === None) || (runState === InFullRun) || (runState === Complete)
    bindButton(runStepButton, runStepMenuItem)

    stopRunMenuItem.disable <== (scenario === None) || (runState === NotRunning) || (runState === InFullRun)
    bindButton(stopRunButton, stopRunMenuItem)

    editMenu.disable <== (scenario === None) || running
    scenarioMenu.disable <== (scenario === None) || running
    runMenu.disable <== (scenario === None)

    bindButton(helpButton, helpMenuItem)

    bindButton(aboutButton, aboutMenuItem)
  }


  private def bindButton(button: Button, menuItem: MenuItem) {
    val buttonId = button.id()

    val actionName = buttonId.substring(0, buttonId.lastIndexOf("Button"))
    val expectedMenuItemId = actionName + "MenuItem"

    if (menuItem.id() != expectedMenuItemId) {
      throw new IllegalArgumentException(
        s"'${menuItem.id()}' should be named '${expectedMenuItemId}' instead"
      )
    }


    val actionImage = new Image(getClass.getResourceAsStream(
      s"actionIcons/${actionName}.png"
    ))

    menuItem.graphic = new ImageView(actionImage)
    button.graphic = new ImageView(actionImage)


    button.disable <== menuItem.disable
    button.onAction <== menuItem.onAction


    button.tooltip = new scalafx.scene.control.Tooltip() {
      text <== menuItem.text
    }
  }

  /*
   * ACTIONS
   */
  def newScenario(): Unit = {
    if (!canLeaveDocument) {
      return
    }

    if (_scenarioRepository.scenarios.isEmpty) {
      Alerts.showWarning(s"${AppInfo.name} requires at least one scenario.\n\nPlease, download one or more .jar files providing scenarios and copy them to the scenarios directory.")
      showScenariosDirectory()
      return
    }

    val scenarioInput = InputDialogs.askForItem(
      "Scenario:",
      _scenarioRepository.scenarios,
      header = "New scenario..."
    )

    if (scenarioInput.isEmpty) {
      return
    }

    val newScenario = scenarioInput.get.clone()

    try {
      val newDesignCanvas = new GraphCanvas(
        newScenario.createDesignController(),
        newScenario.createDesignGraph()
      )

      scenario() = Some(newScenario)
      designCanvas() = Some(newDesignCanvas)


      scenarioFile() = None
    } catch {
      case ex: Exception =>
        Alerts.showException(ex)

        ex.printStackTrace(System.err)
    }
  }


  def openScenario() {
    if (!canLeaveDocument) {
      return
    }

    latestScenarioFile.foreach(file =>
      scenarioFileChooser.initialDirectory = file.getParentFile
    )

    val selectedFile = scenarioFileChooser.showOpenDialog(stage())
    if (selectedFile == null) {
      return
    }

    try {
      val sourceReader = new InputStreamReader(new FileInputStream(selectedFile), "UTF-8")
      try {
        val document = xstream.fromXML(sourceReader).asInstanceOf[GraphDocument]

        scenario() = Some(document.scenario)
        designCanvas() = Some(new GraphCanvas(
          document.scenario.createDesignController(),
          document.designGraph
        ))
      } finally {
        sourceReader.close()
      }


      scenarioFile() = Some(selectedFile)
      latestScenarioFile = Some(selectedFile)
    } catch {
      case ex: ConversionException =>
        val scenarioName = ex.getShortMessage.substring(0, ex.getShortMessage.lastIndexOf(":")).trim

        Alerts.showWarning(s"The requested scenario cannot be loaded:\n\n'${scenarioName}'\n\nPlease, ensure that the related JAR file is available in the scenarios directory.")

        ex.printStackTrace(System.err)

        showScenariosDirectory()


      case ex: Exception =>
        Alerts.showException(ex)

        ex.printStackTrace(System.err)
    }
  }


  def saveScenario(): Boolean = {
    if (scenarioFile().isEmpty) {
      saveScenarioAs()
    } else {
      doActualSaving(scenarioFile().get)
    }
  }


  def saveScenarioAs(): Boolean = {
    latestScenarioFile.foreach(file =>
      scenarioFileChooser.initialDirectory = file.getParentFile
    )

    val selectedFile = scenarioFileChooser.showSaveDialog(stage())
    if (selectedFile == null) {
      return false
    }

    val actualFile = if (!selectedFile.getName.endsWith(AppInfo.DefaultExtension)) {
      new File(selectedFile.getAbsolutePath + AppInfo.DefaultExtension)
    } else {
      selectedFile
    }


    if (!doActualSaving(actualFile)) {
      return false
    }

    latestScenarioFile = Some(actualFile)
    scenarioFile() = Some(actualFile)
    true
  }


  private def doActualSaving(targetFile: File): Boolean = {
    try {
      val document = GraphDocument(
        scenario().get,
        designGraph
      )


      val targetWriter = new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8")
      try {
        xstream.toXML(document, targetWriter)
      } finally {
        targetWriter.close()
      }


      modified() = false
      true
    } catch {
      case ex: Exception =>
        Alerts.showException(ex)

        ex.printStackTrace(System.err)

        false
    }
  }


  def exitProgram(): Unit = {
    stage().close()
  }


  private def canLeaveDocument: Boolean = {
    if (modified()) {
      val inputResult = InputDialogs.askYesNoCancel("Do you wish to save your document?")

      inputResult match {
        case Some(true) =>
          saveScenario()

        case Some(false) =>
          true

        case None =>
          false
      }

    } else {
      true
    }
  }


  def selectAll(): Unit = {
    designGraph = designGraph.selectAll
  }


  def chooseCanvasSize(): Unit = {
    val newWidth = InputDialogs.askForDouble("Width:", designGraph.dimension.width, 1)
    if (newWidth.isEmpty) {
      return
    }

    val newHeight = InputDialogs.askForDouble("Height:", designGraph.dimension.height, 1)
    if (newHeight.isEmpty) {
      return
    }


    designGraph = designGraph.visualCopy(dimension = new Dimension2D(newWidth.get, newHeight.get))
  }


  def saveConsoleAs(): Unit = {
    latestConsoleFile.foreach(file =>
      consoleFileChooser.initialDirectory = file.getParentFile
    )

    val chosenFile = consoleFileChooser.showSaveDialog(stage())
    if (chosenFile == null) {
      return
    }

    var actualFile = chosenFile
    val defaultExtensionFilter = consoleFileChooser.getExtensionFilters.get(0)
    if (consoleFileChooser.getSelectedExtensionFilter == defaultExtensionFilter) {
      val defaultExtension = defaultExtensionFilter.getExtensions.get(0).substring(1)

      if (!chosenFile.getName.endsWith(defaultExtension)) {
        actualFile = new File(chosenFile.getAbsolutePath + defaultExtension)
      }
    }

    latestConsoleFile = Some(actualFile)
    try {
      Files.write(actualFile.toPath, consoleArea.getText().getBytes(Charset.forName("utf-8")))
    } catch {
      case ex: IOException =>
        Alerts.showException(ex)
    }
  }


  def clearConsole(): Unit = {
    consoleArea.clear()
  }


  def showScenarioName(): Unit = {
    Alerts.showInfo(scenario().get.name, "Scenario name")
  }

  def showScenarioHelp(): Unit = {
    scenario().get.showHelp()
  }


  def showScenarioSettings(): Unit = {
    scenario().get.showSettings()
  }


  def run(): Unit = {
    runState() = InFullRun

    while (internalRunStep()) {}
  }


  def runStep(): Unit = {
    runState() = InStepRun

    internalRunStep()
  }


  private def internalRunStep(): Boolean = {
    try {
      val (graphResult, canProceed) =
        algorithm.runStep(stepIndex, runtimeGraph, outputConsole)

      require(graphResult != null)
      runtimeGraph = graphResult

      if (canProceed) {
        stepIndex += 1
        true
      } else {
        runState() = Complete
        false
      }
    } catch {
      case ex: Exception =>
        Alerts.showWarning(if (ex.getMessage != null && ex.getMessage.nonEmpty) ex.getMessage else ex.getClass.getSimpleName)

        runState() = NotRunning

        false
    }
  }


  def stopRun(): Unit = {
    runState() = NotRunning
  }


  def showScenariosDirectory(): Unit = {
    AppInfo.ScenariosDirectory.mkdirs()

    if (AppInfo.ScenariosDirectory.isDirectory) {
      DesktopUtils.openFile(AppInfo.ScenariosDirectory)
    } else {
      Alerts.showError(s"Cannot create the scenarios directory:\n\n'${AppInfo.ScenariosDirectory}'")
    }
  }


  def showHelp(): Unit = {
    DesktopUtils.openBrowser(AppInfo.website)
  }


  def showAboutBox(): Unit = {
    aboutBox().showAndWait()
  }


  @FXML
  protected var graphCanvasPane: javafx.scene.control.ScrollPane = _


  @FXML
  protected var consoleArea: javafx.scene.control.TextArea = _


  @FXML
  protected var newMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var openMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var saveMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var saveAsMenuItem: javafx.scene.control.MenuItem = _


  @FXML
  protected var editMenu: javafx.scene.control.Menu = _


  @FXML
  protected var scenarioMenu: javafx.scene.control.Menu = _


  @FXML
  protected var runMenu: javafx.scene.control.Menu = _

  @FXML
  protected var fullRunMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var runStepMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var stopRunMenuItem: javafx.scene.control.MenuItem = _


  @FXML
  protected var helpMenuItem: javafx.scene.control.MenuItem = _

  @FXML
  protected var aboutMenuItem: javafx.scene.control.MenuItem = _


  @FXML
  protected var newButton: javafx.scene.control.Button = _

  @FXML
  protected var openButton: javafx.scene.control.Button = _

  @FXML
  protected var saveButton: javafx.scene.control.Button = _

  @FXML
  protected var saveAsButton: javafx.scene.control.Button = _


  @FXML
  protected var fullRunButton: javafx.scene.control.Button = _

  @FXML
  protected var runStepButton: javafx.scene.control.Button = _

  @FXML
  protected var stopRunButton: javafx.scene.control.Button = _


  @FXML
  protected var helpButton: javafx.scene.control.Button = _

  @FXML
  protected var aboutButton: javafx.scene.control.Button = _

}