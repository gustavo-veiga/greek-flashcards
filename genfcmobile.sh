#!/bin/sh

JAVA_HOME=/usr/java/j2sdk1.4.2_05
WORKDIR=fcMobilePackage.$$

echo setting up workspace at $WORKDIR
mkdir $WORKDIR

echo copying jars
cp target/install/lessons.jar $WORKDIR
cp target/install/flashcards.jar $WORKDIR
cd $WORKDIR

echo unjarring lessons
jar -xf lessons.jar

#so we don't find when we run flashcards, below
mv lessons lessons.orig
rm lessons.jar

cat > index.html <<!
<html><head><title>CrossWire</title></head><body><b>Flashcards</b><br/>This is an early release of a micro edition of Flashcards from CrossWire Bible Society. To try it out, click on the link below below:<br/>
<a href="/fc/FlashcardsMobile.jad">Flashcards - Hebrew</a><br/>
<a href="/fc/oldphone/FlashcardsMobile.jad">Flashcards(old phones) - Hebrew</a><br/>
<a href="/fc/test/FlashcardsMobile.jad">Flashcards - Phone Test</a><br/>
Other Lessons:<br/>
!


for i in `cd lessons.orig; ls`
do
  rm -rf res lessons
  mkdir -p lessons/$i
  mkdir -p res
  cd lessons.orig/$i
  jNum=0;
  for j in *
  do
    cp $j ../../lessons/$i/lesson${jNum}.flash
    jNum=$(($jNum+1))
  done
  cd ../../
#yeah, I know, it outputs to $HOME/.flashcards, lame.  FIXME
  rm -rf ~/.flashcards/lessons/$i/images
  jar cf lessons.jar lessons
  echo generating pre-rendered images for \[$i\]
  java -cp flashcards.jar org.crosswire.flashcards.LessonManager -genImages
  mv ~/.flashcards/lessons/$i/images lessons/$i/
  mv lessons res/
  rm lessons.jar
  echo LessonSet0=$i > res/lessons/lessons.properties
  echo LessonDescription0=$i >> res/lessons/lessons.properties
  PKGNAME=fc$i
  sed s/##NAME##/${PKGNAME}/ ../micro/bin/MANIFEST.MF > MANIFEST.MF
  sed s/##NAME##/${PKGNAME}/ ../micro/bin/fc.jad > ${PKGNAME}.jad
  cd ..
  ant -Ddest.path=${WORKDIR} -Ddest.name=${PKGNAME} -f build.micro2.xml
  cd ${WORKDIR}
  JARSIZE=`ls -l ${PKGNAME}.jar |cut -f5 -d' '`
  sed -i s/##SIZE##/${JARSIZE}/ ${PKGNAME}.jad
  cat >> index.html <<!
  <a href="/fc/${PKGNAME}.jad">$i</a><br/>
!
done
cat >> index.html <<!
</body></html>
!
