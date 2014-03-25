/**
 * 
 */
package zju.beggar.view;

import java.awt.Point;

/**
 * @author Administrator
 *
 */
public class LocationSize
{
	// Point.x means *%, Point.y means *
	public Point left;
	public Point right;
	public Point top;
	public Point bottom;
	
	public LocationSize()
	{
		left = null;
		right = null;
		top = null; 
		bottom = null;
	}
	
	public LocationSize(Point left, Point top, Point right, Point bottom)
	{
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
}
