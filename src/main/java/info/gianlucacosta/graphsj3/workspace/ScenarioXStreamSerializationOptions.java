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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import info.gianlucacosta.helios.serialization.xml.XStreamSerializationOptions;

/**
 * Describes the XStream-related settings, employed when loading and saving
 * documents
 */
class ScenarioXStreamSerializationOptions implements XStreamSerializationOptions {

    @Override
    public boolean isCompressed() {
        return false;
    }

    @Override
    public XStream createXStream() {
        XStream xStream = new XStream(new StaxDriver());

        xStream.aliasPackage("helios", "info.gianlucacosta.helios");
        xStream.aliasPackage("arcontes", "info.gianlucacosta.arcontes");
        xStream.aliasPackage("gj3", "info.gianlucacosta.graphsj3");

        return xStream;
    }

}
