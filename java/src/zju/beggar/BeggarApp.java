package zju.beggar;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import zju.beggar.util.BeggarException;
import zju.beggar.view.BeggarTab;
import zju.beggar.view.ITabAction;
import zju.beggar.view.XmlLayoutView;

/**
 * @author Tony
 * Good file
 */
public class BeggarApp
{
	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		BeggarApp app = new BeggarApp();
		app.show();
	}
	
	protected void show()
	{
		JFrame frame = new JFrame("Beggar System Manager");
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		
		frame.setSize(410, 540);
		container.setSize(frame.getSize().width-4-4, frame.getSize().height-23-4);
		
		createMainMenu(container);
		createMainComp(container);
		createStatusbar(container);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
	}

	private void createMainMenu(Container parent)
	{
	}

	private void createMainComp(Container parent)
	{
		final JTabbedPane tabbedPane = new JTabbedPane();
		parent.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.setSize(parent.getSize());
		
		tabbedPane.getModel().addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				SingleSelectionModel model = (SingleSelectionModel) e.getSource();
				ITabAction action = ((BeggarTab)tabbedPane.getComponentAt(model.getSelectedIndex())).getIAction();
				if (action != null)
				{
					action.onSelect();
				}
			}
			
		});
//		mainComp = new Composite(parent, SWT.BORDER);
//		mainComp.setLayoutData(new GridData(GridData.FILL_BOTH));
//		mainComp.setLayout(new FillLayout());
//
//		tabbedPane = new CTabFolder(mainComp, SWT.CLOSE | SWT.MULTI | SWT.BORDER);
//		tabbedPane.addSelectionListener(new SelectionListener()
//		{
//			public void widgetDefaultSelected(SelectionEvent e)
//			{
//				widgetSelected(e);
//			}
//
//			public void widgetSelected(SelectionEvent e)
//			{
//				BeggarTab tab = (BeggarTab) e.item;
//				if (tab.getIAction() != null)
//				{
//					tab.getIAction().onSelect(e);
//				}
//			}
//		});

		try
		{
			// Do layout from xml
			XmlLayoutView.getInstance().xml2Layout(tabbedPane);
		}
		catch (BeggarException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createStatusbar(Container parent)
	{
//		statusbar = new Composite(shell, SWT.BORDER);
//		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
//		gridData.heightHint = 19;
//		statusbar.setLayoutData(gridData);
//
//		RowLayout layout = new RowLayout();
//		layout.marginLeft = layout.marginTop = 0;
//		statusbar.setLayout(layout);
//
//		Label statusbarLabel = new Label(statusbar, SWT.BORDER);
//		statusbarLabel.setLayoutData(new RowData(70, 19));
	}
}
