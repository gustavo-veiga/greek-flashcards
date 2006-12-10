package org.crosswire.flashcards.mobile;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import org.crosswire.flashcards.*;
import java.util.Vector;
/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FlashCards extends MIDlet {
  static FlashCards instance;
  public LessonGroups lessonGroups = new LessonGroups();
  public Lessons lessons = new Lessons();
  public Quiz quiz = new Quiz();

  public Vector lessonSets = new Vector();
  public FlashCards() {
    instance = this;
    Properties l = new Properties();
    try {
      l.load("lessons.properties");
    }
    catch (Exception e) {}
    if (l != null) {
      for (int i = 0; true; i++) {
        String ld = l.getProperty("LessonSet" + Integer.toString(i));
        if (ld != null) {
          LessonSet ls = new MicroLessonSet(ld);
          String desc = l.getProperty("LessonDescription" + Integer.toString(i));
          ls.setDescription( (desc != null) ? desc : ld);
          lessonSets.addElement(ls);
        }
        else
          break;
      }
    }
    if (lessonSets.size() < 1) {
      LessonSet ls = new MicroLessonSet("lessons");
      ls.setDescription("All Lessons");
      lessonSets.addElement(ls);
    }
  }

  public void startApp() {
    lessonGroups.show();
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
  }

  public static void quitApp() {
    instance.destroyApp(true);
    instance.notifyDestroyed();
    instance = null;
  }

}
