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

package info.gianlucacosta.graphsj3;

import info.gianlucacosta.graphsj3.stages.main.MainWindow;
import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import info.gianlucacosta.helios.fx.splash.DefaultSplashStage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * The application starting point
 */
public class Main extends Application {

    private final AppInfoService appInfoService;

    public Main() {
        try {
            appInfoService = new GraphsJAppInfoService();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        final DefaultSplashStage splashStage = new DefaultSplashStage(
                appInfoService.getMainIcon(128),
                appInfoService.getMainIcons().values()
        );

        splashStage.show();

        Task<Void> initTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    stage.getIcons().addAll(appInfoService.getMainIcons().values());

                    initLogging();
                    ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/info/gianlucacosta/graphsj3/context.xml"});
                    final MainWindow mainScene = context.getBean(MainWindow.class);

                    Platform.runLater(() -> {
                        mainScene.show(stage);

                        splashStage.close();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                    throw ex;
                }

                return null;
            }

        };

        new Thread(initTask).start();
    }

    private void initLogging() {
        String log4jConfigFile;
        if (appInfoService.isRelease()) {
            log4jConfigFile = "log4j_release.xml";
        } else {
            log4jConfigFile = "log4j_debug.xml";
        }

        DOMConfigurator.configure(Main.class.getResource(log4jConfigFile));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
