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


public class Kruscal extends Algorithme
{
    private Graphe new_g;
    private ArrayList<Arret> arrets;
    
    public Kruscal(Graphe g)
    {
        super("Kruscal", g);
    }

    @Override
    protected void preRun()
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: "+nom+" Debut\n");
        
        arrets = g.getArrets();
        Collections.sort(arrets);
        new_g = new Graphe();
        new_g.getSommets().addAll(g.getSommets());
        
        Canvas.getInstance().setGraphe(new_g);
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }

    @Override
    public void run()
    {
        preRun();
        for(Arret ar:arrets)
        {
            try
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(Prim.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            trace.append("--iteration: "+(++iteration)+"\n");
            new_g.getArrets().add(ar);
            if(new_g.detectBoucle())
            {
                new_g.getArrets().remove(ar);
                trace.append("    arret: "+ar+" supprime\n");
            }else
            {
                trace.append("    arret: "+ar+" ajoute\n");
            }
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
