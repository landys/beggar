/**
 * 
 */
package zju.beggar.view;


/**
 * @author Tony
 *
 */
public class CpuTab implements ITabAction 
{
    private BeggarTab bt = XmlLayoutView.getInstance().getBeggarTab("cpu");
    
    public void onDispose()
    {
	System.out.println("public void onDispose()");
    }

    public void onInit()
    {
	System.out.println("public void onInit()");
    }
    
//    public class TestListener extends MouseAdapter
//    {
//        @Override
//        public void mouseDown(MouseEvent e)
//	{
//	    Table table = (Table) bt.getChildControl("processTable");
//	    System.out.println(bt.getAllControls().size());
//	    System.out.println(table.getColumnCount());
//	    table.setItemCount(100);
//	    System.out.println(table.getItemCount());
//	    TableItem ti = table.getItem(1);
//	    ti.setText("Hello World!");
//	}
//    }

    public void onSelect()
    {
	System.out.println("public void onSelect()");
	
    }
}
