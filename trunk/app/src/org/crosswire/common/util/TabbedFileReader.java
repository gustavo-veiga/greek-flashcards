package org.crosswire.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.crosswire.common.util.ResourceUtil;

/**
 * A TabbedFileReader reads a file consisting of lines with
 * tab separated columns.
 * 
 * <p><table border='1' cellPadding='3' cellSpacing='0'>
 * <tr><td bgColor='white' class='TableRowColor'><font size='-7'>
 *
 * Distribution Licence:<br />
 * JSword is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License,
 * version 2 as published by the Free Software Foundation.<br />
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.<br />
 * The License is available on the internet
 * <a href='http://www.gnu.org/copyleft/gpl.html'>here</a>, or by writing to:
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA<br />
 * The copyright to this program is held by it's authors.
 * </font></td></tr></table>
 * @see gnu.gpl.Licence
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 * @version $Id$
 */
public class TabbedFileReader
{
    /**
     * Process all the lines in the file.
     * @param fileName java.lang.String
     * @param columns int
     * @param lp lineProcessor
     * @throws IOException
     */
    static public void read(String fileName, int columns, RowProcessor lp) throws IOException
    {
        InputStream inputStream = null;
        try
        {
            URL fileURL = ResourceUtil.getResource(fileName);
            inputStream = new FileInputStream(fileURL.getFile());
        }
        catch (Exception ex1)
        {
            inputStream = new FileInputStream(fileName);
        }
 
        // open the file
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        Object row[] = new Object[columns];

        // read the file a line at a time and send it to the
        // processor for processing
        String line = null;
        while ((line = in.readLine()) != null)
        {
            // Split it on tabs
            int previousLoc = 0;
            int lastColumn = columns - 1;
            for (int col = 0; col < lastColumn; col++)
            {
                int loc = line.indexOf('\t', previousLoc);
                if (loc == -1)
                {
                    throw new ArrayIndexOutOfBoundsException();
                }
                 row[col] = line.substring(previousLoc, loc);
                previousLoc = (loc + 1);
            }
            row[lastColumn] = line.substring(previousLoc);
            lp.process(row);
        }
        // close the file
        in.close();
    }

}
