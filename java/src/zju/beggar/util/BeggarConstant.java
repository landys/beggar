/**
 * 
 */
package zju.beggar.util;

import java.util.Properties;

/**
 * @author Tony
 * 
 */
public class BeggarConstant
{
	// swt layout file
	public static final String layoutFile = "BeggarLayout.xml";

	// definite class type for each name of control
	public static final Properties widgetClasses;

	// location type for a control in the composite
	public static final String[] locationType = {"left", "right", "bottom",
		"top"};

	// package of listener class
	public static final String viewPackage = "zju.beggar.view";

	static
	{
		widgetClasses = new Properties();
		widgetClasses.put("tabItem", "javax.swing.JPanel");
		widgetClasses.put("button", "javax.swing.JButton");
		widgetClasses.put("table", "javax.swing.JTable");
		widgetClasses.put("panel", "javax.swing.JPanel");
	}
}
