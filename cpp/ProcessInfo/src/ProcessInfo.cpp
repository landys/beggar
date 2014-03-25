#include "ProcessInfo.h"

// the ps command
string ps_command[] = {"ps"};
#define PS_COMMAND_LENGTH 1  // the PS command length

// the prstat command
string prstat_command[] = {"prstat"};
#define PRSTAT_COMMAND_LENGTH 1 	// the prstat commande length

// init the instance
ProcessInfo* ProcessInfo::pProcInfo = 0;

ProcessInfo::ProcessInfo()
{
	comType = NOCOM;
}

ProcessInfo::~ProcessInfo()
{
	if(comType != NOCOM)
	{
		procShell.cclose();
	}
} 

ProcessInfo* ProcessInfo::getInstance()
{
	if(pProcInfo == 0)
	{
		pProcInfo = new ProcessInfo();
	}

	return pProcInfo;
}

vector<vector<string> > ProcessInfo::getProcessPrimaryInformation()
{
	// the buffer to which CShell read the line string from pipe
	char buf[500];
	// the return vector, the first(0) vector<string> is the column name,
	// and the follow vector<string> is the values correspond to the colume 
	vector<vector<string> > retVec;

	openShell(PRSTAT);

	int columnNum = 0;
	for(int i = 0; procShell.cgets(buf, 500); i++)
	{
		if(buf[0] == '\n')
		{
			continue;
		}

		vector<string> record;

		if(i == 0)
		{// get columns' names
			bool isStart = false;
			int leftSeq = 0;

			for(int j = 0; buf[j] != '\0'; j++)
			{
				if(buf[j] == '\t' || buf[j] == ' ' || buf[j] == '\n')
				{// meet delimit
					if(isStart)
					{
						isStart = false;
						columnNum++;
						record.push_back(string(buf + leftSeq, j - leftSeq));			
					}
				}
				else
				{// normal character
					if(!isStart)
					{
						isStart = true;
						leftSeq = j;
					}
				}
			}
		}
		else
		{
			string str(buf);

			if(str.substr(0, 6) == "Total:")
			{// it is the last line in the frame
				record.push_back(str);
				retVec.push_back(record);
				break;
			}

			// now it is not the last line, it is a normal line
			// get columns' value
			int leftSeq = 0;
			int rightSeq = 0;

			for(int j = 0; j < columnNum - 1; j++)
			{
				// remove the space
				while(buf[rightSeq] == '\t' || buf[rightSeq] == ' ')
				{
					rightSeq++;
				}

				// remove the "I" column
				if(j == 0 && buf[rightSeq] == 'I')
				{
					rightSeq++;
					while(buf[rightSeq] == '\t' || buf[rightSeq] == ' ')
					{
						rightSeq++;
					}
				}
				// the total value num is fewer than columnNum
				if(buf[rightSeq] == '\n')
				{
					throw std::runtime_error
						("getProcessPrimaryInformation: command value num not match");
				}

				leftSeq = rightSeq;
				while(buf[rightSeq] != '\t' && buf[rightSeq] != ' ')
				{
					rightSeq++;
				}
				record.push_back(string(buf + leftSeq, rightSeq - leftSeq));
			}

			// get the last value, maybe it contain the space in the value
			while(buf[rightSeq] == '\t' || buf[rightSeq] == ' ')
			{
				rightSeq++;
			}

			if(buf[rightSeq] == '\n')
			{
				throw std::runtime_error
					("getProcessPrimaryInformation: command value num not match");
			}

			leftSeq = rightSeq;
			if(buf[strlen(buf) - 1] == '\n')
			{
				record.push_back(string(buf + leftSeq, strlen(buf) - leftSeq - 1));
			}
			else
			{
				record.push_back(string(buf + leftSeq, strlen(buf) - leftSeq));
			}
		}

		retVec.push_back(record);
	}

	return retVec;
}

void ProcessInfo::openShell(CommandType comType)
{
	if(this->comType != comType)
	{
		this->comType = comType;
		switch(comType)
		{
			case PS:
			{
				procShell.setCommand(ps_command, PS_COMMAND_LENGTH);
				procShell.copen();
				break;
			}
			case PRSTAT:
			{
				procShell.setCommand(prstat_command, PRSTAT_COMMAND_LENGTH);
				procShell.copen();
				break;
			}
			default:
			{
				throw std::runtime_error
					("ProcessInfo::openShell: command enum not found!");
			}
		}		
	}
}

void ProcessInfo::closeShell()
{
	if(comType != NOCOM)
	{
		procShell.cclose();
		comType = NOCOM;
	}
}
