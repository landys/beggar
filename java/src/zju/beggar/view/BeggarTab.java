package zju.beggar.view;

import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import zju.beggar.util.BeggarConstant;
import zju.beggar.util.BeggarException;
import zju.beggar.util.DataConverters;

/**
 * @author Tony
 * 
 */
public class BeggarTab extends JPanel
{
	private static final long serialVersionUID = -7347096742344567624L;

	// all components in BeggarTab, the key(string) of Hashtable is component id
	private Hashtable<String, JComponent> components = null;

	// component count for components with no id
	private int count = 0;

	// tab action interface with init, ...
	private ITabAction iAction = null;

	public BeggarTab()
	{
		super();
		
		setLayout(null);
		
		// put the Composite in the component Hashtable
		components = new Hashtable<String, JComponent>();
		//components.put("composite", composite);
	}

	/**
     * @return all components hashtable of the tab
     */
	public Hashtable<String, JComponent> getAllComponents()
	{
		return components;
	}

	/**
     * @param componentId -- component id specified in XML file
     * @return child component specified by component id
     */
	public JComponent getChildComponent(String componentId)
	{
		return components.get(componentId);
	}

	public ITabAction getIAction()
	{
		return iAction;
	}

	public void setIAction(ITabAction iAction)
	{
		this.iAction = iAction;
	}
	
	public JComponent getPanel()
	{
		return this;
	}

	/**
     * @param props
     * @return
     * @throws BeggarException
     */
	public JComponent addComponent(HashMap<String, Object> props) throws BeggarException
	{
		return addComponent(props, null, null);
	}

	/**
     * @param props
     * @param listeners
     * @return
     * @throws BeggarException
     */
	public JComponent addComponent(HashMap<String, Object> props, Properties listeners) throws BeggarException
	{
		return addComponent(props, null, listeners);
	}

	/**
     * add a component to the tab item (virtually Composite)
     * 
     * @param props
     * @param propsType
     * @return
     * @throws BeggarException
     */
	public JComponent addComponent(HashMap<String, Object> props, HashMap<String, Class> propsType, Properties listeners) throws BeggarException
	{
		if (props == null)
		{
			return null;
		}

		// the component to be added
		JComponent component = null;
		// the class object of the component to be added
		Class componentClass = null;
		JComponent parent = null;
		// for scroll component
		JScrollPane sp = null;
		
		// Add properties of the component
		try
		{
			// make a class object of the component
			componentClass = Class.forName((String)props.get("type"));
			// remove used property in props
			props.remove("type");
			
			// make an instants
			component = (JComponent) componentClass.newInstance();
			
			if (props.get("parent") != null)
			{
				parent = ((JComponent)props.get("parent"));
				// remove used property in props
				props.remove("parent");
			}
			else
			{
				parent = getPanel();
			}
			if ("true".equalsIgnoreCase((String)props.get("scrolled")))
			{
				sp = new JScrollPane(component);
				parent.add(sp);
			}
			else
			{
				parent.add(component);
			}
			props.remove("scrolled");
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Class not found.");
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: Class cannot be instantiated.");
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BeggarException("Error: No access right of method.");
		}

		// put the component in the Hashtable, with the key id
		String componentId = null;
		if (props.containsKey("id"))
		{
			componentId = (String)props.get("id");
			props.remove("id");
		}
		else
		{
			componentId = "component" + (count++);
		}
		components.put(componentId, component);
		component.setLayout(null);
		
		// get location and size
		LocationSize locSize = null;
		if (props.containsKey("locationSize"))
		{
			locSize = (LocationSize)props.get("locationSize");			
			props.remove("locationSize");
		}
		
		// apply all other properties in props to the component
		for (String key : props.keySet())
		{
			try
			{
				// suffix of getter/setter of Java bean
				// i.e. if key="text", then getter/setter="getText(isText)/setText",
				// and here suffix="Text"
				String suffix = key.substring(0, 1).toUpperCase()
					+ key.substring(1);

				// class object of the parameter of setter method
				Class paraClass = null;
				if (propsType != null && propsType.containsKey(key))
				{
					// the class object is determined by developer
					paraClass = propsType.get(key);
				}
				else
				{
					// to get class object from the return of getter method
					try
					{
						// the getter has the prefix "get"
						paraClass = componentClass.getMethod("get" + suffix,
							(Class[]) null).getReturnType();
					}
					catch (NoSuchMethodException e)
					{
						// TODO log somthing
						// the getter has the prefix "is"
						paraClass = componentClass.getMethod("is" + suffix,
							(Class[]) null).getReturnType();
					}
				}

				Method method = componentClass.getMethod("set" + suffix,
					paraClass);
				Object value = props.get(key);
				if (String.class.equals(value.getClass())
					&& !paraClass.equals(value.getClass()))
				{
					// the value should be converted to a certain type
					// it happens only is value is String, and the paraClass is not String
					// the convertor will be correct only if paraClass has a method "valueOf(String)"
					value = DataConverters.string2Other((String) value,
						paraClass);
				}
				// set the property of the component
				method.invoke(component, value);
			}
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new BeggarException("Error: Security Violation.");
			}

			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new BeggarException("Error: Illegal argument.");
			}

			catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new BeggarException("Error: No such method.");
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new BeggarException("Error: No access right of method.");
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new BeggarException("Error: Exception throws in contruction.");
			}
		}
		
		// set location and size
		// it must be done after all properties set, because of the prefersize
		if (locSize != null)
		{
			Point location = new Point(0, 0);
			Dimension size = component.getPreferredSize();
			Dimension psize = parent.getSize();
			if (locSize.left != null)
			{
				location.x = locSize.left.x * psize.width / 100 + locSize.left.y;
				if (locSize.right != null)
				{
					size.width = locSize.right.x * psize.width / 100 + locSize.right.y - location.x;
				}
			}
			else if (locSize.right != null)
			{
				location.x = locSize.right.x * psize.width / 100 + locSize.right.y - size.width;
			}

			
			if (locSize.top != null)
			{
				location.y = locSize.top.x * psize.height / 100 + locSize.top.y;
				if (locSize.bottom != null)
				{
					size.height = locSize.bottom.x * psize.height / 100 + locSize.bottom.y - location.y;
				}
			}
			else if (locSize.bottom != null)
			{
				location.y = locSize.bottom.x * psize.height / 100 + locSize.bottom.y - size.height;
			}
			if (sp != null)
			{
				sp.setLocation(location);
				sp.setSize(size);
			}
			component.setLocation(location);
			component.setSize(size);
		}
		else
		{
			component.setSize(component.getPreferredSize());
		}

		if (listeners == null)
		{
			return component;
		}
		// Add listeners of the components
		for (Enumeration en = listeners.keys(); en.hasMoreElements();)
		{
			String key = (String) en.nextElement();

			// suffix of method name such as add***Listener of Java objects
			// i.e. if key="mouseListener", then the method is "addMouseListener",
			// here suffix="MouseListener"
			String suffix = key.substring(0, 1).toUpperCase()
				+ key.substring(1);

			try
			{
				// get listener class defined by developer
				String listenerName = listeners.getProperty(key);
				Class listenerClass = null;
				Object listener = null;

				// find the listener class in the ITabAction class first
				if (iAction != null)
				{
					Class[] classes = iAction.getClass().getClasses();
					for (int i = 0; i < classes.length; i++)
					{
						if (listenerName.equals(classes[i].getName())
							|| listenerName.equals(classes[i].getSimpleName()))
						{
							listenerClass = classes[i];
							break;
						}
					}
					try
					{
						try
						{
							listener = listenerClass.newInstance();
						}
						catch (Exception e)
						{
							Constructor listenerCon = listenerClass
								.getConstructor(iAction.getClass());
							listener = listenerCon.newInstance(iAction);
						}
					}
					catch (Exception e)
					{
						// TODO logs
					}
				}

				if (listenerClass == null)
				{
					try
					{
						// use the absolute class name to find the listener class
						listenerClass = Class.forName(listenerName);
						listener = listenerClass.newInstance();
					}
					catch (ClassNotFoundException e)
					{
						// find the listener class in package BeggarConstant.viewPackage
						listenerClass = Class
							.forName(BeggarConstant.viewPackage + "."
								+ listenerName);
						listener = listenerClass.newInstance();
					}
				}

				// get the right add***Listener method
				Method method = componentClass.getMethod("add" + suffix, Class
					.forName("org.eclipse.swt.events." + suffix));
				// invoke the method to add listener
				method.invoke(component, listener);
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InstantiationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return component;
	}
}
