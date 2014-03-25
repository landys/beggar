/**
 * 
 */
package zju.beggar.view;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.concurrent.*;

import zju.beggar.service.ProcessInfoService;

/**
 * @author Administrator
 * 
 */
public class ProcessTabAction extends TabActionAdapter
{
	public ProcessTabAction()
	{
		// get the controls library
		tabParent = XmlLayoutView.getInstance().getBeggarTab("process");
		procSer = new ProcessInfoService();
		procTableModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;
			public String getColumnName(int col) {
		        return columnName[col].toString();
		    }
		    
		    public int getRowCount() { return processItemValue.length; }
		    public int getColumnCount() { return columnName.length; }
		    public Object getValueAt(int row, int col) 
		    {
		        return processItemValue[row][col];
		    }
		    public boolean isCellEditable(int row, int col)
		    { 
		    	return false; 
		    }
		    public void setValueAt(Object value, int row, int col) 
		    {
		    	processItemValue[row][col] = (String)value;
		        fireTableCellUpdated(row, col);
		    }
		};
	}

	/*
     * @see zju.beggar.view.TabActionAdapter#onInit()
     */
	@Override
	public void onInit()
	{
		// get the process infomation
		String[][] proInfo = procSer.getProcessInformation();

		// get the column names
		columnName = proInfo[0];
		
		// get the column value
		processItemValue = updateItemValue(proInfo);
		
		// get the table widget
		processTable = (JTable)tabParent.getChildComponent("processTable");
		processTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		
		// set the table modal and the sorter
		processTable.setModel(procTableModel);
		sorter = new TableRowSorter<AbstractTableModel>(procTableModel);
		processTable.setRowSorter(sorter);
		processTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		processTable.setFillsViewportHeight(true);
		
		// a new Thread to update the table value
		flashThread = new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					try
					{
						Thread.sleep(2000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					final String[][] tempItemValue = updateItemValue
										(procSer.getProcessInformation());
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	processItemValue = tempItemValue;
			            	procTableModel.fireTableDataChanged();
			            	processTable.revalidate();
			            }
			        });
				}
			}
		});
		
		flashThread.start();
	}
	
	private String[][] updateItemValue(String[][] proInfo)
	{
		// AUTO: add the status label
		String[][] tempItemValue = new String[proInfo.length - 2][];
		
		for(int i = 1; i < proInfo.length - 1; i++)
		{
			tempItemValue[i - 1] = proInfo[i];
		}
		return tempItemValue;
	}
	
	private BeggarTab tabParent;						// 1. the control library
	
	private String[] columnName;						// 2. the column's names
 	private String[][] processItemValue;				// 3. the real process information
 	private JTable processTable;						// 4. the show information table
 	private final AbstractTableModel procTableModel;	// 5. the data modal of the table
 	private TableRowSorter<AbstractTableModel> sorter;	// 6. the column sort
 	
	private ProcessInfoService procSer;					// 7. the jni instance
	private Thread flashThread;							// 8. flash the table thread
}
