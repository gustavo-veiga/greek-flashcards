#!/bin/sh

MAX_LESSON_WORDS=125

#JAVA_HOME=/usr/java/j2sdk1.4.2_05
WORKDIR=fcMobilePackage.$$
HTMLBASE=http://crosswire.org/fc

# prepares a lessons/ directory and packages into a jar for a mobile phone
genpackage() {
  # Test syntax.
  partNumber=""
  if [ "$#" -eq 0 ]; then
    echo $"Usage: genpackage [-p partNumber] {lessonSet}"
    return 1
  fi
  if [ "$1" = "-p" ]; then
    partNumber="."$2
    shift 2
  fi
        
#yeah, I know, it outputs to $HOME/.flashcards, lame.  FIXME
  rm -rf ~/.flashcards/lessons/$1/images
  jar cf lessons.jar lessons
  echo generating pre-rendered images for \[$1\]
  java -cp flashcards.jar org.crosswire.flashcards.LessonManager -genImages
  mv ~/.flashcards/lessons/$1/images lessons/$1/
  mv lessons res/
  rm lessons.jar
  echo LessonSet0=$1 > res/lessons/lessons.properties
  echo LessonDescription0=$1$partNumber >> res/lessons/lessons.properties
  PKGNAME=fc$i$partNumber
  sed s/##NAME##/${PKGNAME}/ ../micro/bin/MANIFEST.MF > MANIFEST.MF
  sed s/##NAME##/${PKGNAME}/ ../micro/bin/fc.jad > ${PKGNAME}.jad
  cd ..
  ant -Ddest.path=${WORKDIR} -Ddest.name=${PKGNAME} -f build.micro2.xml
  cd ${WORKDIR}
  JARSIZE=`ls -l ${PKGNAME}.jar |cut -f5 -d' '`
  sed -i s/##SIZE##/${JARSIZE}/ ${PKGNAME}.jad
  sed -i s!##HTMLBASE##!${HTMLBASE}! ${PKGNAME}.jad
  cat >> packages/index.jsp <<!
  Jad file: <a href="/fc/${PKGNAME}.jad">$i$partNumber</a> Jar file: <a href="/fc/${PKGNAME}.jar">$i$partNumber.jar</a><br/>
!
  mv ${PKGNAME}.ja[dr] packages
}

padjNum() {
    jNumPad=$jNum
    if [ "$jNum" -lt 10 ]; then
		jNumPad=0$jNumPad
    fi
    if [ "$jNum" -lt 100 ]; then
		jNumPad=0$jNumPad
    fi
}



echo setting up workspace at $WORKDIR
mkdir -p $WORKDIR/packages

echo copying jars
cp target/install/lessons.jar $WORKDIR
cp target/install/flashcards.jar $WORKDIR
cd $WORKDIR

echo unjarring lessons
jar -xf lessons.jar

#so we don't find when we run flashcards, below
mv lessons lessons.orig
rm lessons.jar

cat > packages/index.jsp <<!
<%
String section = request.getParameter("s");
%>
<html><head><title>CrossWire</title></head>
<body>
<b>Flashcards</b><br/>
<%
if (section == null) {
%>
<b>Micro Flashcards from CrossWire Bible Society.</b><br/>
May God bless your efforts to understand Him more.<br/><br/>
<%
}
%>
!


for i in `cd lessons.orig; ls`
do
  rm -rf res lessons
  mkdir -p lessons/$i
  mkdir -p res
  cd lessons.orig/$i
  jNum=0
  wCount=0
  part=1
  cat >> ../../packages/index.jsp <<!
<%
if (section == null) {
%>
  <a href="?s=$i">$i</a><br/>
<%
}
if ("$i".equals(section)) {
%>
  <a href="index.jsp">Back To Index</a><br/>
<br/><b>$i</b><br/><br/>
!
  for j in *
  do
    words=`grep wordCount $j|sed s/\[^0-9\]//g`
    echo processing lesson $j with $words words
    wCount=$(($wCount+$words))
    if [ "$wCount" -gt $MAX_LESSON_WORDS ]; then
      echo "Generating package" $i.$part "with" $(($jNum)) "lessons and" $(($wCount-$words)) "words"
      cd ../..
      genpackage -p $part $i
      part=$(($part+1))
      wCount=$words
      jNum=0
      rm -rf res lessons
      mkdir -p lessons/$i
      mkdir -p res
      cd lessons.orig/$i
    fi
    padjNum
    cp $j ../../lessons/$i/lesson${jNumPad}.flash
    jNum=$(($jNum+1))
  done
  cd ../../
  if [ $part -gt 1 ]; then
    echo "Generating package" $i.$part "with" $(($jNum-1)) "lessons and" $(($wCount)) "words"
    genpackage -p $part $i
  else
    echo "Generating package" $i "with" $(($jNum-1)) "lessons and" $(($wCount)) "words"
    genpackage $i
  fi
  cat >> packages/index.jsp <<!
<%
}
%>
!
done
cat >> packages/index.jsp <<!
</body></html>
!
