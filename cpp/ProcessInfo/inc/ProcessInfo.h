#pragma once

#include "CShell.h"
#include <stdexcept>
#include <cstdio>
#include <vector>
#include <string>
using namespace std;

class ProcessInfo
{
protected:
	ProcessInfo();
public:
	static ProcessInfo* getInstance();
	~ProcessInfo();
	vector<vector<string> > getProcessPrimaryInformation();
private:
	typedef enum CommandType {NOCOM, PS, PRSTAT} CommandType;
	void openShell(CommandType comType);
	void closeShell();
public:

private:
	static ProcessInfo* pProcInfo;
	CShell procShell;
	
	CommandType comType;
};	
