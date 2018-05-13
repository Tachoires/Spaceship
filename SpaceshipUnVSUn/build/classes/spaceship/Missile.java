package spaceship;

public class Missile extends Coordinates{
    private int type;

    public Missile(int x, int y,  int type)
    {
        super(x,y);
        this.type = type;
    }
}
