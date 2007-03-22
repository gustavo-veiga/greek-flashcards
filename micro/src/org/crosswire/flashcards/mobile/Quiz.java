package org.crosswire.flashcards.mobile;

import javax.microedition.lcdui.*;
import org.crosswire.flashcards.Lesson;
import org.crosswire.flashcards.MicroLesson;
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

  ImageItem wordImage = new ImageItem("", null, ImageItem.LAYOUT_DEFAULT, "");
  ChoiceGroup answersDisplay = new ChoiceGroup("", ChoiceGroup.EXCLUSIVE);
  StringItem statusBar = new StringItem("", "");
  int maxWidth = 10000;
  int maxHeight = 10000;

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
    this.append(wordImage);
    this.append(answersDisplay);
    this.append(statusBar);
    answersDisplay.setLabel(null);
    try {
      // MIDP 2.0 only.  can we avoid this?
      answersDisplay.setFitPolicy(Choice.TEXT_WRAP_OFF);
    }
    catch (Exception e) {
      e.printStackTrace();
      maxWidth = 20;
    }
    //    answersDisplay.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP |
//                             Item.LAYOUT_VEXPAND);
    statusBar.setText("StatusBar");
//    wordImage.setLayout(Item.LAYOUT_LEFT | Item.LAYOUT_TOP |
//                        Item.LAYOUT_VSHRINK);
//    maxWidth =
    try {
      // MIDP 2.0 only.  can we avoid this?
      int thisHeight = this.getHeight();

      thisHeight -= 40; // subtract image height
      thisHeight -= 4; // subtract likely border
//      Font choiceFont = answersDisplay.getFont(0);
      // MIDP 2.0 only.  can we avoid this?
      Font choiceFont = statusBar.getFont();
      int fontHeight = choiceFont.getHeight();
      fontHeight += 1;  // likely buffer space between entries
      maxHeight = thisHeight / fontHeight;
      maxHeight -= 1; // space for status bar
    }
    catch (Exception e) {
      e.printStackTrace();
      maxHeight = 4;
    }
  }

  void show() {
    int lessonCount = FlashCards.instance.lessons.lessonChoice.size();
    quizer.clear();
    for (int i = 0; i < lessonCount; i++) {
      if (FlashCards.instance.lessons.lessonChoice.isSelected(i)) {
        String lessonName = FlashCards.instance.lessons.lessonChoice.getString(i);
        Lesson l = FlashCards.instance.lessonGroups.getLessonSet().getLesson(lessonName);
        quizer.loadLesson(l);
      }
    }
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
	if (right.length() > maxWidth) {
	  right = right.substring(0, maxWidth-3) + "...";
	}
        if (ans.equals(right)) {
          wordDisplay(wrongThisTime, "Correct");
        }
        else {
          setStatus("Try again");
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
    boolean hasImage = false;
    try {
	Image image = MicroLesson.getImage(currentWord);
	wordImage.setImage(image);
	wordImage.setLabel(null);
        hasImage = true;
    }
    catch (Exception e) { e.printStackTrace();}
    if (!hasImage) {
      wordImage.setImage(null);
      wordImage.setLabel(currentWord.getFront());
    }
    Vector answers = quizer.getRandomAnswers(maxHeight);
    while (answersDisplay.size() > 0) {
      answersDisplay.delete(0);
    }
    for (int i = 0; i < answers.size(); i++) {
      String a = (String) answers.elementAt(i);
      if (a.length() > maxWidth) {
        a = a.substring(0, maxWidth-3) + "...";
      }
      answersDisplay.append(a, null);
//      if (answersDisplay.get
    }
  }

  private void setStatus(String text) {
    statusBar.setText(text + "|" + Integer.toString(quizer.getNotLearnedCount()) + "|" + Integer.toString(quizer.getTotalAsked() - quizer.getTotalWrong()) + "/" + Integer.toString(quizer.getTotalAsked()) + "|"+quizer.getPercentage()+"%");
  }
}
