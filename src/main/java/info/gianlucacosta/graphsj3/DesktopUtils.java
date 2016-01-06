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

package info.gianlucacosta.graphsj3;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DesktopUtils {
    private DesktopUtils() {
    }

    public static void openBrowser(String url) {
        Thread browserThread = new Thread(() -> {
            Desktop desktop = Desktop.getDesktop();

            if (desktop == null) {
                throw new UnsupportedOperationException();
            }

            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                throw new UnsupportedOperationException(ex);
            }
        });

        browserThread.start();
    }
}
