#include "CShell.h"
#include "ProcessInfo.h"
#include <iostream>
using namespace std;

int main()
{
//	std::string commands[4] = {"java", "-cp", "f:\\codes\\forEclipse\\HelloSWT\\bin", "tony.helloSWT.TestCShell"};
//	CShell cs(commands, 4);
//	cs.copen();
//	char buf[100];

//	int i=0;
//	while (cs.cgets(buf, 100) && ++i < 5)
//	{
//		printf("good %s", buf);
//	}

//	printf("finish?\n");
	
//	cs.cclose();
	
	ProcessInfo* pi = ProcessInfo::getInstance();
	vector<vector<string> >vt = pi->getProcessPrimaryInformation();
	for(unsigned int i = 0; i < vt.size(); i++)
	{
		vector<string>& strVt = vt[i];

		cout << i << ": ";
		for(unsigned int j = 0; j < strVt.size(); j++)
		{
			cout << j << ":" << strVt[j] << " ";
		}
		cout << endl;
	}

	vector<vector<string> >vt2 = pi->getProcessPrimaryInformation();
	for(unsigned int i = 0; i < vt2.size(); i++)
	{
		vector<string>& strVt = vt2[i];

		cout << i << ": ";
		for(unsigned int j = 0; j < strVt.size(); j++)
		{
			cout << strVt[j] << "    ";
		}
		cout << endl;
	}
	return 0;
}

