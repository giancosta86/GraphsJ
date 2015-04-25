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

import info.gianlucacosta.helios.fx.fxml.FXMLLoaderUtils;
import info.gianlucacosta.helios.fx.style.CssLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Implementation of MainWindow
 */
class DefaultMainWindow implements MainWindow {

    private final FXMLLoader fxmlLoader;

    public DefaultMainWindow(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
        fxmlLoader.setLocation(getClass().getResource("toolbarIcons"));
    }

    @Override
    public void show(Stage stage) {
        Parent root = FXMLLoaderUtils.loadClassRelatedFxml(fxmlLoader, getClass());

        final DefaultMainWindowController controller = fxmlLoader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        new CssLoader(scene).addCssForClass(getClass());
        stage.setScene(scene);

        stage.setWidth(1000);
        stage.setHeight(700);
        stage.centerOnScreen();

        stage.show();
    }

}
