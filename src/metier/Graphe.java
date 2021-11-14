/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import algos.BFS;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import theoriegraphes.Canvas;
import theoriegraphes.ResultatCourtChemin;
import theoriegraphes.MainFrame;


public class Graphe implements Cloneable
{
    private Graphe preInstance;
    private ArrayList<Sommet> sommets = new ArrayList<>();
    private ArrayList<Arret> arrets = new ArrayList<>();

    public int[][] matriceAdj()
    {
        int[][] matrice = new int[sommets.size()][sommets.size()];
        for (Arret ar : arrets) 
        {
            if(Configuration.oriente)
            	matrice[sommets.indexOf(ar.getSommetA())][sommets.indexOf(ar.getSommetB())]=1;
            else
            {
            	if(sommets.indexOf(ar.getSommetA()) < 0 || sommets.indexOf(ar.getSommetB()) < 0) continue;
                matrice[sommets.indexOf(ar.getSommetA())][sommets.indexOf(ar.getSommetB())]=1;
                matrice[sommets.indexOf(ar.getSommetB())][sommets.indexOf(ar.getSommetA())]=1;
            }
        }
        return matrice;
    }

    public Graphe getPreInstance()
    {
        return preInstance;
    }

    public void setPreInstance(Graphe preInstance)
    {
        this.preInstance = preInstance;
    }
    
    public ArrayList<Sommet> getSommets()
    {
        return sommets;
    }

    public void setSommets(ArrayList<Sommet> sommets)
    {
        this.sommets = sommets;
    }

    public ArrayList<Arret> getArrets()
    {
        return arrets;
    }

    public void setArrets(ArrayList<Arret> arrets)
    {
        this.arrets = arrets;
    }
    
