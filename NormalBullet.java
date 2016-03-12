import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class NormalBullet
{  
  //width = 6px, height = 8px
  private BufferedImage img = null;
  private BufferedImage img2 = null;
  private double x, y, velocity = 2;
  private double angle;
  //Creates an instance of timer since each bullet is suppost to only last 1.5 seconds
  private StopWatch timer = new StopWatch();
  
  
  public NormalBullet()
  {   
    //Get the image of the normal bullet
    try {
      img = ImageIO.read(new File("Images/Normal Bullet.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/Normal Bullet.png could not be found!");
    }
  }
  
  public void paint(Graphics g)
  {
    //Since the bullet sprite is not always oriented in the direction of the shot, orient it properly
    angle-=90;
    double rotationRequired = Math.toRadians(angle);
    double locationX = img.getWidth() / 2;
    double locationY = img.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    
    // Drawing the rotated image at the required drawing locations
    int x = (int)this.x;
    int y = (int)this.y;
    g.drawImage(img2, x - 150, y - 150, null);
    g.drawImage(op.filter(img, null), x, y, null);
    //Change the angle back to the original for the bullet to travel in the correct direction
    angle+=90;
  }
  
  public void move()
  {
    //Move the projectile in the direction it is facing
    x+=-velocity* Math.cos(Math.toRadians(angle));
    y+=-velocity* Math.sin(Math.toRadians(angle));
  }
  
  public void setValues(int x, int y, double angle)
  {
    //Sets the position and direction of the bullet relative to the spacecraft when it is created
    timer.start();
    this.x=x;
    this.y=y;
    this.angle=angle+90;
  }
  
  //Returns how long the bullet has been in existance
  public long getTime()
  {
    return timer.getElapsedTimeSeconds();
  }
  
  public int getX()//returns the x position of the bullet for collision detection
  {
    return (int)x;
  }
  
  public int getY()//returns the y position of the bullet for collision detection
  {
    return (int)y;
  }
  
  public int getWidth()//returns the width of the sprite for collision detection
  {
    return img.getWidth();
  }
  
  public int getHeight()//returns the height of the sprite for collision detection
  {
    return img.getHeight();
  }
  
  public int getAngle()//returns the angle of the bullet
  {
    return (int)angle;
  }
}
  