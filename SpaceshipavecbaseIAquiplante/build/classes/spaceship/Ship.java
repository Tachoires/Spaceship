package spaceship;

public class Ship extends Coordinates{
    
    private int life;
    
    public Ship(int x, int y, int life)
    {
        super(x,y);
        this.life = life;
    }
    
    public int getLife()
    {
        return life;
    }
    
    @Override
    public String toString()
    {
        return "";
    }
}
