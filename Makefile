PLUGIN=AllowPlayers
OUT=$(PLUGIN).jar

JC=javac
JFLAGS=-g

JAR=jar
MKDIR=mkdir
RM=rm
WGET=wget

CWD=$(shell pwd)

SRC=src
DEP=.dep

.SUFFIXES: .java .class

SRCS=\
  $(SRC)/org/AllowPlayers/APConfiguration.java \
  $(SRC)/org/AllowPlayers/Log.java \
  $(SRC)/org/AllowPlayers/Message.java \
  $(SRC)/org/AllowPlayers/Request.java \
  $(SRC)/org/AllowPlayers/EventListener.java \
  $(SRC)/org/AllowPlayers/Watcher.java \
  $(SRC)/org/AllowPlayers/AllowPlayers.java \
  $(SRC)/org/AllowPlayers/command/CAllowPlayers.java \
  $(SRC)/org/AllowPlayers/command/CMCNet.java \
  $(SRC)/org/AllowPlayers/command/COnlineMode.java

DEPS=$(DEP)/CraftBukkit.jar:$(SRC)
OBJS=$(SRCS:.java=.class)

all: $(OUT)

$(OUT): objs
	$(JAR) cf $(OUT) -C $(SRC) plugin.yml $(OBJS)

objs: $(OBJS)

%.class: %.java
	$(JC) -classpath $(DEPS) -sourcepath $(SRC) $(JFLAGS) $<

deps:
	$(RM)    -rf $(DEP)
	$(MKDIR) -p  $(DEP)
	
	$(WGET) -O $(DEP)/CraftBukkit.jar http://ci.bukkit.org/job/dev-CraftBukkit/1846/artifact/target/craftbukkit-1.1-R3.jar

clean:
	$(RM) -f $(OBJS) $(OUT)
