

flashgen: flashgen.o
	$(CXX) -g -I/usr/include/sword -o flashgen flashgen.o -lsword

flashgen.o: flashgen.cpp
	$(CXX) -g -I/usr/include/sword -c flashgen.cpp

clean:
	rm -f flashgen flashgen.o
