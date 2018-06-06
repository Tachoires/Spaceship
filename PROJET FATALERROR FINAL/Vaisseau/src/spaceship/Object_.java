/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceship;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Object_ {
    public static final int TAILLEX = 7;
    public static final int TAILLEY = 7;
    private int x, y;
    private ArrayList<Missile> missiles;
    
    public Object_(int x, int y){
        this.x = x;
        this.y = y;
        missiles = new ArrayList();
    }
    
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    
    public ArrayList<Missile> getMissile(){
        return new ArrayList<Missile>(missiles);
    }

        //Mouvements
    public boolean setLeft(){
        if(x != 0)
        {
            this.x--;
        }
            
        else
            System.out.println("Houston on a un problema");
                    
        return (x+1) != 0;

    }
    
    public boolean setRight(){
        if(x != TAILLEX - 1)
            this.x++;
        return (x-1) != TAILLEX - 1;
    }
    
    public boolean setUp(){
        if (y != 0)
            this.y--;
        return (y+1) != 0;
    }

    public boolean setDown(){
        if (y != TAILLEY - 1)
            this.y++;
        return (y-1) != TAILLEY - 1;
    }
    
    public boolean move(char direction, int plateau[][]){
        switch(direction)
        {
            case 'q':     //Left
                if(plateau[x - 1][y] == 1 || plateau[x - 1][y] == 2)
                {
                    setRight();
                    return false;
                }
                setLeft();
                return true;
                
            case 'd':     //Right
                if(plateau[x + 1][y] == 2 || plateau[x + 1][y] == 1)
                {
                    setLeft();
                    return false;
                }
                setRight();
                return true;
            case 'z':     //Up
                //System.out.println("x = " + x + " | y = " + y);
                if(plateau[x][y - 1] == 2 || plateau[x][y - 1] == 1)
                {
                    setDown();
                    return false;
                }
                setUp();
                return true;
            case 's':     //Down
                if(plateau[x][y + 1] == 2 || plateau[x][y + 1] == 1)
                {
                    setUp();
                    return false;
                }
                setDown();
                return true;
            default:
                System.out.println("(Fatal)Error!");
                return false;
        }
    }
    
    //Missiles
    
    public void addMissile(int x, int y, int type, int direction)
    {
        missiles.add(new Missile(x, y, type, direction));
    }
    
     public void delMissile(Missile missile)
    {
        //System.out.println("Missile: " + missile.getX() + "/" + missile.getY());
        
        
        boolean remove = missiles.remove(missile);
        try {
            missile.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(Object_.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("Remove: "  + remove);
    }
    
    public boolean tir(int x, int y, char type_tir, int joueur, int plateau[][])
    {
        if (joueur == 1)
        {
            if(plateau[x][y-1] == 3)
            {
                plateau[x][y-1] = 0;
            }
            switch(type_tir)
            {
            case '1':     //Left
                this.addMissile(x, y - 1, 1 + 4, 1);
                return true;
            case '2':     //Right
                this.addMissile(x, y - 1, 2 + 4, 2);
                return true;
            default:
                System.out.println("(Fatal)Error!");
                return false;
            }
        }
        else { //joueur == 2
            
            if(plateau[x][y+1] == 3)
            {
                plateau[x][y+1] = 0;
            }
            switch(type_tir)
            {
            case '1':     //Left
                this.addMissile(x, y + 1, 1 + 4, 1);
                return true;
            case '2':     //Right
                this.addMissile(x, y + 1, 2 + 4, 2);
                return true;
            default:
                System.out.println("(Fatal)Error!");
                return false;
            }
        }
        
    }
}
