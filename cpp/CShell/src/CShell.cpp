#include "CShell.h"
#include <signal.h>
#include <sys/wait.h>

CShell::CShell()
{
	pp = 0;
	isBin = false;
	commands = 0;
}

CShell::CShell(const std::string command, const bool isBin)
{
	initCommand(command, isBin);
}

CShell::CShell(const std::string commands[], const int length, const bool isBin)
{
	initCommand(commands, length, isBin);
}

CShell::~CShell()
{
	deleteCommand();
}

void CShell::setCommand(const std::string command, const bool isBin) throw (std::runtime_error)
{
	cclose();
	deleteCommand();
	initCommand(command, isBin);
}

void CShell::setCommand(const std::string commands[], const int length, const bool isBin) throw (std::runtime_error)
{
	cclose();
	deleteCommand();
	initCommand(commands, length, isBin);
}	
		
void CShell::copen() throw (std::runtime_error)
{
	if (commands == 0)
	{
		return;
	}
	if (isOpen())
	{
		cclose();
	}
	int fd[2];
	// open a unnamed pipe, often fd[0] for read end, fd[1] for write end
	if (pipe(fd) == -1)
	{
		throw std::runtime_error("pipe error");
	}
	
	// fork a process to run the command
	pid = fork();
	switch (pid)
	{
		// error
		case -1:
		{
			throw std::runtime_error("fork error");
		}
		// child process
		case 0:
		{
			// read end of pipe
			close(fd[0]);
			// get file descriptor of stdout
			int oid = fileno(stdout);
			// stdout
			close(oid);
			// redirect stdout to write end of pipe
			if (dup2(fd[1], oid) == -1)
			{
				throw std::runtime_error("dup2 error");
			}
			// execute the command
			execvp(commands[0], commands);

			// if no error, execl will not return
			// that's: the next codes will not be executes if execl is OK
			// write end of pipe
			close(fd[1]);
			// exit child process
			exit(1);
		}
		// parent
		default:
		{
			// write end of pipe
			close(fd[1]);
			char mode[3] = {'r', 't', 0};
			if (isBin)
			{
				mode[2] = 'b';
			}
			// get io stream from file descriptor
			if ((pp = fdopen(fd[0], mode)) == 0)
			{
				throw std::runtime_error("fdopen error");
			}
		}
	}		
}

void CShell::cclose() throw (std::runtime_error)
{
	if (!isOpen())
	{
		return;
	}
	kill(pid, SIGKILL);
//	if (kill(pid, 0) != -1)
//	{
//		// command process does not terminate, kill it
//		if (kill(pid, SIGKILL) == -1)
//		{
//			throw std::runtime_error("kill child process error");
//		}
//		else
//		{
//			printf("kill %d ok %d\n", pid, SIGKILL);
//		}
//		printf("mypid %d\n", getpid());
//	}

	// read end of pipe
	if (close(fileno(pp)) == -1)
	{
		throw std::runtime_error("close error");
	}

	pp = 0;

	// destory zombie child process
	wait(0);
}

char* CShell::cgets(char* str, int n)// throw (std::runtime_error)
{
	// get char string from pipe
	char* re = fgets(str, n, pp);
//	if (ferror(pp))
//	{
//		throw std::runtime_error("fgets error");
//	}

	return re;
}

bool CShell::isOpen()
{
	return pp != 0;
}

void CShell::initCommand(const std::string command, const bool isBin)
{
	pp = 0;
	this->isBin = isBin;

	commands = new char*[4];
	commands[0] = new char[3];
	strcpy(commands[0], "sh");
	commands[1] = new char[3];
	strcpy(commands[1], "-c");
	commands[2] = new char[command.length()+1];
	strcpy(commands[2], command.c_str());
	commands[3] = (char*)0;
}

void CShell::initCommand(const std::string commands[], const int length, const bool isBin)
{
	pp = 0;
	this->isBin = isBin;

	this->commands = new char*[length+1];
	for (int i=0; i<length; i++)
	{
		this->commands[i] = new char[commands[i].length()+1];
		strcpy(this->commands[i], commands[i].c_str());
	}
	this->commands[length] = (char*)0;
}	

void CShell::deleteCommand()
{
	if (commands != 0)
	{
		int i = 0;
		while (commands[i] != 0)
		{
			delete commands[i++];
		}
		delete[] commands;
	}
}
