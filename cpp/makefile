export CC=cc
export CFLAGS=-Wall -O -g

export CXX=c++
export CXXFLAGS=$(CFLAGS)

export OBJDIR=obj
export SRCDIR=src
export INCDIR=inc

export JDK_INCLUDE=/usr/java/include
export OS_JDK_INCLUDE=$(JDK_INCLUDE)/solaris

BINDIR=bin
DLLS=libProcessInfo.so
EXES=TestCShell.o
INCS=
LDFLAGS=
LIBS=

SUBDIRS=CShell ProcessInfo TestCShell

TESTCSHELL_DIRS=CShell ProcessInfo TestCShell
TESTCSHELL_OBJS=$(wildcard $(patsubst %,%/$(OBJDIR)/*.o,$(TESTCSHELL_DIRS)))

PROCESSINFO_DIRS=CShell ProcessInfo
PROCESSINFO_OBJS=$(wildcard $(patsubst %,%/$(OBJDIR)/*.o,$(PROCESSINFO_DIRS)))

all:$(DLLS) ${EXES}

.PHONY:${EXES} $(DLLS)

TestCShell.o:init subdirs
	$(CXX) $(LDFLAGS) $(LIBS) -o $(BINDIR)/$@ ${TESTCSHELL_OBJS}

libProcessInfo.so:init subdirs
	$(CXX) $(LDFLAGS) $(LIBS) -G -o $(BINDIR)/$@ $(PROCESSINFO_OBJS)

.PHONY:subdirs $(SUBDIRS)
subdirs:$(SUBDIRS)

$(SUBDIRS):
	$(MAKE) -C $@

.PHONY:init
init:
	mkdir -p $(BINDIR)

# make clean as a pseudo objective
.PHONY:clean    
clean:
	-rm -rf $(BINDIR)
	for dir in $(SUBDIRS); do $(MAKE) -C $$dir clean; done
