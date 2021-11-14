/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;
import java.util.HashMap;
import javax.swing.table.AbstractTableModel;


public class DijikstraModel extends AbstractTableModel
{
    private final HashMap<String,Double> l;
    private final HashMap<String,String> p;

    public DijikstraModel(HashMap<String, Double> l, HashMap<String, String> p)
    {
        this.l = l;
        this.p = p;
    }

    @Override
    public int getRowCount()
    {
        return 2;
    }

    @Override
    public int getColumnCount()
    {
        return l.size();
    }

    @Override
    public String getColumnName(int column)
    {
        return (String)l.keySet().toArray()[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(rowIndex==0)
        {
            return l.get(getColumnName(columnIndex));
        }else
        {
            return p.get(getColumnName(columnIndex));
        }
    }
}