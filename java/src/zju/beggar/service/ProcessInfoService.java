/**
 * 
 */
package zju.beggar.service;

import java.util.ArrayList;

/**
 * @author szh
 *
 */
public class ProcessInfoService
{
	static
	{
		System.loadLibrary("ProcessInfo");
	}
	
	public ProcessInfoService()
	{
		
	}
	
	public String[][] getProcessInformation()
	{		
		return getProcessInformationNative();
	}
	
	private native String[][] getProcessInformationNative();
/*
	public ArrayList<ArrayList<String>> getProcessInformation()
	{
		ArrayList<ArrayList<String>> retInfo = new ArrayList<ArrayList<String>>();
		ArrayList<String> columnName = new ArrayList<String>();

		for (int i = 0; i < 10; i++)
		{
			columnName.add(i + "th column");
		}
		retInfo.add(columnName);

		for (int i = 0; i < 100; i++)
		{
			ArrayList<String> columnValue = new ArrayList<String>();
			for (int j = 0; j < 10; j++)
			{
				columnValue.add(i + ":" + j);
			}
			retInfo.add(columnValue);
		}

		return retInfo;
	}*/
}
