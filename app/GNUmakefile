#
# GNUmakefile
#
# Makefile for Crosswire's flashcard program.
#

######################################################################
# Macros

CLASSES_DIRECTORY = $(shell pwd)/classes

CHECK_CLASSES_DIRECTORY = \
if [ ! -d $(CLASSES_DIRECTORY) ] ; then \
	mkdir $(CLASSES_DIRECTORY) ; \
fi

######################################################################
# Targets

.PHONY : clean flashcards Editor Quiz

all : flashcards

clean :
	rm -rf classes

flashcards :
	@$(CHECK_CLASSES_DIRECTORY)
	javac -g -d $(CLASSES_DIRECTORY) `find . -type f -name '*.java'`
	cp src/org/crosswire/flashcards/*png classes/org/crosswire/flashcards
	cd $(CLASSES_DIRECTORY) ; \
	jar cvf $(CLASSES_DIRECTORY)/flashcards.jar org

Editor :
	java -classpath classes org.crosswire.flashcards.Editor &

Quiz :
	java -classpath classes org.crosswire.flashcards.Quiz &
