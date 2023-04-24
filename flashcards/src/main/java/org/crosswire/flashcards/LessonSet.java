/*
 * Distribution Licence:
 * FlashCard is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 * The License is available on the internet at:
 *     http://www.gnu.org/copyleft/gpl.html,
 * or by writing to:
 *     Free Software Foundation, Inc.
 *     59 Temple Place - Suite 330
 *     Boston, MA 02111-1307, USA
 *
 * Copyright: 2004 CrossWire Bible Society
 */
package org.crosswire.flashcards;

import java.util.Vector;

/**
 * A <code>LessonSet</code> is an ordered list of <code>Lesson</code>s.
 * The lessons are sorted by filename.
 * The lesson set also has a description which is useful for showing to a user
 * and a directory name where its Lessons are stored. This directory name is
 * expected to be a relative
 * path and will be stored either in a jar or in the user's FlashCard directory.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonSet {

    /**
     * The <code>url</code> of the lesson
     */
    private String url = null;

    /**
     * A <code>description</code> of the lesson to be displayed to the user.
     */
    private String description;

    /**
     * An ordered list of <code>lessons</code>
     */
    private Vector lessons = new Vector();

    /**
     * Flag indicating whether this lesson set has been <code>modified</code>
     */
    private boolean modified;

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -798022075988174038L;

    public LessonSet(String url) {
        this.url = url; // .toLowerCase();
        description = url.substring(url.lastIndexOf('/', url.length() - 2) + 1);
        if (description.endsWith("/")) {
            description = description.substring(0, description.length() - 1);
        }
        load();
    }

    /**
     * Get the relative path names of the lessons in this lesson set from
     * the jar file.
     * 
     * @param lessonSet
     */

    /**
     * Load this lesson set from persistent store named by the lesson set's
     * <code>dirname</code>.
     * This is the union of lessons in the Jar and in the user's flashcard home
     * directory.
     */
    protected void load() {
    }

    /**
     * Save this lesson to persistent store named by the lesson's
     * <code>dirname</code>.
     */
    public void store() {
        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = (Lesson) lessons.elementAt(i);
            if (lesson.isModified()) {
                lesson.store();
            }
        }
    }

    /**
     * @return whether the lesson set has been modified
     */
    public boolean isModified() {
        if (modified) {
            return true;
        }

        for (int i = 0; i < lessons.size(); i++) {
            Lesson lesson = (Lesson) lessons.elementAt(i);
            if (lesson.isModified()) {
                return true;
            }
        }
        return false;
    }

    public Vector getLessons() {
        return lessons;
    }

    /**
     * Adds the specified <code>Lesson</code> to this lesson set.
     *
     * @param flashCard to be added.
     */
    public void add(Lesson lesson) {
        modified = true;
        lessons.addElement(lesson);
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String newDescription) {
        if (newDescription != null && !newDescription.equals(description)) {
            modified = true;
            description = newDescription;
        }
    }

    /**
     * @return Returns the dirname.
     */
    public String getURL() {
        return url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        LessonSet lesson = (LessonSet) obj;
        return description.compareTo(lesson.description);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return description;
    }

    public void augment(LessonSet other) {
        for (int i = 0; i < other.getLessons().size(); i++) {
            Lesson l = (Lesson) other.getLessons().elementAt(i);
            Lesson exists = getLesson(l.getDescription());
            if (exists != null) {
                lessons.removeElement(exists);
            }
            lessons.addElement(l);
        }
    }

    public Lesson getLesson(String desc) {
        for (int i = 0; i < lessons.size(); i++) {
            Lesson ls = (Lesson) lessons.elementAt(i);
            if (desc.equals(ls.getDescription())) {
                return ls;
            }
        }
        return null;
    }
}
