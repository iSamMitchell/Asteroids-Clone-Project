import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;

public class Asteroid
{  
  private BufferedImage largeAsteroid = null, mediumAsteroid = null, smallAsteroid = null, img = null;
  private double x, y, velocity, spinSpeed, spin; 
  private int asteroidSize = 0, lastSize = 1;
  private int angle;
  
  //large asteroid w: 61px, h: 51px
  //medium asteroid w: 31px, h: 25px
  //small asteroid w: 15px, h: 14px
  
  public Asteroid(int x, int y, int angle, double speed, double spinSpeed)
  {   
    //Initialized all the attributes of the asteroid
    this.x = x;
    this.y = y;
    this.angle = angle;
    spin = angle;
    velocity = speed;
    this.spinSpeed = spinSpeed;
    //Randomly generated the asteroid's size
    asteroidSize = (int)(Math.random() * 3 + 1);  
    loadAsteroids();    
  }
  
  public void setAngle(int newAngle)//Sets the new angle of the asteroid when it collides with another object.
  {
    angle = newAngle;
  }
  
  public int getAngle()//returns the angle of direction which the asteroid is travelling.
  {
    return angle;
  }
  
  public void loadAsteroids()
  {
    //Loads in all 3 asteroid images
    try {
      largeAsteroid = ImageIO.read(new File("Images/Large Asteroid.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/Large Asteroid.png could not be found!");
    }
    try {
      mediumAsteroid = ImageIO.read(new File("Images/Medium Asteroid.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/Medium Asteroid.png could not be found!");
    }
    try {
      smallAsteroid = ImageIO.read(new File("Images/Small Asteroid.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/Small Asteroid.png could not be found!");
    }
    //Sets the asteroid's sprite depending on the size
    if(asteroidSize == 1)
      img = largeAsteroid;
    else if(asteroidSize == 2)
      img = mediumAsteroid;
    else if(asteroidSize == 3)
      img = smallAsteroid;
  }
  
  public void setLastSize(int i)//Sets the past asteroid size after exploding
  {
    lastSize = i;
  }
  
  public void paint(Graphics g)
  {
    //Paints the asteroids with constantly changing angles to give a spin effect
    int x = (int) this.x;
    int y = (int) this.y;
    spin = spin + spinSpeed;
    
    Graphics2D gg = (Graphics2D) g.create();
    gg.rotate(Math.toRadians((int)spin), x + img.getWidth()/2, y + img.getHeight()/2);
    gg.drawImage(img, x, y, null);
    gg.dispose();

    gg = (Graphics2D) g.create();

  }
  
  public int getX()//returns the x coordiate of the asteroid
  {
    return (int) x;
  }
  
  public int getY()//returns the y coordinate of the asteroid
  {
    return (int) y;
  }
  
  public int getWidth()//returns the sprite width of the asteroid
  {
    return img.getWidth();
  }
  
  public int getHeight()//returns the sprite height of the asteroid
  {
    return img.getHeight();
  }
  
  public double getSpeed()//returns the speed of the asteroid
  {
    return velocity;
  }
  
  //Sets the speed of the asteroid, the only other time this can happen is when the
  //asteroid is created. this is for collision of larger and smaller asteroids.
  public void setSpeed(double speed)
  {
    velocity = speed;
  }
  
  public void blowUp()
  {
    //Changes the sprite of the asteroid when it is blown up
    if (asteroidSize == 1)
    {
      x = x + (img.getWidth()/2)/2;
      y = y + (img.getHeight()/2)/2;     
      img = mediumAsteroid;
      asteroidSize = 2;
    }
    if (asteroidSize == 2&&lastSize!=1)
    {
      x = x + (img.getWidth()/2)/2;
      y = y + (img.getHeight()/2)/2;
      img = smallAsteroid;
      asteroidSize = 3;
    }
    int tempAngle = (int)(90 * Math.random()) + 0;
    if(tempAngle > 45)
      tempAngle = tempAngle * -1;
    angle += tempAngle;
  }
  
  public int getAsteroidSize()//returns the asteroid size
  {
    return asteroidSize;
  }
  
  public void move()
  {
    //Moves the asteroid in a set direction
    x+=-velocity * Math.cos(Math.toRadians(angle));
    y+=-velocity * Math.sin(Math.toRadians(angle));
      posCheck();
  }
  public void posCheck()
  {
    //Keeps the asteroid on the screen
    if (x>=764+img.getWidth())
      x=0-img.getWidth();
    if (x<=-img.getWidth()-1)
      x=764;
    if (y>=602+img.getHeight())
      y=0-img.getHeight();
    if (y<=-img.getHeight()-1)
      y=602;
  }
  
  
}