
import java.awt.*; 
import java.awt.geom.*;
import java.awt.image.*;

public abstract class Sprite
{
	// locx & locy -- top corner of sprite
	public int locx;
	public int locy;
	public int width = 64;
	public int height = 64;
	public int dx = 0, dy = 0;
	public int dir = 0;  
	static final int FLOAT = 0; 
	static final int LEFT = 1; 
	static final int RIGHT = 2; 
	static final int UP = 3; 
	static final int DOWN = 4; 
	Catamaran app; 

	public Sprite(Catamaran a, int x, int y)
	{
		locx = x;
		locy = y;
		app = a;
		dir = FLOAT;
	}

	public void setPosition(int c, int d)
	{
		locx = c; 
		locy = d;
	}

	public void setVelocity(int x, int y)
	{
		dx = x; 
		dy = y;
	}

	public Rectangle collisionBox()
	{
		return new Rectangle(locx, locy, width, height);
	}

	public abstract void paint(Graphics gr);

	public abstract void update();
	
	public Image flipImageHor(Image img)
	{
		BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(img, 0, 0, null);
		// Flip the image horizontally
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		return image;
	}

	public Image flipImageVert(Image img)
	{
		// Flip the image vertically
		BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(img, 0, 0, null);
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);	
		return image;
	}
}
