#CC=cc
#CFLAGS=-Wall -O -g

#CXX=c++
#CXXFLAGS=$(CFLAGS)

#OBJDIR=obj

#SRCDIR=src
#INCDIR=inc

INCS=-I$(INCDIR)
#CFLAGS+=$(INCS)

OBJS=CShell.o

all:$(OBJS)

CShell.o:${SRCDIR}/CShell.cpp ${INCDIR}/CShell.h init
	$(CXX) $(CXXFLAGS) $(INCS) -c $< -o $(OBJDIR)/$@

.PHONY:init
init:
	mkdir -p $(OBJDIR)

# make clean as a pseudo objective
.PHONY:clean
clean:
	-rm -rf $(OBJDIR)
