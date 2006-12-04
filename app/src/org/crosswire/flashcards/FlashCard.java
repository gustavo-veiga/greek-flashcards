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

import java.io.Serializable;


/**
 * A FlashCard has a front and a back. The front has the test
 * and the back has the answer.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class FlashCard implements Cloneable, Comparable, Serializable
{
    /**
     * Create a partial FlashCard.
     * @param front
     */
    public FlashCard()
    {
        this("", "");
    }

    /**
     * Create a complete FlashCard
     * @param front
     * @param back
     */
    public FlashCard(String front, String back)
    {
        original = new FlashCardRep(front, back);

        try
        {
            copy = (FlashCardRep) original.clone();
        }
        catch (CloneNotSupportedException e)
        {
            assert false;
        }
    }

    public void setAudioURL(String url) {
         copy.setAudioURL(url);
    }

    public String getAudioURL() {
         return copy.getAudioURL();
    }


    /**
     * Get a particular side of this FlashCard.
     * This is useful to flip the cards.
     * @param front
     * @return the requested side
     */
    public String getSide(boolean frontside)
    {
        if (frontside)
        {
            return getFront();
        }
        return getBack();
    }

    /**
     * @return Returns the back.
     */
    public String getBack()
    {
        return copy.getBack();
    }

    /**
     * @param newBack The back to set.
     */
    public void setBack(String newBack)
    {
        copy.setBack(newBack);
    }

    /**
     * @return Returns the front.
     */
    public String getFront()
    {
        return copy.getFront();
    }

    /**
     * @param newFront The front to set.
     */
    public void setFront(String newFront)
    {
        copy.setFront(newFront);
    }

    /**
     * Method reset
     */
    public void reset()
    {
        copy.assign(original);
    }

    /**
     * Method isIncomplete
     * @return boolean
     */
    public boolean isIncomplete()
    {
        return copy.isIncomplete();
    }

    /**
     * Method setOriginal
     */
    public void setOriginal()
    {
        original.assign(copy);
    }

    /**
     * @return Returns whether this FlashCard has been modified.
     */
    protected boolean isModified()
    {
        return !original.equals(copy);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (!(obj instanceof FlashCard))
        {
            return false;
        }
        FlashCard otherCard = (FlashCard) obj;
        return copy.equals(otherCard.copy);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return copy.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return copy.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        FlashCard otherCard = (FlashCard) obj;
        return copy.compareTo(otherCard.copy);
    }

    private FlashCardRep original;
    private FlashCardRep copy;

    /**
     * Serialization ID
     */
    private static final long serialVersionUID = -4429061155097506281L;
}
