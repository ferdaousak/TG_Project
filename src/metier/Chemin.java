/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class Chemin implements Comparable<Chemin>
{
    ArrayList<Sommet> sommets;
    private Double cout;
    private ArrayList<Double> couts;

    public Chemin(Chemin c)
    {
        sommets = new ArrayList<>();
        if(c!=null)
        {
            cout = c.cout;
            couts =new ArrayList<>();
            couts.addAll(c.couts);
            for(Sommet s:c.sommets)
            	sommets.add(s);
        }else
        {
            cout = 0.0;
            couts =new ArrayList<>();
        }
    }

    public Double epsilone()
    {
        return Collections.min(couts);
    }

    public ArrayList<Double> getCouts()
    {
        return couts;
    }

    public void setCouts(ArrayList<Double> couts)
    {
        this.couts = couts;
    }
    
    public double getCout()
    {
        return cout;
    }

    public void setCout(double cout) 
    {
        this.cout = cout;
    }
    
    public ArrayList<Sommet> getSommets()
    {
        return sommets;
    }

    public void setSommets(ArrayList<Sommet> sommets)
    {
        this.sommets = sommets;
    }

    public void addSommet(Sommet s)
    {
        sommets.add(0, s);
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (Iterator<Sommet> it = sommets.iterator(); it.hasNext();)
        {
            Sommet s = it.next();
            sb.append(s.getLabel());
            if(it.hasNext())
            	sb.append("->");
        }
        sb.append(": epsi="+epsilone());
        return sb.toString();
    }

    @Override
    public int compareTo(Chemin o)
    {
        if(epsilone()>o.epsilone())
        	return -1;
        else if(epsilone()<o.epsilone())
        	return 1;
        return 0;
    }
}
