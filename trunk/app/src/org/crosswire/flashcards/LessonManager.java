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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.crosswire.common.util.CWClassLoader;

/**
 * The <code>LessonManager</code> provides the management of <code>LessonSet</code>s.
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class LessonManager
{
    public static LessonManager instance()
    {
        return INSTANCE;
    }

    private LessonManager()
    {
        lessonSets = new TreeSet();
        try
        {
            String path = System.getProperty("user.home") + File.separator + DIR_PROJECT; //$NON-NLS-1$
            URL home = new URL(FILE_PROTOCOL, null, path);
            CWClassLoader.setHome(home);
        }
        catch (MalformedURLException e1)
        {
            assert false;
        }
        load();
    }

    /**
     * @return the home directory url
     */
    public URL getHome()
    {
        return CWClassLoader.getHome();
    }

    /**
     * Appends the specified <code>Lesson</code> to the end of this list.
     *
     * @param flashCard to be appended to this list.
     */
    public void add(LessonSet aLessonSet)
    {
        lessonSets.add(aLessonSet);
    }

    /**
     * Load this lesson from persistent store named by the lesson's <code>LESSON_ROOT</code>.
     */
    public void load()
    {
        loadJarLessonSets();
        loadHomeLessonSets();
    }

    /**
     * Load lesson sets from the jar file
     */
    private void loadJarLessonSets()
    {

        // Dig into the jar for lessonSets
        URL lessonsURL = this.getClass().getResource('/' + LESSON_ROOT);
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
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements())
            {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                if (jarEntry.isDirectory())
                {
                    String entryName = jarEntry.getName();
                    // remove trailing '/'
                    entryName = entryName.substring(0, entryName.length() - 1);
                    if (entryName.startsWith(LESSON_ROOT) && ! entryName.equals(LESSON_ROOT))
                    {
                        // let the description be just the directory name and not the path
                        add(new LessonSet(entryName));
                    }
                }
            }
        }
    }

    /**
     * Load lesson sets from the "home" directory
     */
    private void loadHomeLessonSets()
    {
        try
        {
            URL dirURL = CWClassLoader.getHomeResource(LESSON_ROOT);
            File directory = new File(dirURL.getFile());
            File[] files = directory.listFiles();
            if (files == null)
            {
                return;
            }
            Arrays.sort(files);
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                if (file.isDirectory())
                {
                    // convert the path to one that is relative to the home and has forward slashes
                    String lessonPath = file.getPath();
                    // If it uses \ as a path separator then replace it w/ /
                    lessonPath = lessonPath.replace('\\', '/');
                    int offset = lessonPath.indexOf(LESSON_ROOT);
                    lessonPath = lessonPath.substring(offset, lessonPath.length());
                    // let the description be just the directory name and not the path
                    add(new LessonSet(lessonPath));
                }
            }
        }
        catch (Exception e)
        {
            // that's fine.  We just failed to load local files.
        }
    }
    
    /**
     * See if any LessonSet has changes that need to be saved
     */
    public boolean isModified()
    {
        Iterator iter = lessonSets.iterator();
        while (iter.hasNext())
        {
            LessonSet lessonSet = (LessonSet) iter.next();
            if (lessonSet.isModified())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Save all the modified lesson sets to persistent store named by the lesson's <code>LESSON_ROOT</code>.
     */
    public void store()
    {
        Iterator iter = lessonSets.iterator();
        while (iter.hasNext())
        {
            LessonSet lessonSet = (LessonSet) iter.next();
            if (lessonSet.isModified())
            {
                lessonSet.store();
            }
        }
    }

    public Iterator iterator()
    {
        return lessonSets.iterator();
    }

    public static final String LESSON_ROOT = "lessons";
    private static final String DIR_PROJECT = ".flashcards";
    private static final String FILE_PROTOCOL = "file";

    private static final LessonManager INSTANCE = new LessonManager();

    /**
     * An ordered list of <code>lessonSets</code>
     */
    private Set lessonSets;
}
