all:
	ant -emacs

clean: zipclean
	ant clean

serverinstallzip: zipclean
	mkdir FlashCards
	cp target/install/flashcards.jar FlashCards
	cp target/install/lessons.jar FlashCards
	zip FlashCards.zip FlashCards/*
	cp -f FlashCards.zip /home/ftp/pub/flashcards

zipclean:
	rm -rf FlashCards

serverinstall: serverinstalllessons serverinstallapp

serverinstallapp: serverinstallzip
	cp -f FlashCards/flashcards.jar /home/flashcards/html/webstart/

serverinstalllessons: serverinstallzip
	cp -f FlashCards/lessons.jar /home/flashcards/html/webstart/
