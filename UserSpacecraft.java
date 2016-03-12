import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList; 
//hitbox width: 59, height: 56  

public class UserSpacecraft
{
  private BufferedImage img = null;
  private BufferedImage bigImage = null;
  BufferedImage[] imgGlow = new BufferedImage[9];
  private double angle, totalAngle = 90, x, y, velocity = 0.0, maxVelocity, count = 0, speed, flameSize;
  private ArrayList<NormalBullet> nb = new ArrayList<NormalBullet>(3); //list of bullets - capacity 3
  
  //Gets spacecraft image
  public UserSpacecraft(double maxVelocity)
  {
    this.maxVelocity = maxVelocity;
    try {
      img = ImageIO.read(new File("Images/User-Spacecraft.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/User-Spacecraft.png could not be found!");
    }
    try {
      bigImage = ImageIO.read(new File("Images/glow.png"));
    } 
    catch (IOException e) {
      System.err.println("Images/glow.png could not be found!");
    }
    for(int i = 0; i < 3; i++)
    {
      imgGlow[i] = bigImage.getSubimage(i*32, 96, 32, 32);
      imgGlow[i+3] = bigImage.getSubimage(192+(i*32), 96, 32, 32);
      imgGlow[i+6] = bigImage.getSubimage(i*32, 224, 32, 32);
    }
    
    //centre the sprite in the frame
    x = 382 - (59 / 2);
    y = 301 - (56 / 2);
  }
  
  public double getTotalAngle()//gets total angle of ship
  {
    return totalAngle;
  }
  
  public void paint(Graphics g)//paints the ship and bullets (when shot)
  { 
    for(int i = 0; i < nb.size(); i++)
    {
      if(nb.get(i).getTime()*10 >= 15)
      {
        nb.set(0, null);
        nb.remove(nb.get(0));//removes bullets based on the time they are in game
      }
    }
    double rotationRequired = Math.toRadians (angle);
    double locationX = img.getWidth() / 2;
    double locationY = img.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    
    // Drawing the rotated image at the required drawing locations
    for (int i = 0; i < nb.size(); i++)
    {
      nb.get(i).move();//moves the bullets
      nb.get(i).paint(g);
    }
    int x = (int) this.x;
    int y = (int) this.y;
    
    //Activly changes the flame size dependant on the speed the spacecraft is moving
    if(flameSize > 0 && speed > 0)
      flameSize-=0.1;
    else if(flameSize < 10)
      flameSize+=0.05;
    //If the flames are visible, draw the flames
    if(flameSize < 10)
    {
      //Creates a temporary graphic to rotate the flames
      Graphics2D g2d = (Graphics2D) g.create();
      g2d.rotate(Math.toRadians(angle+180), x + img.getWidth()/2, y+img.getWidth()/2);
      if(velocity > 0 && velocity < 0.2)
      {
        //Randomly draw 1 of 3 flames for each thruster depending on the speed
        int temp = (int)(3 * Math.random()) + 0;
        int temp2 = (int)(3 * Math.random()) + 0;
        g2d.drawImage(imgGlow[temp], x+22, y+(int)flameSize, null);
        g2d.drawImage(imgGlow[temp2], x+37, y+(int)flameSize, null);
      }
      else if(velocity > 0.2 && velocity < 0.3)
      {
        //Randomly draw 1 of 3 flames for each thruster depending on the speed
        int temp = (int)(6 * Math.random()) + 0;
        int temp2 = (int)(3 * Math.random()) + 0;
        g2d.drawImage(imgGlow[temp], x+22, y+(int)flameSize, null);
        g2d.drawImage(imgGlow[temp2], x+37, y+(int)flameSize, null);
      }
      else if(velocity > 0.3)
      {
        //Randomly draw 1 of 3 flames for each thruster depending on the speed`
        int temp = (int)(9 * Math.random()) + 0;
        int temp2 = (int)(9 * Math.random()) + 0;
        g2d.drawImage(imgGlow[temp], x+22, y+(int)flameSize, null);
        g2d.drawImage(imgGlow[temp2], x+37, y+(int)flameSize, null);
      }
      //Dispose of the temporary graphic so nothing else is rotated
      g2d.dispose();
    }
    //Draw the user spacecraft
    g.drawImage(op.filter(img, null), x, y, null);
  }
  
  public void setNB(int x, int y, int speed, double angle)//sets position and movement of bullet
  {
    if (nb.size() > 3)
    {
      nb.set(0, null);
      nb.remove(nb.get(0));//removes the bullets from the game when tried to exceed 3
    }
    if (nb.size() < 3)//only 3 shots allowed at a time
    {
      nb.add(new NormalBullet());//adds bullets to list when a new one is shot
      nb.get(nb.size()-1).setValues(x, y, angle);
    }
  }
  
  public void removeNB(int i)//removes bullet from game when it hits an asteroid
  {
    nb.set(i, null);
    nb.remove(nb.get(i));
  }
  
  public void move(double speed)//moves spacecraft and displays fire when moving
  {
    this.speed = speed;
    if(velocity > 0.1)
    {
    try {
      img = ImageIO.read(new File("Images/User-Spacecraft-Accelerate.png"));
    } 
    catch (IOException e) {
      System.err.println("User-Spacecraft-Accelerate.png cannot be found!");
    }
    }
    else
    {
    try {
      img = ImageIO.read(new File("Images/User-Spacecraft.png"));
    } 
    catch (IOException e) {
      System.err.println("User-Spacecraft.png cannot be found!");
    }
    }
    count++;
    //Accelerate the ship slowly
    if(velocity + speed <= maxVelocity && count > 10 && velocity + speed >= 0)
    {
        velocity += speed;
      count = 0;
    }
    //Move the spacecraft in the direction it is facing
    x+=-velocity * Math.cos(Math.toRadians(totalAngle));
    y+=-velocity * Math.sin(Math.toRadians(totalAngle));
    //Check the position of the spacecraft
    posCheck();
  }
  
  public void turn(double angle)//rotates ship
  {    
    this.angle += angle;
    totalAngle+=angle;
  }
  
  public int amountOfBullets()//checks amount of bullets in game
  {
    return nb.size();
  }
  
  public void posCheck()//checks if the ship goes over the edge of the playing area and resets its position to come back
  {
    if (x>=705&&y>=602)
    {
      y = -58;
      x = -58;
    }
    if (x>=705&&y<=-59)
    {
      y = 602;
      x = -58;
    }
    if (x<=-59&&y<=-59)
    {
      y = 602;
      x = 764;
    }
    if (x<=-59&&y>=602)
    {
      y = -58;
      x = 764;
    }
    if (y>=602)
    {
      y = -58;
    }
    if (y<=-59)
    {
      y = 602;
    }
    if(x>=764)
    {
      x = -58;
    }
    if (x<=-59)
    {
      x = 764;
    }
  }
  
  public int getBulletX(int i)//gets bullet X coord
  {
    return nb.get(i).getX();
  }
  
  public int getBulletY(int i)//gets bullet Y coord
  {
    return nb.get(i).getY();
  }
  
  public int getBulletWidth(int i)//gets bullet width
  {
    return nb.get(i).getWidth();
  }
  
  public int getBulletHeight(int i)//gets bullet height
  {
    return nb.get(i).getHeight();
  }
  
  public int getBulletAngle(int i)//gets bullet angle
  {
    return nb.get(i).getAngle();
  }
  
  public double getAngle()//gets ship angle
  {
    return angle;
  }
  
  public int getShipMiddleX()//gets the middle of the ship X coord
  {
    return (int)x + img.getWidth()/2;
  }
  
  public int getShipMiddleY()//gets the middle of the ship Y coord
  {
    return (int)y + img.getHeight()/2;
  }
  public int getShipX()//gets the ship X coord
  {
    return (int)x;
  }
  
  public int getShipY()//gets the ship Y coord
  {
    return (int)y;
  }
  
  public int getShipWidth()//gets the ship width
  {
    return img.getWidth();
  }
  
  public int getShipHeight()//gets the ship height
  {
    return img.getHeight();
  }
}