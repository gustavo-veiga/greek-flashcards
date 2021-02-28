<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-US" lang="en-US">
<%@ page
    language="java"
    contentType="text/html;charset=utf-8"
%>
<%@ page import="org.crosswire.flashcards.LessonManager" %>
<%@ page import="org.crosswire.flashcards.LessonSet" %>
<%@ page import="org.crosswire.flashcards.Lesson" %>
<%@ page import="org.crosswire.flashcards.Quizer" %>
<%
Quizer quizer = (Quizer)session.getAttribute("fcquizer");
if (quizer == null) {
	LessonManager mgr = (LessonManager)session.getAttribute("fcmgr");
	String ls = request.getParameter("ls");
	String l[] = request.getParameterValues("l");
	if (mgr == null) return;
	if (ls == null) return;
	if (l == null) return;
	if (l.length == 0) return;
	LessonSet leset = mgr.getLessonSet(ls);
	if (leset == null) return;
	quizer = new Quizer();
	for (String o : l) {
		Lesson lesson = leset.getLesson(o);
		if (lesson != null) {
			quizer.loadLesson(lesson);
		}
	}
	session.setAttribute("fcquizer", quizer);
}
%>
<head profile="http://www.w3.org/2000/08/w3c-synd/#">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="//www.google.com/jsapi"></script>
<script language="JavaScript">
google.load("prototype", "1.6.0.2");

function getWord(choice) {
	s = document.getElementById('status');
	if (s) s.innerHTML = '';
	options = document.getElementsByTagName('input');
	for (i = 0; i < options.length; i++) {
		var o = options[i];
		o.disabled = true;
	}
	var u = 'getWord.jsp'
	if (choice) u += '?c='+escape(choice.value);
	new Ajax.Request(u, {
		method: 'get',
		onSuccess: function(transport) {
			d = document.getElementById('wordItem');
			d.innerHTML = transport.responseText;
		}
	});
}

function loaded() {
	getWord();
}
</script>
<style>
#word {
  font-size: 300%;
}
</style>
</head>
<body onload="loaded();">
<div id="wordItem">
</div>
</body>
</html>
