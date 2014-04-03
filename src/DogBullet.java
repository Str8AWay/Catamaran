import java.awt.*;

public class DogBullet extends Bullet {

    public DogBullet(Catamaran a, Image img, int x, int y, double dir)
    {
        super(a, img, x, y);
        dx = (int) Math.cos(dir)*8;
        dy = (int) -Math.sin(dir)*8;
    }

    public void update()
    {
        locx += dx;
        locy += dy;
    }
}
