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

import org.crosswire.common.swing.RowColumns;

/**
 * Defines the prototypes needed to display a FlashCard in a RowTable.
 * 
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class FlashCardColumns extends RowColumns
{
    /**
     * Field HEADERS
     * The names of the table column headers.
     */
    private static final String[] HEADERS =
    {
        "Front", "Back" //$NON-NLS-1$ //$NON-NLS-2$
    };

    /**
     * Field HEADER_TOOLTIPS
     * The tooltips for the table column headers.
     */
    private static final String[] HEADER_TOOLTIPS =
    {
       "Front of the FlashCard", "Back of the FlashCard"  //$NON-NLS-1$ //$NON-NLS-2$
    };

    /**
     * Field CHARACTER_WIDTHS
     * The widths of each column in Standard Characters.
     */
    private static final int[] CHARACTER_WIDTHS =
    {
        16, 32
    };

    /**
     * Field FIXED_WIDTHS
     * The columns that cannot be resized are true.
     * The columns that can be resized are false.
     */
    private static final boolean[] FIXED_WIDTHS =
    {
        false, false
    };

    /**
     * Field CLASSES
     * Gives object type of contained in each of the column.
     */
    private static final Class[] CLASSES =
    {
        String.class, String.class
    };

    /**
     * Field SORT_KEYS
     * The numerical index (0 based) of the columns that
     * participate in default sorting and column
     * sorting.
     */
    private static final int[] SORT_KEYS =
    {
        0
    };

    /**
     * Field TABLE_NAME
     * The Title of the table displayed in a titled border.
     */
    private static final String TABLE_NAME = "FlashCards: "; //$NON-NLS-1$

    /* (non-Javadoc)
     * @see FlashCardColumns#getHeaders()
     */
    public String[] getHeaders()
    {
        return FlashCardColumns.HEADERS;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getHeaderToolTips()
     */
    public String[] getHeaderToolTips()
    {
        return FlashCardColumns.HEADER_TOOLTIPS;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getCharacterWidths()
     */
    public int[] getCharacterWidths()
    {
        return FlashCardColumns.CHARACTER_WIDTHS;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getFixedWidths()
     */
    public boolean[] getFixedWidths()
    {
        return FlashCardColumns.FIXED_WIDTHS;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getClasses()
     */
    public Class[] getClasses()
    {
        return FlashCardColumns.CLASSES;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getSortKeys()
     */
    public int[] getSortKeys()
    {
        return FlashCardColumns.SORT_KEYS;
    }

     /* (non-Javadoc)
     * @see RowColumns#getValueAt(java.lang.Object, int)
     */
    public Object getValueAt(Object row, int columnIndex)
    {
        FlashCard flashCard = (FlashCard) row;
        if (flashCard != null)
        {
            return flashCard.getSide(columnIndex == 0);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see FlashCardColumns#getTableName()
     */
    public String getTableName()
    {
        return FlashCardColumns.TABLE_NAME;
    }
}