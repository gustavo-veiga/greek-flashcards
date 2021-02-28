<%@ page
    language="java"
    contentType="text/html;charset=utf-8"
%>
<%@ page import="java.util.Vector" %>
<%@ page import="org.crosswire.flashcards.LessonManager" %>
<%@ page import="org.crosswire.flashcards.LessonSet" %>
<%@ page import="org.crosswire.flashcards.Lesson" %>
<%@ page import="org.crosswire.flashcards.FlashCard" %>
<%@ page import="org.crosswire.flashcards.Quizer" %>
<%
Quizer quizer = (Quizer)session.getAttribute("fcquizer");
if (quizer == null) return;

FlashCard currentWord = (FlashCard)session.getAttribute("fcword");
Vector answers = (Vector)session.getAttribute("fcanswers");
Integer w = (Integer)session.getAttribute("fcwrong");
int wrong = -1;
if (w != null) wrong = w.intValue();
String status = "Begin";

String c = request.getParameter("c");
if (currentWord != null) {
	boolean correct = false;
	try { correct = answers.elementAt(Integer.parseInt(c)).equals(currentWord.getBack()); } catch (Exception e) {}
	
	if (correct) {
		status = "Correct";
		currentWord = null;
	}
	else {
		status = "Try again";
		session.setAttribute("fcwrong", new Integer(++wrong));
	}
}

if ((currentWord == null || answers == null)) {
	currentWord = quizer.getRandomWord(wrong);
	answers = quizer.getRandomAnswers(7);
	session.setAttribute("fcword", currentWord);
	session.setAttribute("fcanswers", answers);
	session.setAttribute("fcwrong", new Integer(0));
	c = null;
}
if (currentWord != null) {
%>

<div id="word">
<%= currentWord.getFront() %>
</div>
<hr/>
<div id="answers">
<form>
<%
for (int i = 0; i < answers.size(); i++) {
	String answer = (String)answers.elementAt(i);
	if (answer.length() > 50) answer = answer.substring(0, 50);
%>
<input type="radio" name="c" onclick="getWord(this);" value="<%=i%>" <%=answer.equals(c)?"checked=\"checked\"":""%>><%=answer%></input><br/>
<%
}
%>
</form>
</div>
<hr/>
<div id="status">
    <%=status%> | <%=Integer.toString(quizer.getNotLearnedCount())%> | <%=Integer.toString(quizer.getTotalAsked() - quizer.getTotalWrong())%>/<%=Integer.toString(quizer.getTotalAsked())%> | <%=quizer.getPercentage()%>%
</div>

<%
}
else {
%>
<br/>
<br/>
<h2>-=+* Great Job *+=-</h2>
<br/>
<br/>
<%
	session.setAttribute("fcquizer", null);
}
%>

