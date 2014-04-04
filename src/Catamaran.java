
// basic rules: 
// end level when all dogs are dead
// +10 points per treasure picked up
// -10 points per treasure picked up by dogs

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import java.util.*;
import java.awt.Image.*;

public class Catamaran extends Applet implements Runnable
{
	public static final String CATAMARAN_URL = "http://www.mscs.mu.edu/~fharunan/Games/Catamaran/resources/";

    public static int CHAR_SPEED = 6;

	public final int GAMESTART = 0;
	public final int GAMEPLAY = 1;
	public final int GAMEWON = 2;
	public final int GAMELOST = 3;
	int gameState; 
	
	int level;
	CapnFerdinandLongwhiskers ferdie;
	ArrayList<RoyalNavySeadog> doggies;
	ArrayList<Booty> booties;
    ArrayList<AcornBooty> acornBooties;
	ArrayList<SquirrelBullet> bullets;
    ArrayList<DogBullet> dogBullets;
    Image capt;
    Image dog;
    Image chest;
    Image acornImg;
	Image bulletImg;
    Image dogBulletImg;
	Thread anim;
	Image buffer;
	Graphics bufgr;
	Image bkgnd;
	int cut;
	Font font;
	int FRAME_DELAY = 50;
	public static final int VWIDTH = 640;
	public static final int VHEIGHT = 640;
	public static final int SCROLL = 160;  // Set edge limit for scrolling
	int vleft = 0;	// Pixel coord of left edge of viewable area; used for scrolling
	static final int MINY = 150; // prevent sprites from travelling on land or in air
	static final int MAXX = 2000; // prevent sprites from travelling forever off into never never land
	int points; 
	boolean spaceReleased; 
	int crewmembers; 
	AudioClip ferdieDead;
	AudioClip cannonFired;
	AudioClip squirrelHitsDog;
	AudioClip fireSquirrel;
	AudioClip collectAcorn;
	AudioClip collectTreasure;
    AudioClip tortuga;

