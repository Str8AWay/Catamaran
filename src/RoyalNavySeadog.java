import java.awt.*;
import java.util.*;

public class RoyalNavySeadog extends Sprite
{
	Image dogImg; 

	public RoyalNavySeadog(Catamaran a, Image img, int x, int y)
	{
		super(a, x, y);
		dogImg = img;
		Random r = new Random();
		dir = r.nextInt(4) + 1;  
		if (dir == RIGHT) dogImg = flipImageHor(dogImg);
	}

	public void paint(Graphics g)
	{
		g.drawImage(dogImg, locx-app.vleft, locy, null);
		//g.drawRect(locx-app.vleft+10, locy+10, width-20, height-20);
	}

	// handle movement inputs and then move
	public void update()
	{
		if (dir == LEFT)
		{
			dx = -5;
			dy = 0; 
		}
		else if (dir == RIGHT)
		{
			dx = 5;
			dy = 0; 
		}
		else if (dir == UP)
		{
			dx = 0; 
			dy = -5; 
		}
		else if (dir == DOWN)
		{
			dx = 0; 
			dy = 5; 
		}
		else 
		{
			dx = 0; 
			dy = 0; 
		}
		locx += dx;
		locy += dy;
		if ( (locy < app.MINY) || (locy + height > app.VHEIGHT) )
		{
            switchDirection();
		}
		else if ( (locx < 0) || (locx + width > app.MAXX) )
		{
			switchDirection();
		}
	}

    public void switchDirection()
    {
       switch (dir)
       {
           case UP:
               dir = DOWN;
               update();
               break;
           case DOWN:
               dir = UP;
               update();
               break;
           case LEFT:
               dir = RIGHT;
               dogImg = flipImageHor(dogImg);
               update();
               break;
           case RIGHT:
               dir = LEFT;
               dogImg = flipImageHor(dogImg);
               update();
               break;
       }
    }
	public Rectangle collisionBox()
	{
		return new Rectangle(locx+10, locy+10, width-20, height-20);
	}
}