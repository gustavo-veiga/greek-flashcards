#
# GNUmakefile
#
# Makefile for Crosswire's flashcard program.
#

######################################################################
# Macros

JAVAC=$(JAVA_HOME)/bin/javac
if [ ! -f $(JAVAC) ] ; then \
	JAVAC=javac ; \
fi

CLASSES_DIRECTORY = $(shell pwd)/classes

CHECK_CLASSES_DIRECTORY = \
if [ ! -d $(CLASSES_DIRECTORY) ] ; then \
	mkdir $(CLASSES_DIRECTORY) ; \
fi

######################################################################
# Targets

.PHONY : all clean jar_release jar_develop classes_release classes_develop

all : jar_release

clean :
	rm -rf classes

jar_release : classes_release
	cd $(CLASSES_DIRECTORY) ; \
	jar cvmf org/crosswire/flashcards/flashcards.manifest flashcards.jar org lessons

jar_develop : classes_develop
	cd $(CLASSES_DIRECTORY) ; \
	jar cvmf org/crosswire/flashcards/flashcards.manifest flashcards.jar org lessons

classes_release : clean
	@$(CHECK_CLASSES_DIRECTORY)
	$(JAVAC) -g:none -Xlint -source 1.4 -d $(CLASSES_DIRECTORY) `find . -type f -name '*.java'`
	cd src ; \
	tar cf - `find . -type f -name '*pgn'` | ( cd $(CLASSES_DIRECTORY) ; tar xf - )
	cd src ; \
	tar cf - `find . -type f -name '*manifest'` | ( cd $(CLASSES_DIRECTORY) ; tar xf - )
	cd .. ; \
	tar cf - `find lessons -type f | egrep -v '\.svn'` | ( cd $(CLASSES_DIRECTORY) ; tar xf - )

classes_develop : clean
	@$(CHECK_CLASSES_DIRECTORY)
	$(JAVAC) -g -Xlint -source 1.4 -d $(CLASSES_DIRECTORY) `find . -type f -name '*.java'`
	cd src ; \
	tar cf - `find . -type f | egrep -v '\.svn'` | ( cd $(CLASSES_DIRECTORY) ; tar xf - )
	cd .. ; \
	tar cf - `find lessons -type f | egrep -v '\.svn'` | ( cd $(CLASSES_DIRECTORY) ; tar xf - )
