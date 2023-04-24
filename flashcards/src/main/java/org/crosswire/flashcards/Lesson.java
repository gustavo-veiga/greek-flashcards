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
 *
 * Copyright: 2004 CrossWire Bible Society
 */

package org.crosswire.flashcards;

import java.util.Vector;

/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class Lesson {

    /**
     * The <code>filename</code> gives the relative location of the lesson.
     * Typically this is something like lesson/setname/lessonname.flash.
     */
    private String url;

    /**
     * A <code>description</code> of the lesson to be displayed to the user.
     */
    private String description;

    /**
     * A path to the <code>font</code> to be used by the lesson.
     */
    private String font;

    /**
     * An ordered list of <code>flashCards</code>
     */
    private Vector flashCards = new Vector();

    private boolean modified = false;

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -4031174832238749375L;

    /**
     * Construct a new, empty lesson.
     */
    public Lesson() throws Exception {
        this("NewLesson.flash", "New Lesson");
    }

    /**
     * Construct a lesson from URL.
     * 
     * @param url
     */
    public Lesson(String url) throws Exception {
        this(url, null);
    }

    /**
     * Construct a lesson and assign a description
     * 
     * @param url
     * @param description
     */
    public Lesson(String url, String description) throws Exception {
        this.url = url;
        load();

        if (description != null) {
            this.description = description;
        }
    }

    /**
     * Load this lesson from persistent store named by the lesson's
     * <code>filename</code>.
     */
    protected void load() throws Exception {
    }

    /**
     * Save this lesson to persistent store named by the lesson's
     * <code>filename</code>.
     */
    protected void store() {
    }

    /**
     * Adds the specified <code>FlashCard</code> to this Lesson.
     *
     * @param flashCard to be added.
     */
    public void add(FlashCard flashCard) {
        flashCards.addElement(flashCard);
        modified = true;
    }

    /**
     * Removes the specified <code>FlashCard</code> from the lesson.
     *
     * @param flashCard to be removed.
     */
    public void remove(FlashCard flashCard) {
        flashCards.removeElement(flashCard);
        modified = true;
    }

    /**
     * @param flashCard
     * @return
     */
    public boolean contains(FlashCard flashCard) {
        return flashCards.contains(flashCard);
    }

    /**
     * @return Returns the filename.
     */
    public String getURL() {
        return url;
    }

    /**
     * @param filename The filename to set.
     */
    public void setURL(String url) {
        this.url = url;
        modified = true;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param newDescription The description to set.
     */
    public void setDescription(String newDescription) {
        description = newDescription;
        modified = true;
    }

    /**
     * @return Returns the font.
     */
    public String getFont() {
        return font;
    }

    /**
     * @param newFont The font to set.
     */
    public void setFont(String newFont) {
        font = newFont;
        modified = true;
    }

    /**
     * @return Returns the flashCards.
     */
    public Vector getFlashcards() {
        return flashCards;
    }

    public void setModified(boolean mod) {
        modified = mod;
    }

    /**
     * @return whether this lesson has been modified
     */
    public boolean isModified() {
        if (modified) {
            return true;
        }

        for (int i = 0; i < flashCards.size(); i++) {
            FlashCard flashCard = (FlashCard) flashCards.elementAt(i);
            if (flashCard.isModified()) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        Lesson lesson = (Lesson) obj;
        return url.compareTo(lesson.url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return description;
    }

}
