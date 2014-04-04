import java.awt.*;
import java.util.*;

public class SquirrelBullet extends Bullet
{
	public SquirrelBullet(Catamaran a, Image img, int x, int y, int dir)
	{
		super(a, img, x, y);
		if (dir == CapnFerdinandLongwhiskers.LEFT)
		{
			bulletImg = flipImageHor(bulletImg);
			dx = -(Catamaran.CHAR_SPEED*2);
		}
		else dx = (Catamaran.CHAR_SPEED*2);
        width = 32;
        height = 32;
        locx += width/4;
        locy += height/4;
	}
	
	public void update() 
	{
		locx += dx;
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
