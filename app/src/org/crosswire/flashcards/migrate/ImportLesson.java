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
package org.crosswire.flashcards.migrate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.crosswire.common.util.RowProcessor;
import org.crosswire.common.util.TabbedFileReader;
import org.crosswire.flashcards.FlashCard;

import com.ibm.icu.text.Normalizer;

/**
 * A quick and dirty importer that will need to be modified for general use
 * or any other specific use.
 * 
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class ImportLesson extends JWindow
{
    public ImportLesson()
    {
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FileFilter()
        {
            public boolean accept(File f)
            {
                if (f.isDirectory())
                {
                    return true;
                }
                if (f.getName().endsWith(".txt"))
                {
                    return true;
                }
                return false;
            }

            public String getDescription()
            {
                return "Tab Separated Files";
            }
        });
        dialog.setCurrentDirectory(new File("./"));
        if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            fileToImport = dialog.getSelectedFile();
            System.err.println(fileToImport.getPath());
        }
        final List list = new ArrayList(1000);
        try
        {
            TabbedFileReader.read(fileToImport.getPath(), 2, new RowProcessor()
            {
                int i = 0;
                public void process(Object[] row)
                {
                    String word = Normalizer.decompose((String) row[1],true);
                    list.add(new FlashCard(word, (String) row[0]));
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        int SLICE = 100;
        int listSize = list.size();
        for (int i = 0; i < listSize; i += SLICE)
        {
            Properties props = new Properties();
            for (int j = 0; j < SLICE && i + j < listSize; j++)
            {
                FlashCard fc = (FlashCard) list.get(i + j);
                props.setProperty("word" + j, fc.getFront());
                props.setProperty("answers" + j, fc.getBack());
            }
            int count = props.size() / 2;
            props.setProperty("wordCount", Integer.toString(count));
            props.setProperty("lessonTitle", "Mounce's Most Frequent Words, part " + ((int) i/SLICE + 1));
            try
            {
                props.store(new FileOutputStream("lessons/Mounce/mounce" + ((int) i/SLICE + 1) + ".flash"), "Flash Lesson");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        // Set the "Look And Feel"

        try
        {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        }
        catch (Exception e)
        {

            e.printStackTrace();

        }
        JWindow win = new ImportLesson();
        win.setSize(1, 1);
        win.setVisible(true);
    }

    private File fileToImport;
}
