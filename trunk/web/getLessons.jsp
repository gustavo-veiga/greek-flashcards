<%@ page import="org.crosswire.flashcards.LessonManager" %>
<%@ page import="org.crosswire.flashcards.LessonSet" %>
<%@ page import="org.crosswire.flashcards.Lesson" %>
<%
LessonManager mgr = (LessonManager)session.getAttribute("fcmgr");
String l = request.getParameter("l");
if (mgr == null) return;
if (l == null) return;
LessonSet ls = mgr.getLessonSet(l);
if (ls == null) return;
for (Object o : ls.getLessons()) {
	Lesson lesson = (Lesson)o;
%>
	<option value="<%=lesson.getDescription()%>"><%=lesson.getDescription()%></option>
<%
}
%>

