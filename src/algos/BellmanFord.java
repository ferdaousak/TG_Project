/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Arret;
import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;
import theoriegraphes.MainFrame;


public class BellmanFord extends Algorithme{
    private final ArrayList<Sommet> sommets;
    private final ArrayList<Arret> arrets;
    private Sommet depart;
    //longueur
    private HashMap<String,Double> l;
    //predecesseur
    private HashMap<String,String> p;

    public BellmanFord(Graphe g, Sommet depart)
    {
        super("Bellman Ford", g);
        this.sommets = g.getSommets();
        this.arrets = g.getArrets();
        this.l = new HashMap<>();
        this.p = new HashMap<>();
        this.depart = depart;
    }

    @Override
    //initialisation de l'algorithme
    protected void preRun()
    {
        Configuration.resetImages();
        //prendre screenshot
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: ").append(nom).append(" Debut\n");
        //on trie les sommets par ordre alphanumérique
        Collections.sort(sommets);
        for(Sommet s:sommets)
        {
            if(depart==s)
            {
                l.put(s.getLabel(), 0.0d);
                p.put(s.getLabel(), null);
            }else
            {
                l.put(s.getLabel(), Double.POSITIVE_INFINITY);
                p.put(s.getLabel(), null);
            }
        }
        trace.append("--iteration: 0\n");
        trace.append("    l = ").append(l).append("\n");
        trace.append("    p = ").append(p).append("\n");
        
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }

    @Override
    public void run()
    {
        preRun();
        for(iteration=1;iteration<sommets.size();iteration++){
            try 
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(BellmanFord.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("--iteration: ").append(iteration).append("\n");
            for (Arret ar : arrets) 
            {
            	//l(u) + l(u,v)
                double new_cout = l.get(ar.getSommetA().getLabel())+ar.getCout();
                if( l.get(ar.getSommetB().getLabel()) > new_cout)
                {
                    l.put(ar.getSommetB().getLabel(), new_cout);
                    p.put(ar.getSommetB().getLabel(), ar.getSommetA().getLabel());
                }
            }
            trace.append("    l = ").append(l).append("\n");
            trace.append("    p = ").append(p).append("\n");
            Canvas.getInstance().repaint();
            Canvas.getInstance().screenShot();
        }
        postRun();
    }
    
    @Override
    protected void postRun()
    {
        trace.append("--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("Algorithme: ").append(nom).append(" Fin.\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
        new theoriegraphes.ResultatCourtChemin(MainFrame.getInstance(), depart, l, p).setVisible(true);
    }
}
