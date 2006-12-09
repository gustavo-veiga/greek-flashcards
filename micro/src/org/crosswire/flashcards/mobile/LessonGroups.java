package org.crosswire.flashcards.mobile;

import javax.microedition.lcdui.*;

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
public class LessonGroups extends Form implements CommandListener {
  public LessonGroups() {
    super("Choose a Lesson Group");
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
    addCommand(new Command("Exit", Command.EXIT, 1));
    addCommand(new Command("Select", Command.SCREEN, 2));
    lessonGroupChoice.setLabel("Lesson Groups");
    this.append(lessonGroupChoice);
  }

  void show() {
    loadLessonGroups();
    Display.getDisplay(FlashCards.instance).setCurrent(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    /** @todo Add command handling code */
    if (command.getCommandType() == Command.EXIT) {
      // stop the MIDlet
      FlashCards.quitApp();
    }
    else if (command.getCommandType() == Command.SCREEN) {
      if (command.getPriority() == 2) {
        FlashCards.instance.lessons.show();
      }
    }
  }

  ChoiceGroup lessonGroupChoice = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);

  public void loadLessonGroups() {
    lessonGroupChoice.deleteAll();
    lessonGroupChoice.append(FlashCards.instance.lessonSet.getDescription(), null);
  }
}
