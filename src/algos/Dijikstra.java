/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Arret;
import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;
import theoriegraphes.MainFrame;


public class Dijikstra extends Algorithme
{
    private final ArrayList<Sommet> sommets;
    private final ArrayList<Arret> arrets;
    private Sommet depart;
    private final ArrayList<String> s;
    private final ArrayList<String> t;
    private HashMap<String,Double> l;
    private HashMap<String,String> p;
    
    public Dijikstra(Graphe g,Sommet depart)
    {
        super("Dijikstra", g);
        this.sommets = g.getSommets();
        this.arrets = g.getArrets();
        this.s = new ArrayList<>();
        this.t = new ArrayList<>();
        this.l = new HashMap<>();
        this.p = new HashMap<>();
        this.depart = depart;
    }

    @Override
    protected void preRun()
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: ").append(nom).append(" Debut\n");
        Collections.sort(sommets);
        
        s.add(depart.getLabel());
        for(Sommet ss:sommets)
        {
            if(depart==ss)
            {
                l.put(ss.getLabel(), 0.0d);
                p.put(ss.getLabel(), null);
            }else
            {
                t.add(ss.getLabel());
                Arret ar = g.getArret(depart, ss);
                if(ar!=null)
                {
                    l.put(ss.getLabel(), ar.getCout());
                    p.put(ss.getLabel(), depart.getLabel());
                }else
                {
                    l.put(ss.getLabel(), Double.POSITIVE_INFINITY);
                    p.put(ss.getLabel(), null);
                }
            }
        }
        trace.append("--iteration: 0\n");
        trace.append("    l = ").append(l).append("\n");
        trace.append("    p = ").append(p).append("\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
    
    public Sommet getSByLabel(String lab)
    {
        for(Sommet ss:sommets)
        {
            if(ss.getLabel().equals(lab))return ss;
        }
        return null;
    }
    
    public String selectionMinCout()
    {
        Collection<Double> couts_non_visites = new ArrayList<>();
        for (Map.Entry<String, Double> entry : l.entrySet())
        {
            String key = entry.getKey(); 
            Double value = entry.getValue();
            
            if(t.contains(key))
            {
                couts_non_visites.add(value);
            }
        }
        for (Map.Entry<String, Double> entry : l.entrySet())
        {
            String key = entry.getKey();
            Double value = entry.getValue();
            
            if(t.contains(key) && value <= Collections.min(couts_non_visites))
            {
                return key;
            }
        }
        return null;
    }
    
    @Override
    public void run()
    {
        preRun();
        while(!t.isEmpty())
        {
            try
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex) {
                Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("--iteration: ").append(++iteration).append("\n");
            
            String j = selectionMinCout();
            Sommet js = getSByLabel(j);
            
            if(js!=null)
            {
                for(Sommet i:g.getVoisinsNonVisites(js,s))
                {
                    Double new_cout = l.get(j) + g.getArret(js, i).getCout();
                    if(l.get(i.getLabel())> new_cout)
                    {
                        l.put(i.getLabel(), new_cout);
                        p.put(i.getLabel(), j);
                    }
                }
                
                s.add(j);
                t.remove(j);
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
