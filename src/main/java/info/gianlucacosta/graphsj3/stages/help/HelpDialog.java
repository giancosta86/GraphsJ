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

package info.gianlucacosta.graphsj3.stages.help;

import info.gianlucacosta.graphsj3.Main;
import info.gianlucacosta.helios.fx.application.info.AppInfoService;
import info.gianlucacosta.helios.fx.dialogs.ButtonsPane;
import info.gianlucacosta.helios.fx.dialogs.DialogOptions;
import info.gianlucacosta.helios.fx.dialogs.HtmlDialog;
import javafx.stage.Stage;

import java.util.EnumSet;

/**
 * The help dialog
 */
public class HelpDialog extends HtmlDialog {

    public HelpDialog(AppInfoService appInfoService) {
        super(
                String.format("%s - Online help", appInfoService.getTitle()),
                EnumSet.of(DialogOptions.RESIZABLE),
                appInfoService.getMainIcons().values());

        getWebEngine().setUserStyleSheetLocation(Main.class.getResource("Help.css").toExternalForm());
    }

    @Override
    protected ButtonsPane createButtonsPane(Stage dialogStage) {
        return null;
    }

}