    public Sommet addSommet(int x, int y)
    {
        Rectangle rec = new Rectangle(x, y, Configuration.taille_sommet*2, Configuration.taille_sommet*2);
        for (Sommet s : sommets)
        {
            if(s.getBounds().intersects(rec))
            	return null;
        }
        
        String label = null;
        boolean verification = true;
        while(verification)
        {
            verification = false;
            label = JOptionPane.showInputDialog("Label pour le sommet");
            if(label==null)
            	return null;
            if(label.isEmpty())
            {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), "Vous devez saisi une chaine non vide!!!", "Attention!!", JOptionPane.WARNING_MESSAGE);
                verification = true;
                continue;
            }
            for(Sommet s:sommets)
            {
                if(s.getLabel().equals(label))
                {
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), "Label deja saisi", "Attention!!", JOptionPane.WARNING_MESSAGE);
                    verification = true;
                    break;
                }
            }
        }
        
        Sommet s = new Sommet(label, Configuration.coleur_label, Configuration.coleur_sommet, x, y);
        sommets.add(s);
        return s;
    }
    public void addArret(Sommet a, Sommet b)
    {
        if(Configuration.oriente)
        {
            for (Arret ar : arrets)
            {
                if(ar.getSommetA()==a && ar.getSommetB()==b)
                {
                	//supprimer arret modify
                    arrets.remove(ar);
                    return;
                }
            }
        }else
        {
            for (Arret ar : arrets)
            {
                if((ar.getSommetA()==a && ar.getSommetB()==b) || (ar.getSommetA()==b && ar.getSommetB()==a))
                {
                    arrets.remove(ar);
                    return;
                }
            }
        }
        boolean pondere_positive=true;
        for(Arret ar:arrets)
        {
            if(ar.getCout()<0)
            {
                pondere_positive=false;
                break;
            }
        }
        Configuration.pondere_positive = pondere_positive;
        Arret ar = null;
        if(Configuration.pondere && a!=b)
        {
            double cout = 0.0;
            while(cout==0.0)
            {
                try
                {
                    cout = Double.valueOf(JOptionPane.showInputDialog("Cout de cet arret (zero non acceptable!!)"));
                }catch(NumberFormatException e)
                {
                    cout = 0;
                }
            }
            if(cout<0)
            	Configuration.pondere_positive = false;
            
            ar = new Arret(a, b,Configuration.coleur_arret, cout);
        }else
        {
            ar = new Arret(a, b,Configuration.coleur_arret);
        }
        arrets.add(ar);
    }
    public Arret addArretWireshall(Sommet a, Sommet b)
    {
        Arret ar = null;
        //si l'arret est dèja existant
        if(Configuration.oriente)
        {
            for(Arret arr : arrets)
            {
                if(arr.getSommetA()==a && arr.getSommetB()==b)
                	return ar;
            }
        }else
        {
            for(Arret arr : arrets)
            {
                if((arr.getSommetA()==a && arr.getSommetB()==b) || (arr.getSommetA()==b && arr.getSommetB()==a))
                	return ar;
            }
        }
        
        //creation de l'arret
        if(Configuration.pondere && a!=b)
        {
            ar = new Arret(a, b, Configuration.coleur_arret, Configuration.default_cout);
        }else
        {
            ar = new Arret(a, b,Configuration.coleur_arret);
        }
        
        ar.setDashed(true);
        arrets.add(ar);
        return ar;
    }
    
    public void draw(Graphics2D g2d)
    {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        arrets.forEach((arret) ->{
        	arret.draw(g2d);
        });
        
        sommets.forEach((sommet) -> {
            sommet.draw(g2d);
        });
        
        arrets.forEach((arret) -> {
            arret.drawLabel(g2d);
        });
        MainFrame.label_v.setText(getV()+"");
        MainFrame.label_e.setText(getE()+"");
        MainFrame.label_d.setText(getDensite()+"");
        if(Configuration.current_algo!=null)
        {
            MainFrame.console.setText(Configuration.current_algo.getTrace().toString());
            MainFrame.btn_exporter_pdf.setEnabled(true);
        }else
        {
            MainFrame.console.setText("");
            MainFrame.btn_exporter_pdf.setEnabled(false);
        }
        Configuration.checkAlgos();
    }
    
    public Rectangle getBounds()
    {
        int min_x=Canvas.getInstance().getBounds().width,
            max_x=0,
            min_y=Canvas.getInstance().getBounds().height,
            max_y=0;
        
        for (Sommet s : sommets)
        {
            if(s.getPosition_x()>max_x)max_x = s.getPosition_x();
            if(s.getPosition_x()<min_x)min_x = s.getPosition_x();
            if(s.getPosition_y()>max_y)max_y = s.getPosition_y();
            if(s.getPosition_y()<min_y)min_y = s.getPosition_y();
        }
        int x= min_x-Configuration.taille_sommet*2;
        int y= min_y-Configuration.taille_sommet*2;
        int width = (max_x-min_x)+Configuration.taille_sommet*4;
        int height = (max_y-min_y)+Configuration.taille_sommet*4;
        return new Rectangle(x,y,width,height);
    }
    
    public double somme_degrees()
    {
        double degree=0.0;
        for(Sommet s:sommets)
        {
            degree+=degree(s);
        }
        return degree;
    }
    public double degree(Sommet s)
    {
        double degree=0.0;
        ArrayList<Sommet> voisins = getVoisinsWithOrigin(s);
        for(Sommet v:voisins)
        {
            if(s==v)
            	degree+=2;
            else 
            	degree+=1;
        }
        //on affecte la degree au sommet s
        s.setdegree(degree);
        return degree;
    }

    public Sommet getSommetAtPosition(int x, int y)
    {
        for (Sommet s : sommets)
        {
            if(s.getBounds().contains(x, y))return s;
        }
        return null;
    }

    public void removeSommet(Sommet selectionne)
    {
        for (Iterator<Arret> iterator = arrets.iterator(); iterator.hasNext();)
        {
            Arret next = iterator.next();
            if(next.getSommetA()==selectionne || next.getSommetB()==selectionne)
            	iterator.remove();
        }
        sommets.remove(selectionne);
    }
    private int indexOf(Sommet s1, ArrayList<Sommet> sommets)
	 {
		for(int i=0;i<sommets.size();i++)
		{
			if(sommets.get(i).getLabel().equals(s1.getLabel()))
				return i;
		}
		return -1;
	 }
    public ArrayList<Sommet> getVoisinsWithOrigin(Sommet s)
    {
        ArrayList<Sommet> liste = new ArrayList<>();
        int i = indexOf(s,sommets);
        int[][] m = matriceAdj();
        for (int j = 0; j < m[i].length; j++)
        {          
            if(m[i][j]==1)
            	liste.add(sommets.get(j));
        }
        Collections.sort(liste);
        return liste;
    }
    
    public ArrayList<Sommet> getVoisins(Sommet s)
    {
        ArrayList<Sommet> liste = new ArrayList<>();
        int i = indexOf(s,sommets);
        int[][] m = matriceAdj();
        for (int j = 0; j < m[i].length; j++)
        {          
            if(m[i][j]==1 && i!=j)
            	liste.add(sommets.get(j));
        }
        Collections.sort(liste);
        return liste;
    }
    public ArrayList<Sommet> getVoisinsNonVisites(Sommet s, ArrayList<String> visites)
    {
        ArrayList<Sommet> liste = new ArrayList<>();
        int i = sommets.indexOf(s);
        int[][] m = matriceAdj();
        for (int j = 0; j < m[i].length; j++)
        {          
            if(m[i][j]==1 && i!=j && !visites.contains(sommets.get(j).getLabel()))
            	liste.add(sommets.get(j));
        }
        Collections.sort(liste);
        return liste;
    }
    public ArrayList<Sommet> getVoisinsEntrants(Sommet s)
    {
        ArrayList<Sommet> liste = new ArrayList<>();
        if(Configuration.oriente)
        {
            for (Arret ar : arrets)
            {
                if(ar.getSommetB()==s)
                	liste.add(ar.getSommetA());
            }
        }else
        {
            for (Arret ar : arrets)
            {
                if(ar.getSommetB()==s)
                	liste.add(ar.getSommetA());
                else if(ar.getSommetA()==s)
                	liste.add(ar.getSommetB());
            }
        }
        Collections.sort(liste);
        return liste;
    }
    public ArrayList<Sommet> getVoisinsSortants(Sommet s)
    {
        ArrayList<Sommet> liste = new ArrayList<>();
        if(Configuration.oriente)
        {
            for (Arret ar : arrets)
            {
                if(ar.getSommetA()==s)
                	liste.add(ar.getSommetB());
            }
        }else
        {
            for (Arret ar : arrets)
            {
                if(ar.getSommetB()==s)
                	liste.add(ar.getSommetA());
                else if(ar.getSommetA()==s)
                	liste.add(ar.getSommetB());
            }
        }
        Collections.sort(liste);
        return liste;
    }
    public Arret getMinVoisin(Sommet r,ArrayList<Sommet> visites)
    {
        double min = Double.MAX_VALUE;
        Arret min_ar = null;
        if(Configuration.oriente)
        {
            for(Arret ar:arrets)
            {
                if(ar.getSommetA()==r && (!visites.contains(ar.getSommetB())) && ar.getCout()<min)
                {
                    min_ar = ar;
                    min = ar.getCout();
                }
            }
        }else
        {
            for(Arret ar:arrets)
            {
                if((ar.getSommetA()==r || ar.getSommetB()==r) && ar.getCout()<min)
                {
                    if(ar.getSommetA()==r && (!visites.contains(ar.getSommetB())))
                    {
                        min_ar = ar;
                        min = ar.getCout();                        
                    }
                    if(ar.getSommetB()==r && (!visites.contains(ar.getSommetA())))
                    {
                        Sommet tmp = ar.getSommetB();
                        ar.setSommetB(ar.getSommetA());
                        ar.setSommetA(tmp);
                        min_ar = ar;
                        min = ar.getCout();
                    }
                }
            }
        }
        return min_ar;
    }
    public Arret getArret(Sommet a,Sommet b)
    {
        if(Configuration.oriente)
        {
            for(Arret ar:arrets)
            {
                if(ar.getSommetA()==a && ar.getSommetB()==b)
                	return ar;
            }
        }else
        {
            for(Arret ar:arrets)
            {
                if((ar.getSommetA()==a && ar.getSommetB()==b)||(ar.getSommetB()==a && ar.getSommetA()==b))
                	return ar;
            }
        }
        return null;
    }
    public Stack<Sommet> bfs(Sommet r)
    {
        Stack<Sommet> f = new Stack<>();
        Stack<Sommet> m = new Stack<>();
        f.push(r);
        m.push(r);
        while(!f.empty())
        {
            Sommet s = f.remove(0);
            for (Sommet t : getVoisins(s))
            {
                if(!m.contains(t))
                {
                    f.push(t);
                    m.push(t);
                }
            }
        }
        return m;
    }
    public boolean detectBoucle()
    {
        for(Sommet s:sommets)
        {
            for(Sommet v:getVoisins(s))
            {
                Arret tmp = getArret(s, v);
                arrets.remove(tmp);
                if(bfs(v).contains(s))
                {
                    arrets.add(tmp);
                    return true;
                }
                arrets.add(tmp);
            }
        }
        return false;
    }
    public void repaint()
    {
        for (Sommet s : sommets)
        {
            s.setCouleur(Configuration.coleur_sommet);
            s.setCouleurLabel(Configuration.coleur_label);
        }
        for(Arret ar : arrets)
        {
            ar.setCouleur(Configuration.coleur_arret);
        }
        Canvas.getInstance().repaint();
    }
    public double getV()
    {
        return sommets.size();
    }
    public double getE()
    {
        return arrets.size();
    }
    //check for orienté
    public double getDensite()
    {
        if(getV()<2)
        	return 0.0;
        else return somme_degrees()/(getV()*(getV()-1));
    }
    public double getACM()
    {
        double acm = 0.0;
        for(Arret ar : arrets)
        {
            acm+=ar.getCout();
        }
        return acm;
    }
    
    private ArrayList<Chemin> chemins;
    public ArrayList<Chemin> getChemins(Sommet depart, Sommet dest)
    {
        chemins = new ArrayList<>();
        rec(depart,dest,dest,null);
        for (Iterator<Chemin> it = chemins.iterator(); it.hasNext();)
        {
            Chemin ch = it.next();
            if(ch.getSommets().get(0)!=depart)
            	it.remove();
        }
        return chemins;
    }
    
    public void rec(Sommet depart, Sommet dest,Sommet last, Chemin c)
    {
        ArrayList<Sommet> voisinsEntrants = getVoisinsEntrants(last);
        if((last==depart || voisinsEntrants.isEmpty()) && c!=null)
        {
            if(c.getSommets().size()>0)
            {
                double cout_ajoute = getArret(last, c.getSommets().get(0)).getCout();
                c.setCout(c.getCout()+cout_ajoute);
                c.getCouts().add(cout_ajoute);
            }
            c.addSommet(last);
            chemins.add(c);
        }
        else
        {
            if(c!=null && c.getSommets().contains(last))
            	return;
            for(Sommet v:voisinsEntrants)
            {
                Chemin new_c = new Chemin(c);
                if(new_c.getSommets().size()>0)
                {
                    double cout_ajoute = getArret(last, c.getSommets().get(0)).getCout();
                    new_c.setCout(new_c.getCout()+cout_ajoute);
                    new_c.getCouts().add(cout_ajoute);
                }
                new_c.addSommet(last);
                rec(depart,dest,v,new_c);
            }
        }
    }
    
    public Sommet getS()
    {
        for (Sommet s : sommets)
        {
            if(getVoisinsEntrants(s).isEmpty())
            	return s;
        }
        return null;
    }
    public Sommet getP()
    {
        for (Sommet s : sommets)
        {
            if(getVoisinsSortants(s).isEmpty())
            	return s;
        }
        return null;
    }

    
    public Arret addArret(Sommet get, Sommet get0, Double epsi)
    {
        Arret ar = new Arret(get, get0, Configuration.coleur_arret, epsi);
        arrets.add(ar);
        return ar;
    }
    public double getFluxMax()
    {
        double flux_max = 0.0;
        Sommet dest = getP();
        for(Sommet v:getVoisinsEntrants(dest))
        {
            flux_max+=getArret(v, dest).getFlux();
        }
        return flux_max;
    }
}
