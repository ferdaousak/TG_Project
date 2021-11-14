/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.awt.Color;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;


public class BFS extends Algorithme
{
    private Stack<Sommet> f;
    private Stack<Sommet> m;
    private Sommet r;

    public BFS(Graphe g,Sommet r)
    {
        super("BFS", g);
        this.r = r;
    }
    
    @Override
    public void preRun() 
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("Algorithme: "+nom+" Debut\n");
        
        f = new Stack<>();
        m = new Stack<>();
        f.push(r);
        m.push(r);
        r.setCouleur(Configuration.coleur_parcour);
        
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();   
    }

    @Override
    public void run()
    {
        preRun();
        while(!f.empty())
        {
            try
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("--iteration: "+(++iteration)+"\n");
            Sommet s = f.remove(0);
            for (Sommet t : g.getVoisins(s))
            {
                if(!m.contains(t)){
                    f.push(t);
                    m.push(t);
                    t.setCouleur(Configuration.coleur_parcour);
                    Canvas.getInstance().repaint();
                }
            }
            trace.append("    F="+f+"\n"+"    M="+m+"\n");
            Canvas.getInstance().repaint();
            Canvas.getInstance().screenShot();
        }
        postRun();
    }

    @Override
    public void postRun() 
    {
        trace.append("--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("Algorithme: "+nom+" Fin.\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
    
}
