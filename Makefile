all:
	ant -emacs

clean:
	ant clean
	rm -rf FlashCards

serverinstall:

	rm -rf FlashCards
	mkdir FlashCards
	cp target/install/flashcards.jar FlashCards
	cp target/install/lessons.jar FlashCards
	zip FlashCards.zip FlashCards/*
	cp -f FlashCards.zip /home/ftp/pub/flashcards
	cp -f FlashCards/*.jar /home/flashcards/html/webstart/
	rm -rf FlashCards
