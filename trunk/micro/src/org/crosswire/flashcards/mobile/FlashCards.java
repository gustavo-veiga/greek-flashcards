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
  public boolean loaded = false;

  public Vector lessonSets = new Vector();
  public FlashCards() {
    instance = this;
  }

  public void startApp() {
    lessonGroups.show();
    if (!loaded) {
      loadLessons();
    }
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
  public void loadLessons() {
    Properties l = new Properties();
    try {
      l.load("/lessons/lessons.properties");
    }
    catch (Exception e) {}
    if (l != null) {
      for (int i = 0; true; i++) {
        String ld = l.getProperty("LessonSet" + Integer.toString(i));
        if (ld != null) {
          String desc = l.getProperty("LessonDescription" + Integer.toString(i));
          lessonGroups.lessonGroupChoice.setLabel("Loading ["+desc+"]...");
          LessonSet ls = new MicroLessonSet("/lessons/" + ld);
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
    loaded = true;
    lessonGroups.lessonGroupChoice.setLabel("Lesson Groups");
    lessonGroups.loadLessonGroups();
  }
}
