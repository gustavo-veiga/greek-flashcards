<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML Basic 1.1//EN"
             "http://www.w3.org/TR/xhtml-basic/xhtml-basic11.dtd" >

<%@ page import="org.crosswire.flashcards.LessonManager" %>
<%@ page import="org.crosswire.flashcards.LessonSet" %>
<%
final String SKEL_PATH  ="/home/crosswire/bin/fcskel";
final String LESSONS_PATH="/home/crosswire/html/fc/g/packages";
String lessonDir = request.getParameter("l");
lessonDir = (lessonDir == null) ? (SKEL_PATH+"/target/install")
				: (LESSONS_PATH+"/"+lessonDir);

LessonManager mgr = (LessonManager)session.getAttribute("fcmgr");
String oldLessonDir = (String)session.getAttribute("fclessdir");
if (!lessonDir.equals(oldLessonDir)) mgr = null;
if (mgr == null) {
	session.setAttribute("fcmgr", new LessonManager(lessonDir));
	session.setAttribute("fclessdir", lessonDir);
}
mgr = (LessonManager)session.getAttribute("fcmgr");
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US">

<head profile="http://www.w3.org/2000/08/w3c-synd/#">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>Flashcards Web Quizer</title>
<script type="text/javascript" src="//www.google.com/jsapi"></script>
<script type="text/javascript">
// <!--
google.load("prototype", "1.6.0.2");
function lschange(ls) {
	d = document.getElementById('lessons');
	d.innerHTML='';
	new Ajax.Request('getLessons.jsp?l='+ls.value, {
		method: 'get',
		onSuccess: function(transport) {
			d = document.getElementById('lessons');
			d.innerHTML = transport.responseText;
		}
	});
}

function lessonchange(l) {
	return;
	var lessons='';
	for (var i=0; i<l.options.length; i++) {
		if (l.options[i].selected) {
			lessons+=((lessons.length>0)?'&':'?');
			lessons+='l='+escape(l.options[i].value);
		}
	}
}

function quiz() {
	new Ajax.Request('clear.jsp?t='+new Date().getTime(), {
		method: 'get',
		onSuccess: function(transport) {
			d = document.getElementById('setup');
			d.submit();
		}
	});
}
// -->
</script>
</head>
<body>
<form id="setup" action="quiz.jsp" method="get">
<fieldset>
<select name="ls" onchange="lschange(this)" size="10">
<%
for (Object o : mgr.getLessonSets()) {
LessonSet ls = (LessonSet)o;
if (!"images".equals(ls.getDescription())) {
%>
<option value="<%=ls.getDescription()%>"><%=ls.getDescription()%></option>
<%
}
}
%>
</select>
<select id="lessons" name="l" onchange="lessonchange(this)" size="10" multiple="multiple">
<option>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
</select>
<button onclick="quiz(); return false;">Quiz</button>
</fieldset>
</form>
</body>
</html>
