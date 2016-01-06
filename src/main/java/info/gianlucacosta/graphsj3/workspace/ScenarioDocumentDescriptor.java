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

import java.io.File;

/**
 * Small header deserialized before the document itself
 * <p>
 * It provides important information that <strong>must</strong> be available
 * when loading the body of the document, such as:
 * <ul>
 * <li>the document version</li>
 * <li>
 * the <i>reference jar file</i> for the scenario, required to
 * correctly load external classes (that is, not belonging to GraphsJ's internal
 * library)
 * </li>
 * </ul>
 */
class ScenarioDocumentDescriptor {

    private final int documentVersion;
    private final File referenceJarFile;

    public ScenarioDocumentDescriptor(int documentVersion, File referenceJarFile) {
        this.documentVersion = documentVersion;
        this.referenceJarFile = referenceJarFile;
    }

    public int getDocumentVersion() {
        return documentVersion;
    }

    public File getReferenceJarFile() {
        return referenceJarFile;
    }

}
