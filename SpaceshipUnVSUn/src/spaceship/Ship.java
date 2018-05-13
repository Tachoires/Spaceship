/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceship;

public class Ship extends Object_{
    
    private int joueur;  
    
    public Ship(int x, int y, int joueur)
    {
        super(x,y);
        this.joueur = joueur;
    }
    
    public int getJoueur()
    {
        return joueur;
    }
    
    @Override
    public String toString()
    {
        return "";
    }
}
