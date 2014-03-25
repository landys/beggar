/**
 * 
 */
package zju.beggar.view;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import zju.beggar.util.BeggarConstant;
import zju.beggar.util.BeggarException;

/**
 * singleton
 * 
 * @author Tony
 * 
 */
public class XmlLayoutView
{
	private static XmlLayoutView xmlView = null;

	private HashMap<String, BeggarTab> beggarTabs = null;

	private int count = 0;

	private XmlLayoutView()
	{
	}

	/**
     * @return singleton objec of class XmlLayoutView
     */
	public static XmlLayoutView getInstance()
	{
		if (xmlView == null)
		{
			xmlView = new XmlLayoutView();
		}
		return xmlView;
	}

	/**
     * @return all tabs hashmap
     */
	public HashMap<String, BeggarTab> getAllBeggarTabs()
	{
		return beggarTabs;
	}

	/**
     * @param tabId -- tab id specified in XML file
     * @return particular BeggarTab specified by id
     */
	public BeggarTab getBeggarTab(String tabId)
	{
		return beggarTabs.get(tabId);
	}

	public void xml2Layout(JTabbedPane parent) throws BeggarException
	{
		beggarTabs = new HashMap<String, BeggarTab>();

		// find the layout file
		InputStream in = getClass().getResourceAsStream(
			BeggarConstant.layoutFile);
		if (in == null)
		{
			in = getClass().getClassLoader().getResourceAsStream(
				BeggarConstant.layoutFile);
			if (in == null)
			{
				return;
			}
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			// get DocumnetBuilder from DocumentBuilderFactory for multi-platform problem
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);

			// get all "tabItem" elements
			NodeList nl = doc.getElementsByTagName("tabItem");
			for (int i = 0; i < nl.getLength(); i++)
			{
				// create a BeggarTab
				final BeggarTab tabItem = new BeggarTab();

				// get attributes of the BeggarTab item
				NamedNodeMap nnm = nl.item(i).getAttributes();

				// put BeggarTab item in Hashtable beggarTabs with id as key
				String tabId = null;
				Node node = nnm.getNamedItem("id");
				if (node != null)
				{
					tabId = node.getNodeValue();
				}
				else
				{
					tabId = "tabItem" + (count++);
				}
				beggarTabs.put(tabId, tabItem);

				String text = "";
				// set BeggarTab's property text
				node = nnm.getNamedItem("text");
				if (node != null)
				{
					text = node.getNodeValue();
				}
				
				parent.add(text, tabItem);
				tabItem.setSize(parent.getSize().width-2-3, parent.getSize().height-27-3);

				// set related action class of BeggarTab
				node = nnm.getNamedItem("class");
				ITabAction action = null;
				if (node != null)
				{
					try
					{
						String actionName = node.getNodeValue();
						Class actionClass = null;
						try
						{
							actionClass = Class.forName(actionName);
						}
						catch (ClassNotFoundException e)
						{
							actionClass = Class
								.forName(BeggarConstant.viewPackage + "."
									+ actionName);
						}

						// new the object of the action class
						action = (ITabAction) actionClass.newInstance();

						// save the object in BeggarTab
						tabItem.setIAction(action);
					}
					catch (ClassNotFoundException e)
					{
						// TODO some log
						e.printStackTrace();
					}
					catch (InstantiationException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// controls in CTabItem
				NodeList nlCtrl = nl.item(i).getChildNodes();
				// Add controls to BeggarItem recursively
				addComponents(nlCtrl, tabItem.getPanel(), tabItem);

				if (action != null)
				{
					// call to initialize the tab, i.e. data in table
					action.onInit();

					// call to destroy something before the tab is disposed
//					tabItem.addDisposeListener(new DisposeListener()
//					{
//						public void widgetDisposed(DisposeEvent e)
//						{
//							BeggarTab tab = (BeggarTab) e.widget;
//							tab.getIAction().onDispose();
//						}
//					});
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException(
				"Error: Cannot create XML DocumentBuilder.");
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Wrong in XML parsing.");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: IO exception.");
		}
		catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Wrong number format in string.");
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Security Violation.");
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Illegal argument.");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Unknown exception.");
		}
	}

	/**
     * Add controls to BeggarItem recursively
     * 
     * @param nlCtrl -- a list of controls to be added represented as XML nodes
     * @param tabItem -- BeggarTab to hold controls
     * @throws BeggarException
     */
	private void addComponents(final NodeList nlCtrl, final JComponent parent, final BeggarTab tabItem) throws BeggarException
	{
		for (int j = 0; j < nlCtrl.getLength(); j++)
		{
			Node nodeCtrl = nlCtrl.item(j);
			if (nodeCtrl.getNodeType() != Node.ELEMENT_NODE)
			{
				// continue if the node type is not element
				continue;
			}

			// properties of a control, used for adding control
			final HashMap<String, Object> props = new HashMap<String, Object>();
			// HashMap<String, Class> propsType = new HashMap<String, Class>();
			// listener of a control
			final Properties listeners = new Properties();
			final LocationSize locSize = new LocationSize();

			props.put("parent", parent);
			props.put("type", BeggarConstant.widgetClasses.getProperty(nodeCtrl
				.getNodeName()));
			NamedNodeMap nnmCtrl = nodeCtrl.getAttributes();
			try
			{
				for (int k = 0; k < BeggarConstant.locationType.length; k++)
				{
					Node node = nnmCtrl
						.getNamedItem(BeggarConstant.locationType[k]);
					if (node != null)
					{
						String value = node.getNodeValue();
						String[] values = value.split(";");
						LocationSize.class.getField(BeggarConstant.locationType[k])
							.set(
								locSize,
								new Point(Integer.parseInt(values[0]),
									Integer.parseInt(values[1])));
						nnmCtrl.removeNamedItem(BeggarConstant.locationType[k]);
					}
				}
			}
			catch (NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BeggarException("Error: No such field.");
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BeggarException("Error: No access right of method.");
			}
			props.put("locationSize", locSize);

			for (int k = 0; k < nnmCtrl.getLength(); k++)
			{
				Node node = nnmCtrl.item(k);
				String nodeName = node.getNodeName();
				if (nodeName.endsWith("Listener")
					|| nodeName.endsWith("listener"))
				{
					// replace "listener" with "Listener" is neccessary
					if (nodeName.endsWith("listener"))
					{
						nodeName = nodeName.substring(0, nodeName.length()
							- "listener".length())
							+ "Listener";
					}
					listeners.put(nodeName, node.getNodeValue());
				}
				else
				{
					props.put(nodeName, node.getNodeValue());
				}
			}

			// Add the control
			JComponent ctrl = null;
			if (listeners.size() > 0)
			{
				ctrl = tabItem.addComponent(props, listeners);
			}
			else
			{
				ctrl = tabItem.addComponent(props);
			}

			// Add controls to BeggarItem recursively
			NodeList nlChildCtrl = nodeCtrl.getChildNodes();
			if (nlChildCtrl.getLength() > 0 && ctrl instanceof JComponent)
			{
				addComponents(nlChildCtrl, (JComponent) ctrl, tabItem);
			}
		}
	}
}
