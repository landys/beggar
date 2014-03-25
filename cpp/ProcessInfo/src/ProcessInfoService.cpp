#include "zju_beggar_service_ProcessInfoService.h"
#include "ProcessInfo.h"
#include <vector>
using std::vector;

JNIEXPORT jobjectArray JNICALL Java_zju_beggar_service_ProcessInfoService_getProcessInformationNative
  (JNIEnv *env, jobject cls)
{
	jobjectArray result;

	ProcessInfo* pProcessInfo = ProcessInfo::getInstance();

	vector<vector<string> > vvStr = pProcessInfo->getProcessPrimaryInformation();
	
	jclass stringArrClass = env->FindClass("[Ljava/lang/String;");
	if (stringArrClass == 0)
	{
		return 0; // exception throw
	}

	result = env->NewObjectArray(vvStr.size(), stringArrClass, 0);
	if (result == 0)
	{
		return 0; // out of memory error throw
	}

	for(unsigned int i = 0; i < vvStr.size(); i++)
	{
		jclass jniStr = env->FindClass("java/lang/String");
		jobjectArray sarr = env->NewObjectArray(vvStr[i].size(), jniStr, 0);
		if(sarr == 0)
		{
			return 0; // out of memory error thrown
		}

		for(unsigned int j = 0; j < vvStr[i].size(); j++)
		{
			env->SetObjectArrayElement(sarr, j, env->NewStringUTF(vvStr[i].at(j).c_str()));
		}

		env->SetObjectArrayElement(result, i, sarr);
	}
	
	return result;
}
