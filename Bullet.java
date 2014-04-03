import java.awt.*;

/**
 * Created by jlaqua on 4/3/14.
 */
public abstract class Bullet extends Sprite {

    Image bulletImg;
    public Bullet(Catamaran a, Image img, int x, int y)
    {
        super(a, x, y);
        bulletImg = img;

    }

    public void paint(Graphics g)
    {
        g.drawImage(bulletImg, locx-app.vleft, locy, null);
        //g.drawRect(locx-app.vleft+10, locy+10, width-20, height-20);
    }

    public abstract void update();

    public Rectangle collisionBox()
    {
        return new Rectangle(locx, locy, width, height);
    }

    public boolean onscreen()
    {
        return collisionBox().intersects(new Rectangle(app.vleft, 0, app.vleft + app.VWIDTH, app.VHEIGHT));
    }
}
