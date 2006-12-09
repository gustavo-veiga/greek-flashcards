package org.crosswire.flashcards.mobile;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import org.crosswire.flashcards.*;
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
  public LessonSet lessonSet = new MicroLessonSet("lessons");
  public FlashCards() {
    instance = this;
    lessonSet.setDescription("Greek Top Freq");
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
