import java.awt.*;
import java.util.*;

public class Booty extends Sprite
{
	Image treasure;

	public Booty(Catamaran a, Image img, int x, int y)
	{
		super(a, x, y);
		treasure = img;
	}

	public void paint(Graphics g)
	{
		g.drawImage(treasure, locx-app.vleft, locy, null);
		//g.drawRect(locx-app.vleft+10, locy+10, width-20, height-20);
	}
	
	public void update() {}

	public Rectangle collisionBox()
	{
		return new Rectangle(locx, locy, width, height);
	}

	public boolean onscreen()
	{
		return collisionBox().intersects(new Rectangle(app.vleft, 0, app.vleft + app.VWIDTH, app.VHEIGHT));
	}
}
