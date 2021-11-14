/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


public class MatriceAdjModel extends AbstractTableModel
{
    
    private int[][] matrice;
    private ArrayList<Sommet> sommets;

    public int[][] getMatrice()
    {
        return matrice;
    }

    public void setMatrice(int[][] matrice)
    {
        this.matrice = matrice;
    }

    public ArrayList<Sommet> getSommets()
    {
        return sommets;
    }

    public void setSommets(ArrayList<Sommet> sommets)
    {
        this.sommets = sommets;
    }
    
    

    public MatriceAdjModel(int[][] matrice,ArrayList<Sommet> sommets)
    {
        this.matrice = matrice;
        this.sommets = sommets;
    }
    
    @Override
    public int getRowCount()
    {
        return this.matrice.length;
    }

    @Override
    public int getColumnCount()
    {
        return getRowCount()+1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if(columnIndex==0)return sommets.get(rowIndex).getLabel();
        return this.matrice[rowIndex][columnIndex-1];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        //super.setValueAt(aValue, rowIndex, columnIndex);
        //if((int) aValue!=0 && (int) aValue!=1)return;
        this.matrice[rowIndex][columnIndex-1] = (int) aValue;
    }

    @Override
    public Class getColumnClass(int columnIndex)
    {
        if(columnIndex==0)
        	return String.class;
        return Integer.class;
    }

    @Override
    public String getColumnName(int column)
    {
        if(column==0)
        	return "";
        return sommets.get(column-1).getLabel();
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex!=0;
    }
    
}
