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


/**
 * A FlashCard has a front and a back. The front has the test
 * and the back has the answer.
 * 
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class FlashCard implements Cloneable, Comparable
{
    /**
     * Create a partial FlashCard.
     * @param front
     */
    public FlashCard(String front)
    {
        this(front, "");
    }

    /**
     * Create a complete FlashCard
     * @param front
     * @param back
     */
    public FlashCard(String front, String back)
    {
        this.front = front;
        this.back = back;
        modified = true;
    }

    /**
     * @return Returns the back.
     */
    public String getBack()
    {
        return back;
    }

    /**
     * @param newBack The back to set.
     */
    public void setBack(String newBack)
    {
        if (newBack != null && !newBack.equals(back))
        {
            modified = true;
            back = newBack;
        }
    }

    /**
     * @return Returns the front.
     */
    public String getFront()
    {
        return front;
    }

    /**
     * @param newFront The front to set.
     */
    public void setFront(String newFront)
    {
        if (newFront != null && !newFront.equals(this.front))
        {
            modified = true;
        	front = newFront;
        }
    }

    /**
     * @return Returns whether this FlashCard has been modified.
     */
    protected boolean isModified()
    {
        return modified;
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
            return true;
        if (!(obj instanceof FlashCard))
            return false;
        FlashCard otherCard = (FlashCard) obj;
        return front.equals(otherCard.front)
        	&& back.equals(otherCard.back);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        int hashCode = 31 + front.hashCode();
        hashCode = 31 * hashCode + back.hashCode();
        return hashCode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return front + " " + back;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj)
    {
        FlashCard otherCard = (FlashCard) obj;
        int result = front.compareTo(otherCard.front);
        if (result == 0)
        {
            result = back.compareTo(otherCard.back);
        }
        return result;
    }

    private String front;
    private String back;
    private transient boolean modified;
}