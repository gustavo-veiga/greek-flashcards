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
public class Quiz extends Canvas implements CommandListener {

  ImageItem wordImage = new ImageItem("", null, ImageItem.LAYOUT_DEFAULT, "");
  Vector answers = new Vector();
  Image image = null;
  boolean showAnswer = false;
  String statusBar = "";

  Quizer quizer = new Quizer();
  FlashCard currentWord = null;
  int wrongThisTime = 0;

  public Quiz() {
    super();
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setCommandListener(this);
    addCommand(new Command("Help", Command.HELP, 1));
    addCommand(new Command("Quit", Command.BACK, 2));
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
    if ("Quit".equals(command.getLabel())) {
      Display.getDisplay(FlashCards.instance).setCurrent(FlashCards.instance.
          lessons);
    }
    else if ("Help".equals(command.getLabel())) {
      if (showAnswer) {
	wordDisplay(-1, "Begin");
      }
      else {
        showAnswer = true;
        repaint();
      }
    }
  }


  public void wordDisplay(int wrong, String status) {
    showAnswer = false;
    currentWord = quizer.getRandomWord(wrong);
    image = null;
    answers.removeAllElements();
    // we do this here cuz we first need to update stats from line above this
    wrongThisTime = 0;
    if (status != null)
      setStatus(status);
    if (currentWord == null) {
      setStatus("Score");
      return;
    }
    // default to something sane
    int wordHeight = 20;
    try {
      image = MicroLesson.getImage(currentWord);
    }
    catch (Exception e) {
    }
    repaint();
  }

  private void setStatus(String text) {
    statusBar = text + "|" + Integer.toString(quizer.getNotLearnedCount()) + "|" + Integer.toString(quizer.getTotalAsked() - quizer.getTotalWrong()) + "/" + Integer.toString(quizer.getTotalAsked()) + "|"+quizer.getPercentage()+"%";
    this.repaint();
  }

  protected void keyPressed(int keyCode) {
    if ((currentWord != null) && (keyCode >= this.KEY_NUM1) && (keyCode <= this.KEY_NUM9)) {
      int a = keyCode - this.KEY_NUM1;
      String ans = (String)answers.elementAt(a);
      String right = currentWord.getBack();
      /*
      if (right.length() > maxWidth) {
	right = right.substring(0, maxWidth-3) + "...";
      }
      */
      if (ans.equals(right)) {
	wordDisplay(wrongThisTime, "Correct");
      }
      else {
	setStatus("Try again");
	wrongThisTime++;
      }
    }
    else {
      super.keyPressed(keyCode);
    }
    repaint();
  }

  public void paint (Graphics g) {


    Font gf = g.getFont();
    Font normFont = Font.getFont(gf.getFace(), Font.STYLE_PLAIN, gf.getSize());
    Font boldFont = Font.getFont(gf.getFace(), Font.STYLE_BOLD, gf.getSize());
    Font largeFont = Font.getFont(gf.getFace(), Font.STYLE_BOLD, Font.SIZE_LARGE);

    int spacer = 2;
    int rowHeight = boldFont.getHeight() + spacer;
    int y = 0;

    g.setGrayScale(255);
    g.fillRect (0, 0, getWidth (), getHeight ());
    g.setColor(0x0);

    if (currentWord == null) {
      String str = "Great Job!!!";
      g.setFont(largeFont);
      g.drawString(str, (getWidth() / 2) - (largeFont.stringWidth(str) / 2), (getHeight() / 2), Graphics.TOP|Graphics.LEFT);
    }
    else {

      // draw the word image
      if (image != null) {
        g.drawImage(image, 0, 0, Graphics.TOP | Graphics.LEFT);
        y = image.getHeight() + spacer;
      }
      else {
        g.setFont(largeFont);
        g.drawString(currentWord.getFront(), 0, 0, Graphics.TOP | Graphics.LEFT);
        y = largeFont.getHeight() + spacer;
      }

      g.drawLine(0,y, getWidth(), y);
      y += spacer;

      if (answers.size() == 0) {
        // compute how many rows we have for displaying answers
        int height = getHeight();
        height -= (y + rowHeight);
        int answerCount = height / (rowHeight);
        if (answerCount > 9)
          answerCount = 9;
        answers = quizer.getRandomAnswers(answerCount);
      }

      // draw the answers
      for (int i = 0; i < answers.size(); i++) {
        String ans = (String) answers.elementAt(i);
        String right = currentWord.getBack();
        /*
               if (right.length() > maxWidth) {
          right = right.substring(0, maxWidth-3) + "...";
               }
         */
        String a = String.valueOf(i + 1) + "." + ans;
        if (showAnswer && ans.equals(right)) {
          g.setFont(boldFont);
	  g.drawLine(0, y-1, boldFont.stringWidth(a), y-1);
	  g.drawLine(0, y+boldFont.getHeight()+1, boldFont.stringWidth(a), y+boldFont.getHeight()+1);
	  g.drawLine(boldFont.stringWidth(a)+1, y-1, boldFont.stringWidth(a)+1, y+boldFont.getHeight()+1);
        }
        else {
          g.setFont(normFont);
        }
        g.drawString(a, 0, y, Graphics.TOP | Graphics.LEFT);
        y += rowHeight;
      }

    }
    // draw status bar
    g.setFont(normFont);
    y = getHeight() - normFont.getHeight() - spacer;
    g.drawLine(0, y, getWidth(), y);
    g.drawString(statusBar, 0, this.getHeight(), Graphics.BOTTOM|Graphics.LEFT);

    // set the font back to original value (is this necessary?)
    g.setFont(gf);
  }

}

