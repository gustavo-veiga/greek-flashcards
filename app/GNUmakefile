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
	cd src ; \
	tar cf - `find . -type f | egrep -v '\.svn'` | ( cd $(CLASSES_DIRECTORY) ; tar xvf - )
	cd .. ; \
	tar cf - `find lessons -type f | egrep -v '\.svn'` | ( cd $(CLASSES_DIRECTORY) ; tar xvf - )
	cd $(CLASSES_DIRECTORY) ; \
	jar cvmf org/crosswire/flashcards/flashcards.manifest flashcards.jar org lessons

Editor :
	java -classpath classes org.crosswire.flashcards.Editor &

Quiz :
	java -classpath classes org.crosswire.flashcards.Quiz &
