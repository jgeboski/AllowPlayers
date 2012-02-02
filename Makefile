PLUGIN=AllowPlayers
CWD=$(shell pwd)
SRC=src
DEPS=.deps
OBJS=.objs

JAR=$(CWD)/$(PLUGIN).jar

SRCS=$(shell find $(SRC) -type f -name *.java -printf "%h/%f ")
JDEPS=$(shell find $(DEPS) -type f -printf "%h/%f:")

all: deps objs jar

jar:
	rm -f $(JAR)
	jar cvf $(JAR) -C $(OBJS) .

objs:
	rm -rf $(OBJS)
	mkdir -p $(OBJS)
	cp $(SRC)/plugin.yml $(OBJS)
	javac -cp $(JDEPS) -d $(OBJS) -g $(SRCS)

deps:
	rm -rf $(DEPS)
	mkdir -p $(DEPS)
	
	wget -O $(DEPS)/CraftBukkit.jar http://ci.bukkit.org/job/dev-CraftBukkit/1846/artifact/target/craftbukkit-1.1-R3.jar
