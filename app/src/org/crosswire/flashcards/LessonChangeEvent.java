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

import java.util.EventObject;

/**
 * A LessonChangeEvent indicates that a lesson has changed and
 * needs to be saved.
 * 
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class LessonChangeEvent extends EventObject
{

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 6186012752083856266L;

    /**
     * @param source
     */
    public LessonChangeEvent(Object source)
    {
        super(source);
    }


}
