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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;


/**
 * A panel that quizzes over a selection of lessons.
 *
 * @author Troy A. Griffitts [scribe at crosswire dot org]
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public class QuizPane extends JPanel
{
    SetupPane setupPane;
    Vector words = new Vector();
    Vector notLearned = new Vector();
    WordEntry currentWord = null;
    int wrong = 0;
    int totalAsked = 0;
    int totalWrong = 0;
    boolean shownAnswer = false;
    JButton startLessonButton = new JButton();
    JButton showAnswerButton = new JButton();
    JLabel wordText = new JLabel();
    JLabel statusBar = new JLabel();
    JLabel wCount = new JLabel();

    JPanel choicesPanel = new JPanel();
    GridLayout choicesPanelGridLayout = new GridLayout();
    JPanel statusPanel = new JPanel();
    BorderLayout statusPanelBorderLayout = new BorderLayout();
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    static class WordEntry
    {
        public WordEntry(FlashCard flashCard)
        {
            this.flashCard = flashCard;
        }

        public void incrementFailures(int failures)
        {
            attempts += failures;
        }

        public int getFailures()
        {
            return attempts;
        }

        public String getSide(boolean front)
        {
            return flashCard.getSide(front);
        }

        public String toString()
        {
            return flashCard.getFront();
        }
        private FlashCard flashCard;
        private int attempts;
    }


    //Construct the frame
    public QuizPane(SetupPane setupPane)
    {
        this.setupPane = setupPane;
        try
        {
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //Component initialization
    private void jbInit() throws Exception
    {
        startLessonButton.setText("Start");
        startLessonButton.addActionListener(new QuizPane_startLessonButton_actionAdapter(this));

        showAnswerButton.setFocusPainted(true);
        showAnswerButton.setMnemonic('A');
        showAnswerButton.setText("Show Answer");
        showAnswerButton.addActionListener(new QuizPane_showAnswerButton_actionAdapter(this));

        wordText.setBackground(SystemColor.text);
        wordText.setFont(new Font("Dialog", 0, 30));
        wordText.setHorizontalAlignment(SwingConstants.CENTER);
        wordText.setHorizontalTextPosition(SwingConstants.CENTER);

        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setText(" ");
        wCount.setBorder(BorderFactory.createEtchedBorder());

        choicesPanel.setLayout(choicesPanelGridLayout);
        choicesPanelGridLayout.setColumns(1);
        choicesPanelGridLayout.setRows(0);

        statusPanel.setLayout(statusPanelBorderLayout);
        statusBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        statusPanel.add(statusBar, BorderLayout.CENTER);
        statusPanel.add(wCount, BorderLayout.EAST);

        setLayout(gridBagLayout1);

        add(startLessonButton,  new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(showAnswerButton,  new GridBagConstraints(2, GridBagConstraints.RELATIVE, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(wordText,  new GridBagConstraints(0, GridBagConstraints.RELATIVE, 3, 1, 1.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(choicesPanel,  new GridBagConstraints(0, GridBagConstraints.RELATIVE, 3, 1, 0.0, 1.0
            ,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(statusPanel,  new GridBagConstraints(0, GridBagConstraints.RELATIVE, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 10, 10));
    }

    public void deleteChildren(JComponent c)
    {
        while (c.getComponentCount() > 0)
            c.remove(c.getComponent(0));
    }

    public void loadTest()
    {
//        boolean loadedFont = false;
        words = new Vector();
        Iterator lessonIter = setupPane.iterator();
        while (lessonIter.hasNext())
        {
            Lesson lesson = (Lesson) lessonIter.next();
            Iterator cardIter = lesson.iterator();
            while (cardIter.hasNext())
            {
                words.add(new WordEntry((FlashCard) cardIter.next()));
            }
//            if (!loadedFont)
//            {
//                String font = lesson.getFont();
//                if (font.length() > 1)
//                {
//                    try
//                    {
//                        loadFont(new FileInputStream(font));
//                        loadedFont = true;
//                    }
//                    catch (FileNotFoundException ex)
//                    {
//                    }
//                }
//            }
        }
    }

//    public void loadFont(InputStream is)
//    {
//        try
//        {
//            statusBar.setText("Loading font...");
//            statusBar.paintImmediately(statusBar.getVisibleRect());
//            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
//            Font newFont = font.deriveFont((float) 18.0);
//            wordText.setFont(newFont);
//            is.close();
//            statusBar.setText("New Font Loaded.");
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    void startLessonButton_actionPerformed(ActionEvent e)
    {
        loadTest();
        notLearned = (Vector) words.clone();
        totalAsked = 0;
        totalWrong = 0;
        showRandomWord(currentWord);
    }

    public void showRandomWord(WordEntry last)
    {
        deleteChildren(choicesPanel);
        int numToLearn = notLearned.size();
        if (numToLearn == 0)
        {
            return;
        }
        while (currentWord == last)
        {
            int wordNum = (int) (Math.random() * notLearned.size());
            currentWord = (WordEntry) notLearned.get(wordNum);
        }
        showWord(currentWord);
    }


    public void showWord(WordEntry w)
    {
        currentWord = w;
        wordText.setText(w.getSide(!setupPane.isFlipped()));
        Vector choices = (Vector) words.clone();
        choices.remove(w);

        // randomly pick answers
        boolean flipped = setupPane.isFlipped();
        List picks = new ArrayList();
        picks.add(createAnswerEntry(w.getSide(flipped)));
        int size = words.size();
        while (picks.size() < Math.min(10, size))
        {
            int c = (int) (Math.random() * choices.size());
            WordEntry wc = (WordEntry) choices.get(c);
            String answer = wc.getSide(flipped);

            // some times two different word have the same answer
            if (!picks.contains(answer))
            {
                picks.add(createAnswerEntry(answer));
            	choices.remove(wc);
            }
        }
        // Now randomize these answers. To do this we swap the first one
        // with another.
        int c = (int) (Math.random() * picks.size());
        // If we have selected something other than ourselves.
        if (c > 0)
        {
            picks.add(0, picks.remove(c));
            picks.add(c, picks.remove(1));
        }
        Iterator iter = picks.iterator();
        while (iter.hasNext())
        {
            choicesPanel.add((Component) iter.next());
        }
        wrong = 0;
        shownAnswer = false;
        updateStats();
        choicesPanel.invalidate();
        choicesPanel.validate();
        choicesPanel.repaint();
    }


    Component createAnswerEntry(String answer)
    {
        JCheckBox ck = new JCheckBox(answer, false);
        ck.setFont(new Font("Dialog", 0, 16));
        ck.addItemListener(new QuizPane_answer_itemAdapter(this));
        return ck;
    }

    void updateStats()
    {
        int percent = 100;
        if (totalAsked > 0)
        {
            percent = (int) ((((float) (totalAsked - totalWrong)) / (float) totalAsked) * (float) 100);
        }
        wCount.setText(Integer.toString(notLearned.size()) + " | " + Integer.toString(totalAsked - totalWrong) + "/" + Integer.toString(totalAsked) + " ("
                        + Integer.toString(percent) + "%)");
    }


    void answer_itemStateChanged(ItemEvent e)
    {
        JCheckBox ck = (JCheckBox) e.getItem();
        if (ck.isSelected())
        {
            totalAsked++;
            if (ck.getText().compareTo(currentWord.getSide(setupPane.isFlipped())) != 0)
            {
                statusBar.setText("Please try again. " + ck.getText() + " is not correct.");
                wrong++;
                totalWrong++;
                ck.setSelected(false);
            }
            else
            {
                if (notLearned.size() > 1)
                {
                    statusBar.setText("Correct.  Try this next word");
                    if (wrong > 0)
                    {
                        currentWord.incrementFailures(wrong);
                    }
                    else
                        currentWord.incrementFailures(-1);
                    if (currentWord.getFailures() < 0)
                    {
                        notLearned.remove(currentWord);
                    }
                    showRandomWord(currentWord);
                }
                else
                {
                    notLearned.remove(currentWord);
                    deleteChildren(choicesPanel);
                    wordText.setText("-=+* Great! *+=-");
                    statusBar.setText("Nice Job!  You've mastered all " + words.size() + " words!");
                }
            }
            updateStats();
        }
    }

    public void showAnswer()
    {
        for (int i = 0; i < choicesPanel.getComponentCount(); i++)
        {
            JCheckBox ck = (JCheckBox) choicesPanel.getComponent(i);
            if (ck.getText() == currentWord.getSide(setupPane.isFlipped()))
            {
                ck.setFont(new Font(ck.getFont().getName(), Font.BOLD | Font.ITALIC, ck.getFont().getSize()));
                break;
            }
        }
        shownAnswer = true;
    }

    void showAnswerButton_actionPerformed(ActionEvent e)
    {
        if (!shownAnswer)
        {
            showAnswer();
            return;
        }
        int next = notLearned.indexOf(currentWord) + 1;
        if (next == 0)
        {
            return;
        }
        if (next >= notLearned.size())
            next = 0;
        deleteChildren(choicesPanel);
        showWord((WordEntry) notLearned.get(next));
        showAnswer();
    }

}

class QuizPane_startLessonButton_actionAdapter implements ActionListener
{
    QuizPane adaptee;

    QuizPane_startLessonButton_actionAdapter(QuizPane adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.startLessonButton_actionPerformed(e);
    }
}

class QuizPane_answer_itemAdapter implements ItemListener
{
    QuizPane adaptee;

    QuizPane_answer_itemAdapter(QuizPane adaptee)
    {
        this.adaptee = adaptee;
    }

    public void itemStateChanged(ItemEvent e)
    {
        adaptee.answer_itemStateChanged(e);
    }
}

class QuizPane_showAnswerButton_actionAdapter implements ActionListener
{
    QuizPane adaptee;

    QuizPane_showAnswerButton_actionAdapter(QuizPane adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.showAnswerButton_actionPerformed(e);
    }
}
