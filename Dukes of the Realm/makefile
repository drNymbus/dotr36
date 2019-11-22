JFLAGS = -g -d bin -cp src
JC = javac

.SUFFIXES: .java .class

.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		src/board/Main.java \
		src/board/Input.java \
		src/board/Sprite.java \
		src/board/Settings.java \
		src/game/Battlefield.java \
		src/game/Castle.java \
		src/game/Soldier.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) ../bin/*.class
