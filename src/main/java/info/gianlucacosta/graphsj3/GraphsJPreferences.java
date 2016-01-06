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

import info.gianlucacosta.helios.collections.queues.SlidingQueue;
import info.gianlucacosta.helios.preferences.ObjectPreferences;
import info.gianlucacosta.helios.recentfiles.RecentFilesPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.prefs.Preferences;

/**
 * GraphsJ's preferences
 */
public class GraphsJPreferences extends ObjectPreferences implements RecentFilesPreferences {

    private static final Logger logger = LoggerFactory.getLogger(GraphsJPreferences.class);

    private static final String UNDO_STACK_MAX_SIZE_KEY = "MaxUndoStackSize";
    private static final int UNDO_STACK_DEFAULT_MAX_SIZE = 1200;

    private static final String RECENT_FILES_MAX_SIZE_KEY = "RecentFilesMaxSize";
    private static final int RECENT_FILES_DEFAULT_MAX_SIZE = 6;

    private static final String RECENT_FILES_KEY = "RecentFiles";

    public GraphsJPreferences(Preferences decoratedPreferences) {
        super(decoratedPreferences);
    }

    public int getUndoStackMaxSize() {
        return getInt(UNDO_STACK_MAX_SIZE_KEY, UNDO_STACK_DEFAULT_MAX_SIZE);
    }

    public void setUndoStackMaxSize(int undoStackMaxSize) {
        putInt(UNDO_STACK_MAX_SIZE_KEY, undoStackMaxSize);
    }

    @Override
    public SlidingQueue<File> getRecentFiles() {
        try {
            return getObject(RECENT_FILES_KEY);
        } catch (Exception ex) {
            logger.warn("Could not deserialize the recent files list:\n%s", ex);
            remove(RECENT_FILES_KEY);
            return null;
        }
    }

    @Override
    public void setRecentFiles(Collection<File> recentFiles) {
        try {
            putObject(RECENT_FILES_KEY, recentFiles);
        } catch (Exception ex) {
            logger.warn("Could not serialize the recent files list:\n%s", ex);
        }
    }

    @Override
    public int getRecentFilesMaxSize() {
        return getInt(RECENT_FILES_MAX_SIZE_KEY, RECENT_FILES_DEFAULT_MAX_SIZE);
    }

    @Override
    public void setRecentFilesMaxSize(int maxSize) {
        putInt(RECENT_FILES_MAX_SIZE_KEY, maxSize);
    }
}
