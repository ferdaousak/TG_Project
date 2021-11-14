/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;


public class Sommet implements Comparable<Sommet>
{
    private String label;
    private Color couleurLabel;
    private Color couleur;
    private int position_x;
    private int position_y;
    private boolean selected;
    private double degree;
    
    //we have to add a variable if sommet is marked 
    private boolean marked = false;

    public String getLabel()
    {
        return label;
    }
    
    public void setdegree(double d)
    {
    	this.degree = d;
    }
    
    public double getdegree()
    {
    	return degree;
    }
    
    public void setLabel(String label)
    {
        this.label = label;
    }

    public Color getCouleurLabel()
    {
        return couleurLabel;
    }

    public void setCouleurLabel(Color couleurLabel)
    {
        this.couleurLabel = couleurLabel;
    }

    public Color getCouleur()
    {
        return couleur;
    }

    public void setCouleur(Color couleur)
    {
        this.couleur = couleur;
    }

    public int getPosition_x()
    {
        return position_x;
    }

    public void setPosition_x(int position_x)
    {
        this.position_x = position_x;
    }

    public int getPosition_y()
    {
        return position_y;
    }

    public void setPosition_y(int position_y)
    {
        this.position_y = position_y;
    }
    
    public boolean getSelected()
    {
        return this.selected;
    }
    
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
    public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

    public Sommet(Sommet o)
    {
        this.label = o.label;
        this.couleurLabel = o.couleurLabel;
        this.couleur = o.couleur;
        this.position_x = o.position_x;
        this.position_y = o.position_y;
        this.selected = o.selected;
    }
    public Sommet(String label, Color couleurLabel, Color couleur, int position_x, int position_y)
    {
        this.label = label;
        this.couleurLabel = couleurLabel;
        this.couleur = couleur;
        this.position_x = position_x;
        this.position_y = position_y;
    }
    public Rectangle getBounds()
    {
        return new Rectangle(position_x-Configuration.taille_sommet, position_y-Configuration.taille_sommet, Configuration.taille_sommet*2, Configuration.taille_sommet*2);
    }
    
    
    public void draw(Graphics2D g2d)
    {
        g2d.setColor(couleur);
        g2d.fillOval(position_x-Configuration.taille_sommet, position_y-Configuration.taille_sommet, Configuration.taille_sommet*2, Configuration.taille_sommet*2);
        if(selected){
            g2d.setColor(Color.RED);
            g2d.drawOval(position_x-(Configuration.taille_sommet+1), position_y-(Configuration.taille_sommet+1), Configuration.taille_sommet*2+2, Configuration.taille_sommet*2+2);
        }
        g2d.setColor(couleurLabel);
        if(marked) g2d.drawString("+", position_x-5,  position_y-Configuration.taille_sommet - 5);
        int labelWidth = g2d.getFontMetrics().stringWidth(label);
        int labelHeight = g2d.getFontMetrics().getHeight();
        g2d.drawString(label, position_x-labelWidth/2, position_y+labelHeight/4);
    }

    @Override
    public String toString()
    {
        return label;
    }

    @Override
    public int compareTo(Sommet o)
    {
        return this.label.compareTo(o.label);
    }
    
}
