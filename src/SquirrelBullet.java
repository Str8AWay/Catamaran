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
			dx = -8;
		}
		else dx = 8;
	}
	
	public void update() 
	{
		locx += dx;
	}
}
