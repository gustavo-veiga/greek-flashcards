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
 * Copyright 2006: CrossWire Bible Society
 */
package org.crosswire.flashcards;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A FlashCard has a front and a back. The front has the test
 * and the back has the answer.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [ dmsmith555 at yahoo dot com]
 */
public class FlashCard {
  private Hashtable original = new Hashtable();
  private Hashtable values = new Hashtable();
  private final String front = "front";
  private final String back = "back";
  private final String audioURL = "audioURL";
  private final String imageURL = "imageURL";

  /**
   * Create a partial FlashCard.
   * @param front
   */
  public FlashCard() {
    this("", "");
  }

  /**
   * Create a complete FlashCard
   * @param front
   * @param back
   */
  public FlashCard(String frontValue, String backValue) {
    original.put(front, frontValue);
    original.put(back, backValue);
    reset();
  }

  /**
   * Get a particular side of this FlashCard.
   * This is useful to flip the cards.
   * @param front
   * @return the requested side
   */
  public String getSide(boolean frontside) {
    if (frontside) {
      return getFront();
    }
    return getBack();
  }

  /**
   * @return Returns the back.
   */
  public String getBack() {
    return (String)values.get(back);
  }

  /**
   * @param newBack The back to set.
   */
  public void setBack(String newBack) {
    values.put(back, newBack);
  }

  /**
   * @return Returns the front.
   */
  public String getFront() {
    return (String)values.get(front);
  }

  /**
   * @param newFront The front to set.
   */
  public void setFront(String newFront) {
    values.put(front, newFront);
  }

  public String getAudioURL() {
    return (String) values.get(audioURL);
  }

  public void setAudioURL(String newAudioURL) {
    values.put(audioURL, newAudioURL);
  }

  public String getImageURL() {
    return (String) values.get(imageURL);
  }

  public void setImageURL(String newImageURL) {
    values.put(imageURL, newImageURL);
  }

  /**
   * Method reset
   */
  public void reset() {
    hashCopy(values, original);
  }

  private static void hashCopy(Hashtable dest, Hashtable src) {
    dest.clear();
    for (Enumeration k = src.keys(); k.hasMoreElements();) {
      String key = (String)k.nextElement();
      dest.put(key, src.get(key));
    }
  }

  /**
   * Method isIncomplete
   * @return boolean
   */
  public boolean isIncomplete() {
    return (values.get(front) != null) && (values.get(back) != null);
  }

  /**
   * Method setOriginal
   */
  public void setOriginal() {
    hashCopy(original, values);
  }

  /**
   * @return Returns whether this FlashCard has been modified.
   */
  protected boolean isModified() {
    return !values.equals(original);
  }

  public Object clone() {
    FlashCard n = new FlashCard();
    hashCopy(n.original, original);
    hashCopy(n.values, values);
    return n;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (! (obj instanceof FlashCard)) {
      return false;
    }
    FlashCard otherCard = (FlashCard) obj;
    return values.equals(otherCard.values);
  }

  public int compareTo(Object other) {
      return getFront().compareTo(((FlashCard)other).getFront());
  }
}
