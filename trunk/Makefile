all:
	ant -emacs

clean:
	ant clean
	rm -rf FlashCards

serverinstall:

	rm -rf FlashCards
	mkdir FlashCards
	cp target/install/flashcards.jar FlashCards
	zip FlashCards.zip FlashCards/*
	cp -f FlashCards.zip /home/ftp/pub/flashcards
	cp -f FlashCards/flashcards.jar /home/flashcards/html/webstart/
	rm -rf FlashCards
