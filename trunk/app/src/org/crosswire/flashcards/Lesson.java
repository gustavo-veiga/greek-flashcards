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
 * The copyright to this program is held by it's authors
 * Copyright: 2004
 */
package org.crosswire.flashcards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.crosswire.common.CWClassLoader;
import org.crosswire.common.ResourceUtil;

/**
 * A Lesson is an ordered list of FlashCards.
 * The lesson also has a description which is useful for showing to a user.
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class Lesson implements Comparable
{
    /**
     * Construct a fully described, empty lesson.
     * @param filename
     * @param description
     */
    public Lesson(String filename, String description)
    {
        this.filename = filename;
        this.description = description;
        flashCards = new TreeSet();
        load();
    }

    /**
     * Appends the specified <code>FlashCard</code> to the end of this list.
     *
     * @param flashCard to be appended to this list.
     */
    public void add(FlashCard flashCard)
    {
        flashCards.add(flashCard);
    }

    /**
     * @return Returns the filename.
     */
    public String getFilename()
    {
        return filename;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param newDescription The description to set.
     */
    public void setDescription(String newDescription)
    {
        if (newDescription != null && !newDescription.equals(description))
        {
            modified = true;
            description = newDescription;
        }
    }

    /**
     * @return Returns the flashCards.
     */
    public Iterator iterator()
    {
        return flashCards.iterator();
    }

    /**
     * @return whether this lesson has been modified
     */
    public boolean isModified()
    {
        if (modified)
        {
            return true;
        }
        Iterator iter = flashCards.iterator();
        while (iter.hasNext())
        {
            FlashCard flashCard = (FlashCard) iter.next();
            if (flashCard.isModified())
            {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        Lesson lesson = (Lesson) obj;
        return description.compareTo(lesson.description);
    }

    /**
     * Load this lesson from persistent store named by the lesson's <code>filename</code>.
     */
    public void load()
    {
        Properties lesson = new Properties();
        try
        {
            URL lessonURL = ResourceUtil.getResource(filename);
            lesson.load(lessonURL.openConnection().getInputStream());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }

        int wordCount = Integer.parseInt(lesson.getProperty("wordCount"));
        for (int i = 0; i < wordCount; i++)
        {
            add(new FlashCard(lesson.getProperty("word" + i), lesson.getProperty("answers" + i)));
        }
    }

    /**
     * Save this lesson to persistent store named by the lesson's <code>filename</code>.
     */
    public void store()
    {
        Properties lesson = new Properties();
        try
        {
            lesson.setProperty("lessonTitle", description);
            Iterator iter = flashCards.iterator();
            int i = 0;
            while (iter.hasNext())
            {
                FlashCard flashCard = (FlashCard) iter.next();
                lesson.setProperty("word" + i, flashCard.getFront());
                lesson.setProperty("answers" + i, flashCard.getBack());
                i++;
            }
            lesson.setProperty("wordCount", Integer.toString(i));

            // Save it as a "home" resource.
            URL filePath = CWClassLoader.getHomeResource(filename);
            File file = new File(filePath.getFile());
            File dir = file.getParentFile();
            // Is it already a directory ?
            if (!dir.isDirectory())
            {
                dir.mkdirs();
            }
            lesson.store(new FileOutputStream(file), "Flash Lesson");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * The <code>filename</code> gives the relative location of the lesson.
     * Typically this is something like lesson/setname/lessonname.flash.
     */
    private String filename;

    /**
     * A <code>description</code> of the lesson to be displayed to the user.
     */
    private String description;

    /**
     * An ordered list of <code>flashCards</code>
     */
    private Set flashCards;

    /**
     * Flag indicating whether this lesson has been <code>modified</code>
     */
    private boolean modified;
}