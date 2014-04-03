import java.awt.*;
import java.util.*;

public class SquirrelBullet extends Sprite
{
	Image bulletImg;

	public SquirrelBullet(Catamaran a, Image img, int x, int y, int dir)
	{
		super(a, x, y);
		bulletImg = img;
		if (dir == CapnFerdinandLongwhiskers.LEFT)
		{
			bulletImg = flipImageHor(bulletImg);
			dx = -8;
		}
		else dx = 8;
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(bulletImg, locx-app.vleft, locy, null);
		//g.drawRect(locx-app.vleft+10, locy+10, width-20, height-20);
	}
	
	public void update() 
	{
		locx += dx;
	}

	public Rectangle collisionBox()
	{
		return new Rectangle(locx, locy, width, height);
	}

	public boolean onscreen()
	{
		return collisionBox().intersects(new Rectangle(app.vleft, 0, app.vleft + app.VWIDTH, app.VHEIGHT));
	}
}
