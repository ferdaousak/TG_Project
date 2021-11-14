package algos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;

import java.awt.Color;

import metier.Sommet;
import theoriegraphes.Canvas;
import metier.Configuration;
import metier.Graphe;

public class WelchPowell extends Algorithme
{
	private ArrayList<Sommet> ListeOrdone;
	private static Color[] Colors= {Color.RED,Color.PINK,Color.CYAN,Color.BLUE,Color.YELLOW,Color.MAGENTA,Color.PINK,Color.GREEN}; 
	private int nbrC=0;
	public WelchPowell(Graphe g)
	{
		super("Welch & Powell", g);
	}
	
	protected void preRun()
    {
        Configuration.resetImages();
        Canvas.getInstance().screenShot();
        Configuration.backup();
        Configuration.current_algo=this;
        trace.append("-------- Algorithme: "+nom+" Debut ---------\n");
        
        ListeOrdone = new ArrayList<Sommet>(g.getSommets());
        //on ordonne la liste des sommets par degré
        ListeOrdone.sort((s1,s2)->{
        	if(g.degree(s1) == g.degree(s2))
        		return s1.getLabel().compareTo(s2.getLabel());
        	return (int) (g.degree(s2) - g.degree(s1));
        });
        Canvas.getInstance().setGraphe(g);
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
    }
	@Override
	public void run()
	{
		preRun();
		Sommet current;
        while(!ListeOrdone.isEmpty())
        {
            Iterator<Sommet> it = ListeOrdone.iterator();
            try
            {
                Thread.sleep(Configuration.sleep_time);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(WelchPowell.class.getName()).log(Level.SEVERE, null, ex);
            }
            trace.append("\n--iteration: "+(++iteration)+"\n");
            trace.append("Liste ordonné : " + ListeOrdone + "\n");
            trace.append("Couleur : "+getColorName(Colors[nbrC])+" est affecté à : ");
			while(it.hasNext())
			{
				//on selectionne le premier sommet de la liste
				current = it.next();
        		current.setSelected(true);
				boolean found = false;
				//on parcour les voisin du sommet selectionné
				for (Sommet sommet : g.getVoisins(current)) 
				{
					if(sommet.getCouleur().equals(Colors[nbrC]))
					{
						found = true;
						break;
					}
				}	
				//si aucaun voisin n'a la même couleur
				if(!found)
				{
					current.setCouleur(Colors[nbrC]);
					Canvas.getInstance().repaint();
					trace.append(" "+current);
					it.remove();
				}
			}
			nbrC++;
			Canvas.getInstance().repaint();
	        Canvas.getInstance().screenShot();
        }
        postRun();
	}
	@Override
	protected void postRun()
	{
		trace.append("\n Le nombre minimum des couleurs est : "+(nbrC+1));
        trace.append("\n--graphe final: |V|= "+g.getV()+", |E|= "+g.getE()+", Densite= "+g.getDensite()+"\n");
        trace.append("\n--- Algorithme: "+nom+" Fin.----\n");
        Canvas.getInstance().repaint();
        Canvas.getInstance().screenShot();
	}
	
	private String getColorName(Color C)
	{
		HashMap<Color, String> colors = new HashMap<Color, String>();
		colors.put(Color.BLACK,            "BLACK");
		colors.put(Color.BLUE,             "BLUE");
		colors.put(Color.CYAN,             "CYAN");
		colors.put(Color.DARK_GRAY,        "DARK_GRAY");
		colors.put(Color.GRAY,             "GRAY");
		colors.put(Color.GREEN,            "GREEN");
		colors.put(Color.LIGHT_GRAY,       "LIGHT_GRAY");
		colors.put(Color.MAGENTA,          "MAGENTA");
		colors.put(Color.ORANGE,           "ORANGE");
		colors.put(Color.PINK,             "PINK");
		colors.put(Color.YELLOW,           "YELLOW");		
		colors.put(Color.RED,              "RED");
		colors.put(Color.WHITE,            "WHITE");
		colors.put(new Color(192, 0, 255), "PURPLE");
		colors.put(new Color(0xBADA55),    "BADASS_GREEN");
		colors.put(new Color(0, 0, 128),   "DARK_BLUE");
		
		return (String) colors.get(C);
	}

}
