/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Arret;
import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;


public class Prim extends Algorithme
{
    private Graphe new_g;

    public Prim(Graphe g)
    {
        super("Prim", g);
    }

    @Override
    protected void preRun()
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: "+nom+" Debut\n");
        
        new_g = new Graphe();
        Collections.sort(g.getSommets());
        new_g.getSommets().add(g.getSommets().get(0));
        
        Canvas.getInstance().setGraphe(new_g);
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
    
    @Override
    public void run()
    {
        preRun();
        while(new_g.getSommets().size()<g.getSommets().size())
        {
            try 
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(Prim.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("--iteration: "+(++iteration)+"\n");
            
            ArrayList<Arret> possible_arret = new ArrayList<>();
            for(Sommet s:new_g.getSommets())
            {
                Arret min_ar = g.getMinVoisin(s, new_g.getSommets());
                if(min_ar!=null)
                	possible_arret.add(min_ar);
            }
            
            Collections.sort(possible_arret);
            new_g.getSommets().add(possible_arret.get(0).getSommetB());
            new_g.getArrets().add(possible_arret.get(0));
            
            trace.append("    sommet: "+possible_arret.get(0).getSommetB()+" ajoute via "+possible_arret.get(0)+"\n");
            Canvas.getInstance().repaint();
            Canvas.getInstance().screenShot();
        }
        postRun();
    }
    
    @Override
    protected void postRun()
    {
        trace.append("--ACM= "+new_g.getACM()+"\n");
        trace.append("--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("Algorithme: "+nom+" Fin.\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }

    
    
}
