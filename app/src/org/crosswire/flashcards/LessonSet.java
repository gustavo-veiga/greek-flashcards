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
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.crosswire.common.CWClassLoader;
import org.crosswire.common.ResourceUtil;

/**
 * A <code>LessonSet</code> is an ordered list of <code>Lesson</code>s.
 * The lessons are sorted by filename.
 * The lesson set also has a description which is useful for showing to a user
 * and a directory name where its Lessons are stored. This directory name is expected to be a relative
 * path and will be stored either in a jar or in the user's FlashCard directory.
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonSet implements Comparable
{
    public LessonSet(String dirname, String description)
    {
        this.dirname = dirname;
        this.description = description;
        lessons = new TreeSet();
        load();
    }

    /**
     * Adds the specified <code>Lesson</code> to this lesson set.
     *
     * @param flashCard to be added.
     */
    public void add(Lesson lesson)
    {
        modified = true;
        lessons.add(lesson);
    }

    /**
     * @return Returns the description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description The description to set.
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
     * @return Returns the dirname.
     */
    public String getDirname()
    {
        return dirname;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        LessonSet lesson = (LessonSet) obj;
        return description.compareTo(lesson.description);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return description;
    }
    /**
     * Load this lesson set from persistent store named by the lesson set's <code>dirname</code>.
     * This is the union of lessons in the Jar and in the user's flashcard home directory.
     */
    public void load()
    {
        loadJarLessons();
        loadHomeLessons();
    }

    /**
     * Get the relative path names of the lessons in this lesson set from
     * the jar file.
     * @param lessonSet
     */
    private void loadJarLessons()
    {

        // Dig into the jar for lessons
        URL lessonsURL = this.getClass().getResource('/' + dirname);
        if (lessonsURL == null)
        {
            return;
        }
        URLConnection connection = null;
        try
        {
            connection = lessonsURL.openConnection();
        }
        catch (Exception e1)
        {
            assert false;
        }
        if (connection instanceof JarURLConnection)
        {
            JarURLConnection jarConnection = (JarURLConnection) connection;
            JarFile jarFile = null;
            try
            {
                jarFile = jarConnection.getJarFile();
            }
            catch (IOException e2)
            {
                assert false;
            }
            if (jarFile == null)
            {
                return;
            }
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                String lessonPath = jarEntry.getName();
                if (lessonPath.startsWith(dirname) && ! jarEntry.isDirectory())
                {
                    String lessonDescription = getLessonDescription(lessonPath);
                    add(new Lesson(lessonPath, lessonDescription));
                }
            }
        }
    }

    /**
     * Get the relative path names of the lessons in this lesson set from
     * the user's program home.
     * @param lessonSet
     */
    private void loadHomeLessons()
    {
        try
        {
            URL dirURL = CWClassLoader.getHomeResource(dirname);
            File directory = new File(dirURL.getFile());
            File[] files = directory.listFiles();
            if (files == null)
            {
                return;
            }
            Arrays.sort(files);
            for (int i = 0; i < files.length; i++)
            {
                // convert the path to one that is relative to the home and has forward slashes
                File file = files[i];
                String lessonPath = file.getPath();
                // If it uses \ as a path separator then replace it w/ /
                lessonPath = lessonPath.replace('\\', '/');
                int offset = lessonPath.indexOf(dirname);
                lessonPath = lessonPath.substring(offset, lessonPath.length());
                String lessonDescription = getLessonDescription(lessonPath);
                add(new Lesson(lessonPath, lessonDescription));
            }
        }
        catch (Exception e)
        {
            // that's fine.  We just failed to load local files.
        }
    }
    
    /**
     * Get the description of the lesson
     * @param lessonpath the relative path to the lesson
     * @return the description of the lesson
     */
    private String getLessonDescription(String lessonpath)
    {
        URL lessonURL = ResourceUtil.getResource(lessonpath);
        Properties p = new Properties();
        try
        {
            p.load(lessonURL.openConnection().getInputStream());
        }
        catch (IOException ex)
        {
            assert false;
        }
        return p.getProperty("lessonTitle");
    }

    /**
     * Save this lesson to persistent store named by the lesson's <code>dirname</code>.
     */
    public void store()
    {
        Iterator iter = lessons.iterator();
        while (iter.hasNext())
        {
            Lesson lesson = (Lesson) iter.next();
            if (lesson.isModified())
            {
                lesson.store();
            }
        }
    }

    /**
     * @return whether the lesson set has been modified
     */
    public boolean isModified()
    {
        if (modified)
        {
            return true;
        }

        Iterator iter = lessons.iterator();
        while (iter.hasNext())
        {
            Lesson lesson = (Lesson) iter.next();
            if (lesson.isModified())
            {
                return true;
            }
        }
        return false;
    }
    
    public Iterator iterator()
    {
        return lessons.iterator();
    }

    /**
     * The <code>dirname</code> of the lesson
     */
    private String dirname;

    /**
     * A <code>description</code> of the lesson to be displayed to the user.
     */
    private String description;

    /**
     * An ordered list of <code>lessons</code>
     */
    private Set lessons;

    /**
     * Flag indicating whether this lesson set has been <code>modified</code>
     */
    private boolean modified;
}