/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceship;

public class Missile extends Object_{
    private int type;  //5 = 1 case, 6 = 2 cases, 7 = diagonales gauches, 8 b = diagonales droite
    private int direction; //1 : haut, 2: bas
    public Missile(int x, int y,  int type, int direction)
    {
        super(x,y);
        this.type = type;
        this.direction = direction;
    }
    
    
    public int getType()
    {
        return type;
    }
    
}
