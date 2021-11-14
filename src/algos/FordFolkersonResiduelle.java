/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.Arret;
import metier.Chemin;
import metier.Configuration;
import metier.Graphe;
import metier.Sommet;
import theoriegraphes.Canvas;


public class FordFolkersonResiduelle extends Algorithme
{
    private Graphe new_g;
    private Sommet depart;
    private Sommet destination;

    public FordFolkersonResiduelle(Graphe g,Sommet depart, Sommet destination)
    {
        super("Ford-Folkerson (Methode residuelle)", g);
        new_g = new Graphe();
        this.depart = depart;
        this.destination = destination;
    }
    @Override
    protected void preRun()
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        
        trace.append("Algorithme: "+nom+" Debut\n");
        new_g.getSommets().addAll(g.getSommets());
        for(Arret ar : g.getArrets())
        {
            Arret copie = new Arret(ar, ar.getSommetA(), ar.getSommetB());
            new_g.getArrets().add(copie);
        }
        Canvas.getInstance().setGraphe(new_g);
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
    
    @Override
    public void run()
    {
        preRun();
        ArrayList<Chemin> chemins = new_g.getChemins(depart, destination);
        Collections.sort(chemins);
        while(!chemins.isEmpty())
        {
            try
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("--iteration: ").append(++iteration).append("\n");
            Chemin ch = chemins.get(0);
            Double epsi = ch.epsilone();
            for(int i=0; i<ch.getSommets().size()-1;i++)
            {
                Arret ar = new_g.getArret(ch.getSommets().get(i), ch.getSommets().get(i+1));
                if(ar!=null)
                {
                    double new_cout = ar.getCout()-epsi;
                    Arret ar_retour = new_g.getArret(ch.getSommets().get(i+1), ch.getSommets().get(i));
                    
                    if(ar_retour!=null)
                    	ar_retour.setCout(ar_retour.getCout()+epsi);
                    else
                    {
                        new_g.addArret(ch.getSommets().get(i+1), ch.getSommets().get(i),epsi);
                    }
                    if(new_cout==0)
                    {
                        new_g.getArrets().remove(ar);
                    }else
                    {
                        ar.setCout(new_cout);
                    }
                } 
            }
            
            trace.append("    Chemin choisi: ").append(ch).append("\n");
            Canvas.getInstance().repaint();
            Canvas.getInstance().screenShot();
            chemins = new_g.getChemins(depart, destination);
            Collections.sort(chemins);
        }
        try
        {
            Thread.sleep(Configuration.sleep_time);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(BFS.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Arret ar:g.getArrets())
        {
            Arret ar_retour = new_g.getArret(ar.getSommetB(), ar.getSommetA());
            if(ar_retour!=null)
            {
                ar.setFlux(ar_retour.getCout());
            }else
            {
                ar.setFlux(0.0d);
            }
        }
        Canvas.getInstance().setGraphe(g);
        postRun();
    }

    @Override
    protected void postRun()
    {
        trace.append("--Flux Max= ").append(g.getFluxMax()).append("\n");
        trace.append("--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("Algorithme: ").append(nom).append(" Fin.\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }

}
