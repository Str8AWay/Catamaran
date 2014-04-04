import java.awt.*;

public class DogBullet extends Bullet {

    public DogBullet(Catamaran a, Image img, int x, int y, double dir)
    {
        super(a, img, x, y);
        dx = (int) (-Math.cos(dir)*Catamaran.CHAR_SPEED*2);
        dy = (int) (-Math.sin(dir)*Catamaran.CHAR_SPEED*2);
        width = 32;
        height = 32;
        locx += width/4;
        locy += height/4;
    }

    public void update()
    {
        locx += dx;
        locy += dy;
    }

    public void paint(Graphics g)
    {
        g.drawImage(bulletImg, locx-app.vleft, locy, width, height, null);
        //g.drawRect(locx-app.vleft+4, locy+4, width-10, height-10);
    }

    public Rectangle collisionBox()
    {
        return new Rectangle(locx+4, locy+4, width-10, height-10);
    }
}
