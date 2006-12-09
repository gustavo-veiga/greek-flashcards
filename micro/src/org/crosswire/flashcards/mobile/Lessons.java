package org.crosswire.flashcards.mobile;

import javax.microedition.lcdui.*;
import java.util.Vector;
import org.crosswire.flashcards.Lesson;

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
public class Lessons extends Form implements CommandListener {

  ChoiceGroup lessonChoice = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);

  public Lessons() {
    super("Choose Lessons");
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    // Set up this Displayable to listen to command events
    setCommandListener(this);
    // add the Exit command
    addCommand(new Command("Back", Command.BACK, 1));
    addCommand(new Command("Start", Command.SCREEN, 2));
    lessonChoice.setLabel("Lesson Group Title");
    this.append(lessonChoice);
  }

  void show() {
    loadLessons();
    Display.getDisplay(FlashCards.instance).setCurrent(this);
  }


  public void commandAction(Command command, Displayable displayable) {
    /** @todo Add command handling code */
    if (command.getCommandType() == Command.BACK) {
      FlashCards.instance.lessonGroups.show();
    }
    else if (command.getCommandType() == Command.SCREEN) {
      if (command.getPriority() == 2) {
	FlashCards.instance.quiz.show();
      }
    }
  }

  public void loadLessons() {
    int lessonGroup = FlashCards.instance.lessonGroups.lessonGroupChoice.getSelectedIndex();
    String lessonGroupName = FlashCards.instance.lessonGroups.lessonGroupChoice.getString(lessonGroup);
    lessonChoice.setLabel(lessonGroupName);
    lessonChoice.deleteAll();
    Vector lessons = FlashCards.instance.lessonSet.getLessons();
    for (int i = 0; i < lessons.size(); i++) {
       Lesson l = (Lesson) lessons.elementAt(i);
      lessonChoice.append(l.getDescription(), null);
    }
  }

}
