#CC=cc
#CFLAGS=-Wall -O -g

#CXX=c++
#CXXFLAGS=$(CFLAGS)

#OBJDIR=obj

#SRCDIR=src
#INCDIR=inc

CSHELL_INCDIR=../CShell/$(INCDIR)

INCS=-I$(INCDIR) -I$(CSHELL_INCDIR) -I$(JDK_INCLUDE) -I$(OS_JDK_INCLUDE)

OBJS=ProcessInfo.o ProcessInfoService.o

all:$(OBJS)

ProcessInfo.o:${SRCDIR}/ProcessInfo.cpp ${INCDIR}/ProcessInfo.h ${CSHELL_INCDIR}/CShell.h init
	$(CXX) $(CXXFLAGS) $(INCS) -c $< -o $(OBJDIR)/$@

ProcessInfoService.o:${SRCDIR}/ProcessInfoService.cpp ${INCDIR}/ProcessInfo.h ${INCDIR}/zju_beggar_service_ProcessInfoService.h init
	$(CXX) $(CXXFLAGS) $(INCS) -c $< -o $(OBJDIR)/$@

.PHONY:init
init:
	mkdir -p $(OBJDIR)

# make clean as a pseudo objective
.PHONY:clean
clean:
	-rm -rf $(OBJDIR)
