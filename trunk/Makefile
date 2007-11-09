all:
	ant -emacs

clean: zipclean mobileclean
	ant clean

serverinstallzip: zipclean
	mkdir FlashCards
	cp target/install/flashcards.jar FlashCards
	cp target/install/lessons.jar FlashCards
	zip FlashCards.zip FlashCards/*
	cp -f FlashCards.zip /home/ftp/pub/flashcards

zipclean:
	rm -rf FlashCards

extraLessons:
	make -C ../sword-tools/flashtools lessons
	cp -r ../sword-tools/flashtools/lessons/* lessons

extraLessonsClean:
	rm -rf lessons/greekFreq lessons/greekFreqKJV lessons/hebrewFreq lessons/hebrewFreqKJV

serverinstall: serverinstalllessons serverinstallapp

serverinstallapp: serverinstallzip
	cp -f FlashCards/flashcards.jar /home/flashcards/html/webstart/

serverinstalllessons: serverinstallzip
	cp -f FlashCards/lessons.jar /home/flashcards/html/webstart/

mobile: 
#	JAVA_HOME=/usr/java/j2sdk1.4.2_05 ant -f build.micro1.xml
	ant -f build.micro1.xml
	./genfcmobile.sh

mobileclean:
	rm -rf microbuild
	rm -rf fcMobilePackage*
