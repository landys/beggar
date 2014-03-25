#CC=cc
#CFLAGS=-Wall -O -g

#CXX=c++
#CXXFLAGS=$(CFLAGS)

#OBJDIR=obj

SRCDIR=src
INCDIR=inc

CSHELL_INCDIR=../CShell/$(INCDIR)
PROCESSINFO_INCDIR=../ProcessInfo/$(INCDIR)

INCS=-I$(INCDIR) -I$(CSHELL_INCDIR) -I$(PROCESSINFO_INCDIR)
#CFLAGS+=$(INCS)

OBJS=TestCShell.o

all:$(OBJS)

TestCShell.o:${SRCDIR}/TestCShell.cpp ${CSHELL_INCDIR}/CShell.h $(PROCESSINFO_INCDIR)/ProcessInfo.h init
	$(CXX) $(CXXFLAGS) $(INCS) -c $< -o $(OBJDIR)/$@

.PHONY:init
init:
	mkdir -p $(OBJDIR)

# make clean as a pseudo objective
.PHONY:clean
clean:
	-rm -rf $(OBJDIR)
