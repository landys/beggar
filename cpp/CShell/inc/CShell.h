#pragma once

#include <stdexcept>
#include <cstdio>
#include <string>

class CShell
{
private:
	// executive command, i.e. dtrace scipt 
	char** commands;
	// whether read as binary file
	bool isBin;
	// io stream
	FILE* pp;
	// pid of the process running the command
	int pid;
public:
	CShell();
	CShell(const std::string command, const bool isBin=false);
	// length is length of array commands
	CShell(const std::string commands[], const int length, const bool isBin=false);
	virtual ~CShell();
	
	// set content of command
	void setCommand(const std::string command, const bool isBin=false) throw (std::runtime_error);
	// length is length of array commands
	void setCommand(const std::string commands[], const int length, const bool isBin=false) throw (std::runtime_error);
	
	// start a process to run the command, and open a pipe
	void copen() throw (std::runtime_error);
	// close pipe, kill the process if it is not exit
	void cclose() throw (std::runtime_error);
	// read string from pipe, similar as fgets in standard c library
	char* cgets(char* str, int n)/* throw (std::runtime_error)*/;
	// check if whether the pipe is open
	bool isOpen();

private:
	inline void initCommand(const std::string command, const bool isBin);
	inline void initCommand(const std::string commands[], const int length, const bool isBin);
	inline void deleteCommand();
};

