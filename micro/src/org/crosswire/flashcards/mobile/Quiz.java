package org.crosswire.flashcards.mobile;

import javax.microedition.lcdui.*;
import org.crosswire.flashcards.Lesson;
import org.crosswire.flashcards.Quizer;
import org.crosswire.flashcards.FlashCard;
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
public class Quiz extends Form implements CommandListener {

  StringItem wordDisplay = new StringItem("", "");
  ChoiceGroup answersDisplay = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);
  StringItem statusBar = new StringItem("", "");
  Spacer spacer1 = new Spacer(0, 0);
  Spacer spacer2 = new Spacer(0, 0);
  Quizer quizer = new Quizer();
  FlashCard currentWord = null;
  int wrongThisTime = 0;

  public Quiz() {
    super("Quiz");
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
    addCommand(new Command("End", Command.BACK, 1));
    addCommand(new Command("Answer", Command.SCREEN, 2));
    wordDisplay.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_TOP |
                          Item.LAYOUT_NEWLINE_BEFORE |
                          Item.LAYOUT_NEWLINE_AFTER);
    wordDisplay.setText("Word");
    this.append(spacer1);
    this.append(wordDisplay);
    this.append(spacer2);
    this.append(answersDisplay);
    this.append(statusBar);
    answersDisplay.setLabel("Answers");
    answersDisplay.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_VCENTER |
                             Item.LAYOUT_VEXPAND);
    statusBar.setLayout(Item.LAYOUT_BOTTOM | Item.LAYOUT_VEXPAND |
                        Item.LAYOUT_NEWLINE_BEFORE);
    statusBar.setText("StatusBar");
//    spacer1.setPreferredSize();
    spacer1.setMinimumSize(2, 2);
  }

  void show() {
    int lessonNum = FlashCards.instance.lessons.lessonChoice.getSelectedIndex();
    String lessonName = FlashCards.instance.lessons.lessonChoice.getString(lessonNum);
    Lesson l = FlashCards.instance.lessonSet.getLesson(lessonName);
    quizer.clear();
    quizer.loadLesson(l);
    wordDisplay(-1, "Begin");
    Display.getDisplay(FlashCards.instance).setCurrent(this);
  }

  public void commandAction(Command command, Displayable displayable) {
    if (command.getCommandType() == Command.BACK) {
	Display.getDisplay(FlashCards.instance).setCurrent(FlashCards.instance.lessons);
    }
    else if (command.getCommandType() == Command.SCREEN) {
      if (command.getPriority() == 2) {
        // show answer
        // for now let's use this as our answer picker button
        int a = answersDisplay.getSelectedIndex();
        String ans = answersDisplay.getString(a);
        String right = currentWord.getBack();
	if (right.length() > 23) {
	  right = right.substring(0, 21) + "...";
	}
        if (ans.equals(right)) {
          wordDisplay(wrongThisTime, "Correct");
        }
        else {
          setStatus("Wrong. Try again.");
          wrongThisTime++;
        }
      }
    }
  }


  public void wordDisplay(int wrong, String status) {
    currentWord = quizer.getRandomWord(wrong);
    // we do this here cuz we first need to update stats from line above this
    wrongThisTime = 0;
    if (status != null) setStatus(status);
    if (currentWord == null) {
      setStatus("Great Job!");
      return;
    }
    wordDisplay.setText(currentWord.getFront());
    Vector answers = quizer.getRandomAnswers(4);
    answersDisplay.deleteAll();
    for (int i = 0; i < answers.size(); i++) {
      String a = (String) answers.elementAt(i);
      if (a.length() > 23) {
        a = a.substring(0, 21) + "...";
      }
      answersDisplay.append(a, null);
    }
  }

  private void setStatus(String text) {
    statusBar.setText(text + " | " + Integer.toString(quizer.getNotLearnedCount()) + " | " + Integer.toString(quizer.getTotalAsked() - quizer.getTotalWrong()) + "/" + Integer.toString(quizer.getTotalAsked()) + "("+quizer.getPercentage()+"%)");
  }
}
