/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;


public class Arret implements Comparable<Arret>
{
    private Sommet sommetA;
    private Sommet sommetB;
    private Color couleur;
    private double cout;
    private Double flux = null;
    private boolean dashed=false;// pour wireshall
    
    //constructeur de recopie
    public Arret(Arret o,Sommet a,Sommet b)
    {
        this.sommetA = a;
        this.sommetB = b;
        this.couleur = o.couleur;
        this.cout = o.cout;
        this.dashed = o.dashed;
        this.flux = o.flux;
    }
    public Arret(Sommet sommetA, Sommet sommetB, Color couleur)
    {
        this.sommetA = sommetA;
        this.sommetB = sommetB;
        this.couleur = couleur;
        if(sommetA==sommetB)this.cout = 2;
        else this.cout = 1;
    }
    public Arret(Sommet sommetA, Sommet sommetB, Color couleur, double cout)
    {
        this.sommetA = sommetA;
        this.sommetB = sommetB;
        this.couleur = couleur;
        this.cout = cout;
    }

    public Double getFlux()
    {
        return flux;
    }

    public void setFlux(Double flux)
    {
        this.flux = flux;
    }

    public boolean isDashed()
    {
        return dashed;
    }

    public void setDashed(boolean dashed)
    {
        this.dashed = dashed;
    }

    
    public Sommet getSommetA()
    {
        return sommetA;
    }

    public void setSommetA(Sommet sommetA)
    {
        this.sommetA = sommetA;
    }

    public Sommet getSommetB()
    {
        return sommetB;
    }

    public void setSommetB(Sommet sommetB)
    {
        this.sommetB = sommetB;
    }

    public Color getCouleur()
    {
        return couleur;
    }

    public void setCouleur(Color couleur)
    {
        this.couleur = couleur;
    }

    public double getCout()
    {
        return cout;
    }

    public void setCout(double cout)
    {
        this.cout = cout;
    }
    
    public void draw(Graphics2D g2d)
    {
    	//line
        Stroke st = g2d.getStroke();
        g2d.setColor(couleur);
        
        if(dashed)
        	g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        else 
        	g2d.setStroke(new BasicStroke(Configuration.taille_arret));
        if(sommetA==sommetB)
        {//boucle
            g2d.drawArc(sommetA.getPosition_x()-2*Configuration.taille_sommet/3, sommetA.getPosition_y()-(Configuration.taille_sommet+Configuration.taille_sommet/2), 4*Configuration.taille_sommet/3, Configuration.taille_sommet, 0, 360);
        }else
        {
            g2d.drawLine(sommetA.getPosition_x(), sommetA.getPosition_y(),sommetB.getPosition_x(), sommetB.getPosition_y());
            if(Configuration.oriente)
            {
                double dx = sommetB.getPosition_x() - sommetA.getPosition_x();
                double dy = sommetB.getPosition_y() - sommetA.getPosition_y();
                double distance = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
                double x_arrow = ((Configuration.taille_sommet/distance)*(sommetA.getPosition_x()-sommetB.getPosition_x())) + sommetB.getPosition_x();
                double y_arrow = ((Configuration.taille_sommet/distance)*(sommetA.getPosition_y()-sommetB.getPosition_y())) + sommetB.getPosition_y();
                drawArrowHead(g2d,sommetA.getPosition_x(),sommetA.getPosition_y(),x_arrow,y_arrow);
                
            }
            
        }
        g2d.setStroke(st);
    }
    public void drawLabel(Graphics2D g2d)
    {
        if(Configuration.pondere)
        {
            if(Configuration.oriente)
            {
                double dx = sommetB.getPosition_x() - sommetA.getPosition_x();
                double dy = sommetB.getPosition_y() - sommetA.getPosition_y();
                double distance = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));

                double x_arrow = ((Configuration.taille_sommet/distance)*(sommetA.getPosition_x()-sommetB.getPosition_x())) + sommetB.getPosition_x();
                double y_arrow = ((Configuration.taille_sommet/distance)*(sommetA.getPosition_y()-sommetB.getPosition_y())) + sommetB.getPosition_y();
                drawCout(g2d,sommetA.getPosition_x(),sommetA.getPosition_y(),x_arrow,y_arrow);
            }else if(sommetA!=sommetB)
            {
                //get center of the line
                int centerX =sommetA.getPosition_x() + ((sommetB.getPosition_x()-sommetA.getPosition_x())/2);
                int centerY =sommetA.getPosition_y() + ((sommetB.getPosition_y()-sommetA.getPosition_y())/2);
                //get the angle in degrees
                double deg = Math.toDegrees(Math.atan2(centerY - sommetB.getPosition_y(), centerX - sommetB.getPosition_x())+ Math.PI);
                //need this in order to flip the text to be more readable within angles 90<deg<270
                if ((deg>90)&&(deg<270))
                {
                    deg += 180;
                }
                double angle = Math.toRadians(deg);
                String label=""+cout;
                if(flux!=null)label+="/"+flux;
                //get the length of the text on screen
                int sw =  g2d.getFontMetrics().stringWidth(label);
                //rotate the text
                g2d.rotate(angle, centerX, centerY);
                //draw the text to the center of the line
                g2d.setColor(Configuration.coleur_label);
                //if(flux!=0)label=
                g2d.drawString(label, centerX - (sw/2), centerY - (Configuration.taille_arret+5)); 
                //reverse the rotation
                g2d.rotate(-angle, centerX, centerY);
            }
        }
    }
    private void drawCout(Graphics2D g2d,int x1,int y1, double x2, double y2)
    {
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        double x = 0, y = 0, rho = theta + Math.toRadians(15);
        x = x2 - 15 * Math.cos(rho);
        y = y2 - 15 * Math.sin(rho);
        g2d.setColor(Configuration.coleur_label);
        String label=""+cout;
        if(flux!=null)label+="/"+flux;
        g2d.drawString(label, (int)x-10, (int)y-10);
    }
    private void drawArrowHead(Graphics2D g2d,int x1,int y1, double x2, double y2)
    {
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        double x = 0, y = 0, rho = theta + Math.toRadians(15);
        for(int j = 0; j < 2; j++)
        {
            x = x2 - 15 * Math.cos(rho);
            y = y2 - 15 * Math.sin(rho);
            g2d.draw(new Line2D.Double(x2, y2, x, y));
            rho = theta - Math.toRadians(15);
        } 
    }

    @Override
    public String toString()
    {
        return "("+sommetA.toString()+"-"+sommetB.toString()+")";
    }

    @Override
    public int compareTo(Arret o)
    {
        if(this.cout==o.cout)
        {
            String a = this.sommetA.getLabel().compareTo(this.sommetB.getLabel())<=0?this.sommetA.getLabel():this.sommetB.getLabel();
            String b = o.sommetA.getLabel().compareTo(o.sommetB.getLabel())<=0?o.sommetA.getLabel():o.sommetB.getLabel();
            return a.compareTo(b);
        }
        return this.cout>o.cout?1:-1;
    }
}