	public void init()
	{
		if (getKeyListeners().length < 1)
		{
			addMouseListener(new mseL());
			addKeyListener(new keyL());
		}

        gameState = GAMESTART;
		points = 0;
        level = 1;

        buffer = createImage(VWIDTH,VHEIGHT);
		bufgr = buffer.getGraphics();
		font = new Font("Arial",Font.ITALIC,30);

	    // The following statements are necessary in order to host all game files up on mscs, because each
	    // individual computer needs to download all images.
		
	    try {
		    capt = getImage(new URL(CATAMARAN_URL + "catBlock.png"));
	    } catch (MalformedURLException e) {}

	    try {
	    	dog = getImage(new URL(CATAMARAN_URL + "dogBlock.png"));
	    } catch (MalformedURLException e) {}

	    try {
	    	chest = getImage(new URL(CATAMARAN_URL + "treasureBlock.png"));
	    } catch (MalformedURLException e) {}

	    try {
		    bulletImg = getImage(new URL(CATAMARAN_URL + "squirrelBlock.png"));
	    } catch (MalformedURLException e) {}	

        try {
            acornImg = getImage(new URL(CATAMARAN_URL + "acornBlock.png"));
        } catch (MalformedURLException e) {}

        try {
            dogBulletImg = getImage(new URL(CATAMARAN_URL + "cannonballBlock.png"));
        } catch (MalformedURLException e) {}

        try {
            bkgnd = getImage(new URL(CATAMARAN_URL + "beach.png"));
        } catch (MalformedURLException e) {}

	    try {
		    ferdieDead = getAudioClip(new URL(CATAMARAN_URL + "ferdieDead.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    cannonFired = getAudioClip(new URL(CATAMARAN_URL + "cannonFired.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    squirrelHitsDog = getAudioClip(new URL(CATAMARAN_URL + "squirrelHitsDog.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    fireSquirrel = getAudioClip(new URL(CATAMARAN_URL + "fireSquirrel.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    collectAcorn = getAudioClip(new URL(CATAMARAN_URL + "collectAcorn.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    collectTreasure = getAudioClip(new URL(CATAMARAN_URL + "collectTreasure.wav"));
	    } catch (MalformedURLException e) {}

	    try {
		    tortuga = getAudioClip(new URL(CATAMARAN_URL + "tortuga.wav"));
	    } catch (MalformedURLException e) {}

        // Chill while images aren't loaded from the interwebz
        while (capt.getHeight(this) == -1 || dog.getHeight(this) == -1 ||
                chest.getHeight(this) == -1 || bulletImg.getHeight(this) == -1 ||
                bkgnd.getHeight(this) == -1 || acornImg.getHeight(this) == -1 ||
                dogBulletImg.getHeight(this) == -1);
        
        // Start looping the background song
        tortuga.loop();
	}

    public void startNewLevel()
    {
        spaceReleased = false;
        crewmembers = 0;

        // Create sprite objects
        ferdie = new CapnFerdinandLongwhiskers(this, capt, 50, 249);
        doggies = new ArrayList<RoyalNavySeadog>();
        Random rm = new Random();
        for (int i = 0; i < level*5; i++)
        {
            boolean overlap = true;
            int x = 0;
            int y = 0;
            // Ensure dogs don't overlap
            while(overlap)
            {
                x = rm.nextInt(MAXX - 64 - 128) + 128;
                y = rm.nextInt(VHEIGHT-MINY-64)+MINY;

                overlap = false;
                for (RoyalNavySeadog dog : doggies)
                {
                    if (dog.collisionBox().intersects(new Rectangle(x, y, 64, 64)))
                    {
                        overlap = true;
                        break;
                    }
                }
            }
            doggies.add(new RoyalNavySeadog(this, dog, x, y));
        }
        booties = new ArrayList<Booty>();
        for (int i = 0; i < level*10; i++)
        {
            booties.add(new Booty(this, chest, rm.nextInt(MAXX - 64), rm.nextInt(VHEIGHT-MINY-64)+MINY));
        }
        acornBooties = new ArrayList<AcornBooty>();
        for (int i = 0; i < level*10; i++)
        {
            acornBooties.add(new AcornBooty(this, acornImg, rm.nextInt(MAXX - 64), rm.nextInt(VHEIGHT-MINY-64)+MINY));
        }
        bullets = new ArrayList<SquirrelBullet>();
        dogBullets = new ArrayList<DogBullet>();
    }

	public void start()
	{
		anim = new Thread(this);
		anim.start();
	}	
	public void stop()
	{
		anim = null;
	}

	public void run()
	{

		while (true)
		{

			switch (gameState)
			{
				case GAMEPLAY: 
					if (spaceReleased)
					{
						if (crewmembers > 0)
						{
                            fireSquirrel.play();
							int bulX;
							if (ferdie.dir == CapnFerdinandLongwhiskers.RIGHT) bulX = ferdie.locx+ferdie.width-20;
							else bulX = ferdie.locx-ferdie.width+20;
							bullets.add(new SquirrelBullet(this, bulletImg, bulX, ferdie.locy, ferdie.dir));
							crewmembers--;
						}
						spaceReleased = false; 
					}
					ferdie.update();
					for (RoyalNavySeadog dog : doggies)
					{
						dog.update();
					}
					for (SquirrelBullet bullet : bullets)
					{
						bullet.update();
					}
                    for (DogBullet bullet : dogBullets)
                    {
                        bullet.update();
                    }
					checkScrolling();
					checkCollisions();
                    checkBulletFire();
					repaint();
					break;
				case GAMESTART:
				case GAMEWON:
				case GAMELOST:
					repaint();
					break;
			}
			try
			{
				Thread.sleep(FRAME_DELAY);
			}
			catch (InterruptedException e) {}
		}		
	}
	void checkScrolling()
	{
		// Test if ferdie is at edge of view window and scroll appropriately
		if (ferdie.locx < (vleft+SCROLL))
		{
			vleft = ferdie.locx-SCROLL;
			if (vleft < 0)
				vleft = 0;
		}
		if ((ferdie.locx + ferdie.width) > (vleft+VWIDTH-SCROLL))
		{
			vleft = ferdie.locx+ferdie.width-VWIDTH+SCROLL;
			if (vleft > MAXX - VWIDTH)
				vleft = MAXX - VWIDTH;
		}
	}

	void checkCollisions()
	{
		Rectangle catBox = ferdie.collisionBox();
		
		// Cat and Treasure collisions
		Iterator<Booty> treasureIter = booties.iterator();
		while (treasureIter.hasNext())
		{
			Booty booty = treasureIter.next();
			if (catBox.intersects(booty.collisionBox()))
			{
                collectTreasure.play();
				points += 10;
				treasureIter.remove();
				break;
			}
		}

        // Cat and Acorn collisions
        Iterator<AcornBooty> acornBootyIterator = acornBooties.iterator();
        while (acornBootyIterator.hasNext())
        {
            AcornBooty acornBooty = acornBootyIterator.next();
            if (catBox.intersects(acornBooty.collisionBox()))
            {
                collectAcorn.play();
                crewmembers++;
                acornBootyIterator.remove();
                break;
            }
        }

        Iterator<DogBullet> dogBulletIterator = dogBullets.iterator();
        while (dogBulletIterator.hasNext())
        {
            DogBullet bullet = dogBulletIterator.next();

            if (!bullet.onscreen())
            {
                dogBulletIterator.remove();
                continue;
            }

            // Cat and DogBullet collisions
            if (catBox.intersects(bullet.collisionBox()))
            {
                tortuga.stop();
                ferdieDead.play();
                gameState = GAMELOST;
                ferdie.capt = ferdie.flipImageVert(ferdie.capt);
                break;
            }
        }

        Iterator<SquirrelBullet> bulletIter;
		Iterator<RoyalNavySeadog> doggiesIter = doggies.iterator();
		while(doggiesIter.hasNext())
		{
			RoyalNavySeadog dog = doggiesIter.next();

			// Cat and Dog collisions
			Rectangle dogBox = dog.collisionBox();
			if (catBox.intersects(dogBox)) 
			{  
                tortuga.stop();
                ferdieDead.play();
				gameState = GAMELOST;
				ferdie.capt = ferdie.flipImageVert(ferdie.capt);
				break;
			}

			// Dog and Treasure collisions
			treasureIter = booties.iterator();
			while (treasureIter.hasNext())
			{
				Booty booty = treasureIter.next();
				if (dogBox.intersects(booty.collisionBox()) && booty.onscreen()) // Only lets dogs get treasure if it is onscreen
				{
                    collectTreasure.play();
					points -= 10;
					treasureIter.remove();
					break;
				}
			}
		}

		// Dog and Bullet collisions
		bulletIter = bullets.iterator();
		while (bulletIter.hasNext())
		{
			SquirrelBullet bullet = bulletIter.next();
			doggiesIter = doggies.iterator();

			// if a bullet goes offscreen, delete it so that it doesn't attack things we can't see
			if (!bullet.onscreen())
			{
				bulletIter.remove();
				continue;
			}

			while (doggiesIter.hasNext())
			{
				RoyalNavySeadog dog = doggiesIter.next();
				if (bullet.collisionBox().intersects(dog.collisionBox()))
				{
                    squirrelHitsDog.play();
					points += 5;
					doggiesIter.remove();
					bulletIter.remove();
					break; 
				}
			}
		}

		// Dog and Dog collisions
		for (int i = 0; i < doggies.size(); i++)
		{
			Rectangle dogBox = doggies.get(i).collisionBox();
			for (int j = 0; j < doggies.size(); j++)
			{
				if ((i != j) && dogBox.intersects(doggies.get(j).collisionBox()))
				{
					doggies.get(i).switchDirection();
                    doggies.get(j).switchDirection();
				}
			}
		}
		// If all dogs have been killed, then we won the level
		if (doggies.isEmpty())
		{
			gameState = GAMEWON;
            level++;
		}
	}

    void checkBulletFire()
    {
        Iterator<RoyalNavySeadog> doggiesIter = doggies.iterator();
        while(doggiesIter.hasNext())
        {
            RoyalNavySeadog dog = doggiesIter.next();
            if (dog.onscreen() && dog.readyToFire())
            {
                int x, y;
                if (dog.dir == RoyalNavySeadog.RIGHT) x = dog.locx+dog.width-20;
                else x = dog.locx-dog.width+20;
                double dir = Math.atan(((double) (ferdie.locy - dog.locx)) / (ferdie.locx - dog.locx));
                dogBullets.add(new DogBullet(this, dogBulletImg, x, dog.locy, dir));
            }
        }
    }

	public void update(Graphics g)
	{
		paint(bufgr);
		g.drawImage(buffer,0,0,this);
	}

	public void paint(Graphics g)
	{

		switch (gameState)
		{
			case GAMESTART:
				g.setColor(Color.yellow);
				g.fillRect(0,0,VWIDTH,VHEIGHT);
				g.setColor(Color.blue);
				g.setFont(font);
				g.drawString("Click to Start", VWIDTH/2 - 100, VHEIGHT/2);
				break;

			case GAMEPLAY:
				drawBoard(g);
				break;

			case GAMEWON:
				drawBoard(g);
				g.drawString("You win!",100,100);
                g.drawString("Click to Start Next Level", VWIDTH/2 - 100, VHEIGHT/2);
				break;

			case GAMELOST:
				drawBoard(g);
				g.drawString("You lose.",100,100);
				break;
		}		
	}

	private void drawBoard(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0,0,VWIDTH,VHEIGHT);
		g.setClip(0,0,VWIDTH,VHEIGHT);
		cut = vleft;
		g.drawImage(bkgnd, -cut, 0, this);
		g.drawImage(bkgnd, 1280-cut, 0, this);
		g.drawImage(bkgnd, 2560-cut, 0, this);
		g.drawImage(bkgnd, 3840-cut, 0, this);
		g.drawImage(bkgnd, 5120-cut, 0, this);

		ferdie.paint(g);

		for (RoyalNavySeadog dog : doggies)
		{
			dog.paint(g);
		}
		for (Booty booty : booties)
		{
			booty.paint(g);
		}
        for (AcornBooty acornBooty : acornBooties)
        {
            acornBooty.paint(g);
        }
		for (SquirrelBullet bullet : bullets)
		{
			bullet.paint(g);
		}
        for (DogBullet bullet : dogBullets)
        {
            bullet.paint(g);
        }
		g.setColor(Color.black);
		g.setFont(font);	
		g.drawString("Points: " + points, 100, 140);
        g.drawString("x" + crewmembers, 525, 50);
        g.drawImage(bulletImg, 485, 15, 40, 40, null);
	}

	private class mseL extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			requestFocus();
			if (gameState == GAMESTART || gameState == GAMEWON)
            {
                startNewLevel();
				gameState = GAMEPLAY;
            }
		}
	}

	private class keyL extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();
			switch (key)
			{
				case KeyEvent.VK_A: 
				case KeyEvent.VK_LEFT:
					ferdie.setDir(CapnFerdinandLongwhiskers.LEFT);
					ferdie.dx = -CHAR_SPEED;
					break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D: 
					ferdie.setDir(CapnFerdinandLongwhiskers.RIGHT);
					ferdie.dx = CHAR_SPEED;
					break;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					ferdie.dy = -CHAR_SPEED;
					break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:  
					ferdie.dy = CHAR_SPEED;
					break;
				case KeyEvent.VK_R:
					init();
					break;
			}
		}
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();
			switch (key)
			{
				case KeyEvent.VK_A: 
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					ferdie.dx = 0;
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					ferdie.dy = 0;
					break;
				case KeyEvent.VK_SPACE:
					spaceReleased = true; 
					break;
			}
		}
	}
}
