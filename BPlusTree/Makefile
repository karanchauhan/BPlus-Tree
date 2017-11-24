JCC =javac
JFLAGS = -g
CPATH = com/karan/ads/bplustree/
# typing 'make' will invoke the first target entry in the makefile 
# (the default one in this case)
#

default: Key.class Node.class BPlusTree.class treesearch.class

Key.class: $(CPATH)Key.java
	$(JCC) $(JFLAGS) $(CPATH)Key.java

Node.class: $(CPATH)Node.java
	$(JCC) $(JFLAGS) $(CPATH)Node.java

BPlusTree.class: $(CPATH)BPlusTree.java
	$(JCC) $(JFLAGS) $(CPATH)BPlusTree.java

treesearch.class: treesearch.java
	$(JCC) $(JFLAGS) treesearch.java

clean: 
	$(RM) *.class
	$(RM) $(CPATH)*.class
