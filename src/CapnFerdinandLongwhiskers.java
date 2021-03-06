
import java.awt.*;

public class CapnFerdinandLongwhiskers extends Sprite
{

	Image capt; 

	public CapnFerdinandLongwhiskers(Catamaran a, Image img, int x, int y)
	{
		super(a, x, y);
		capt = img; 
		dir = RIGHT;
	}

	public void paint(Graphics g)
	{
		g.drawImage(capt, locx-app.vleft, locy, null);
		//g.drawRect(locx-app.vleft+12, locy+15, width-18, height-25);
	}

	// handle movement inputs and then move
	public void update()
	{
		locx += dx;
		locy += dy;
		if (locy < app.MINY) locy = app.MINY; 
		if (locy + height > app.VHEIGHT) locy = app.VHEIGHT - height; 
		if (locx < 0) locx = 0;
		if (locx + width > app.MAXX) locx = app.MAXX - width; 
		
	}

	public void setDir(int direction)
	{
		if (dir != direction) capt = flipImageHor(capt);
		dir = direction;
	}

	public Rectangle collisionBox()
	{
		return new Rectangle(locx+12, locy+15, width-18, height-25);
	}
}
