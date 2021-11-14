/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algos;

import metier.Configuration;
import metier.Graphe;


//Algorithme class is an abscart class that's runnable
public abstract class Algorithme implements Runnable
{
    protected String nom;
    protected Graphe g;
    protected int iteration;
    protected StringBuffer trace;
    

    public Algorithme(String nom, Graphe g)
    {
        this.nom = nom;
        this.g = g;
        trace = new StringBuffer();
    }  

    public String getNom() 
    {
        return nom;
    }
    
    protected abstract void preRun();
    protected abstract void postRun();
    
    public StringBuffer getTrace()
    {
        return trace;
    }

    public void setTrace(StringBuffer trace)
    {
        this.trace = trace;
    }

//    public void interupt() {
//        while(!Thread.currentThread().isInterrupted())Thread.currentThread().interrupt();
//        Configuration.current_algo = null;
//    }
}
