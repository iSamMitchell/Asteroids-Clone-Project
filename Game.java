import java.awt.*;
import java.io.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList; 
import javax.sound.sampled.*;
import java.net.URL;

public class Game
{
  //Variables
  private Font font = null;
  private int asteroidX = 0, asteroidY = 0, count = 0;
  private String explosionSound = "";
  private boolean gameRunning = true, firstCollision = false, asteroidsSet = false, accel = false;
  private double turn, speed;
  //Objects
  private UserSpacecraft uCraft = new UserSpacecraft(1.0);
  private ScoreCounter sc = new ScoreCounter();
  private StopWatch timer = new StopWatch();
  private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
  
  public void collisions()//creates hitboxes for the objects in the game and checks if they collide
  {
    int x1 = uCraft.getShipX()+19 + 10;//the x coordinate of the ship hitbox
    int y1 = uCraft.getShipY()+23 + 10;//the y coordinate of the ship hitbox
    int x1Two = 0; //the x coordinate of the asteroids' hitboxes
    int y1Two = 0;//the y coordinate of the asteroids' hitboxes
    int x2 = x1 + 59 - 10; //the second x coordinate of the ship hitbox
    int y2 = y1 + 59 - 10;//the second y coordinate of the ship hitbox
    int x2Two = 0;//the second x coordinate of the asteroids' hitboxes
    int y2Two = 0;//the second y coordinate of the asteroids' hitboxes
    int x1Three = 0; //the x coordinate of the normal bullet hitbox
    int y1Three = 0; //the y coordinate of the normal bullet hitbox
    int x2Three = 0; //the second x coordinate of the normal bullet hitbox
    int y2Three = 0; //the second y coordinate of the normal bullet hitbox
    
    for (int i = 0; i<asteroids.size(); i++)
    {
      x1Two = asteroids.get(i).getX(); //the x coordinate of the asteroids' hitboxes
      y1Two = asteroids.get(i).getY();//the y coordinate of the asteroids' hitboxes
      x2Two = asteroids.get(i).getX() + asteroids.get(i).getWidth();//the second x coordinate of the asteroids' hitboxes
      y2Two = asteroids.get(i).getY() + asteroids.get(i).getHeight();//the second y coordinate of the asteroids' hitboxes
      
      if (!(x2 < x1Two + 3 || y2 < y1Two || x1 > x2Two - 3 || y1 > y2Two))//checks if the the ship and asteroid collide
      {
        if(asteroids.get(i).getAsteroidSize() == 3)
        {
          if(asteroidsSet == false)
            firstCollision = true;
          explosionSound = "Small Explosion";
          if(asteroidsSet == true)
            explode();
          asteroids.set(i, null);
          asteroids.remove(asteroids.get(i));
          if(asteroidsSet == true)
            sc.increment(25);
          gameRunning = false;
        }
        
        else
        Outer://label for outer else statment
        {
          
          if (asteroids.get(i).getAsteroidSize() == 1)
          {
            if(asteroidsSet == false)
              firstCollision = true;
            explosionSound = "Large Explosion";
            if(asteroidsSet == true)
              explode();
            asteroids.get(i).setLastSize(1);
            asteroids.get(i).blowUp();
            if(asteroidsSet == true)
              sc.increment(5);
            gameRunning = false;
            break Outer;//allows the outer else statment to be broken out of after this if-statement is run (labelled break statement)
          }
          if (asteroids.get(i).getAsteroidSize() == 2)
          {
            if(asteroidsSet == false)
              firstCollision = true;
            explosionSound = "Medium Explosion";
            if(asteroidsSet == true)
              explode();
            asteroids.get(i).setLastSize(0);
            asteroids.get(i).blowUp();
            if(asteroidsSet == true)
              sc.increment(15);
            gameRunning = false;
            break Outer;
          }
        }     
      }
      for (int j = 0; j < uCraft.amountOfBullets(); j++)
      {
        x1Three = uCraft.getBulletX(j); //the x coordinate of the normal bullet hitbox
        y1Three = uCraft.getBulletY(j);//the y coordinate of the normal bullet hitbox
        x2Three = x1Three + uCraft.getBulletWidth(j); //the second x coordinate of the normal bullet hitbox
        y2Three = y1Three + uCraft.getBulletHeight(j);//the second y coordinate of the normal bullet hitbox
        if (!(x2Two - 3 < x1Three || y2Two < y1Three || x1Two + 3 > x2Three || y1Two > y2Three))
        {
          if(asteroids.get(i).getAsteroidSize() == 3)
          {
            explosionSound = "Small Explosion";
            explode();
            uCraft.removeNB(j);
            asteroids.set(i, null);
            asteroids.remove(asteroids.get(i));
            sc.increment(25);
          }
          
          else
          Outer://label for outer else statment
          {
            if (asteroids.get(i).getAsteroidSize() == 1)
            {
              explosionSound = "Large Explosion";
              explode();
              uCraft.removeNB(j);
              asteroids.get(i).setLastSize(1);
              asteroids.get(i).blowUp();
              sc.increment(5);
              
              break Outer;//allows the outer else statment to be broken out of after this if-statement is run (labelled break statement)
            }
            if (asteroids.get(i).getAsteroidSize() == 2)
            {
              explosionSound = "Medium Explosion";
              explode();
              uCraft.removeNB(j);
              asteroids.get(i).setLastSize(0);
              asteroids.get(i).blowUp();
              sc.increment(15);
              
              break Outer;
            }
          }
        }
      }
      
    }
    //To stop asteroids from clumping, asteroid-asteroid collision is activated 5 seconds after initial spawn of asteriods.
    if (asteroids.size()>= 2 && timer.getElapsedTimeSeconds() >= 5)
    {
      for (int i = 0; i < asteroids.size(); i++)
      {
        for(int x = 0; x < asteroids.size() && x != i; x++)
        {
          int ax1 = asteroids.get(i).getX(); //left side
          int ay1 = asteroids.get(i).getY();//bottom side
          int ax2 = asteroids.get(i).getX() + asteroids.get(i).getWidth();//right side
          int ay2 = asteroids.get(i).getY() + asteroids.get(i).getHeight();//top side
          
          int bx1 = asteroids.get(x).getX(); //left side
          int by1 = asteroids.get(x).getY();//bottom side
          int bx2 = asteroids.get(x).getX() + asteroids.get(x).getWidth();//right side
          int by2 = asteroids.get(x).getY() + asteroids.get(x).getHeight();//top side
          
          //Find the hitbox of the two asteroids that is closer to x = 0 and y = 0
          int distSideRight = Math.max(ax1, bx1);
          int distSideLeft = Math.min(ax2, bx2);
          int distSideBottom = Math.max(ay1, by1);
          int distSideTop = Math.min(ay2, by2);
          
          //Check the distances of the sides to determine if there is an intersection
          if(!(distSideLeft < distSideRight || distSideTop < distSideBottom)) 
          { 
            int angleA1 = asteroids.get(i).getAngle();
            int angleA2 = asteroids.get(x).getAngle();
            
            double speedA1 = asteroids.get(i).getSpeed();            
            double speedA2 = asteroids.get(x).getSpeed();
            
            int sizeA1 = asteroids.get(i).getAsteroidSize();
            int sizeA2 = asteroids.get(x).getAsteroidSize();
            
            //If the asteroid is larger and moving faster, switch speed on contact.
            if((speedA1 > speedA2 && sizeA1 > sizeA2)||(sizeA1 == sizeA2))
            {
              asteroids.get(i).setSpeed(speedA2);
              asteroids.get(x).setSpeed(speedA1);
            }
            
            //Switch the angles of the asteroids to give the illusion of bouncing
            asteroids.get(i).setAngle(angleA2);
            asteroids.get(x).setAngle(angleA1);
          }
        }
      }
    }
  }
  
  public void setFont(Font newFont)//sets font for in-game text
  {
    font = newFont;
  }
  
  public void setAsteroids()//spawns asteroids into the game
  {
    //Create 18 asteroids
    for (int i = 0; i<18; i++)
    {
      do
      {
        //Randomly create asteroids in the corners of the screen
        firstCollision = false;
        asteroidX = (int)(1000 * Math.random()) + 764;
        asteroidY = (int)(1000 * Math.random()) + 602;
        int asteroidAngle = (int)(360 * Math.random()) + 0;
        int asteroidSpeed = (int)(20 * Math.random()) + 5;
        int asteroidSpin = (int)(100 * Math.random()) + 0;
        if(asteroidSpin > 50)
        {
          asteroidSpin -= 50;
          asteroidSpin = asteroidSpin * -1;
        }
        asteroids.add( new Asteroid(asteroidX, asteroidY, asteroidAngle, asteroidSpeed / 100.0, asteroidSpin / 100.0));
        collisions();
      } while(firstCollision);
      //Makes sure no asteriods spawn ontop of the ship in the corners
    }
    while (asteroids.size()>18)
    {
      asteroids.set(0, null);
      asteroids.remove(asteroids.get(0));
    }
    
    asteroidsSet = true;
    //Start a timer after the asteroids have been spawned to allow asteroids to spread out
    //instead of bunching together from the asteroid collision checking
    timer.start();
  }
  
  public void paint(Graphics g)
  {
    //Only paint the PLAYABLE game if it is running
    if (gameRunning)
    {
      while (count ==0)
      {
        //Create the asteroids
        setAsteroids();
        count++;
      }
      uCraft.turn(turn);
      uCraft.paint(g);
      
      //Move the ship if the up arrow key is pressed
      if(accel)
      {
        speed = 0.05;
      }
      else
      {
        speed = -0.051;
      }
      uCraft.move(speed);
      
      //Move all the asteroids
      for (int i = 0; i<asteroids.size(); i++)
      {
        asteroids.get(i).move();
        asteroids.get(i).paint(g);
      }
      
      //If there are asteroids on the screen check collisions
      if (count!=0)
      {
        collisions();
      }
      sc.setFont(font);
      sc.paint(g);
      if (asteroids.size()==0)
      {
        count = 0;
      }
      //Draws the bullet counter at the top left corner of the screen during the game
      g.setColor(Color.WHITE);
    if(uCraft.amountOfBullets() < 3)
      g.fillRect(26, 28, 10, 10);
    if(uCraft.amountOfBullets() < 2)
      g.fillRect(46, 28, 10, 10);
    if(uCraft.amountOfBullets() < 1)
      g.fillRect(66, 28, 10, 10);
    }
    else //paints the Game Over messages when destroyed, and nothing else is painted
    {
      count++;
      if (count%2 == 0)
      {
        g.setColor(Color.WHITE);
      }
      else
        g.setColor(Color.YELLOW);
      //Draws game over message when you die
      g.setFont(font.deriveFont(Font.PLAIN, 100));
      int width = g.getFontMetrics().stringWidth("GAME OVER!");
      g.drawString("GAME OVER!", 390-(width/2), 221);
      g.setFont(font.deriveFont(Font.PLAIN, 18));
      width = g.getFontMetrics().stringWidth("(Unfortunately your spacecraft has been destroyed)");
      g.drawString("(Unfortunately your spacecraft has been destroyed)", 390-(width/2), 239);
      g.setFont(font.deriveFont(Font.PLAIN, 25));
      width = g.getFontMetrics().stringWidth("Your Score: "+ sc.getScore());
      if (count%2 == 0)
      {
        g.setColor(Color.BLUE);
      }
      else
        g.setColor(Color.GREEN);
      //Gets the user's score for this game
      g.drawString("Your Score: "+ sc.getScore(), 390-(width/2), 266);
      width = g.getFontMetrics().stringWidth("Click YES to save your score.");
      g.setColor(Color.WHITE);
      g.drawString("Click YES to save your score.", 390-(width/2), 341);//offers the option for the score to be saved
      width = g.getFontMetrics().stringWidth("Click BACK to return to the main menu.");
      g.drawString("Click BACK to return to the main menu.", 390-(width/2), 366); 
    }
  }
  
  public void keyPressed(KeyEvent e)
  {
    if (gameRunning)
    {
      if (e.getKeyCode() == KeyEvent.VK_RIGHT ) 
      {
        //Changes the angle of the user spacecraft
        turn = 0.5;
      } 
      else if (e.getKeyCode() == KeyEvent.VK_LEFT ) 
      {
        turn = -0.5;
      } 
      else if (e.getKeyCode() == KeyEvent.VK_UP ) 
      {
        //Accelerates the spacecraft
        thrustSound();//plays thrusting sound
        accel = true;
      } 
      else if (e.getKeyCode() == KeyEvent.VK_SPACE) 
      {
        //Creates bullets when the spacebar is pressed, as long as the user is not exceeding 3 at a time
        if(uCraft.amountOfBullets() >= 0 && uCraft.amountOfBullets() < 3)
        {
        uCraft.setNB(uCraft.getShipMiddleX(), uCraft.getShipMiddleY(), 2, uCraft.getAngle());
        shootSound();
        }
      }      
    }
  }
  
  //Register key releases, this is so multiple keys can be pressed at the same time
  public void keyReleased(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_RIGHT ) 
    {
      turn = 0.0;
    } 
    else if (e.getKeyCode() == KeyEvent.VK_LEFT ) 
    {
      turn = 0.0;
    } 
    else if (e.getKeyCode() == KeyEvent.VK_UP ) 
    {
      if (gameRunning)
      {
        accel = false;
      }
    } 
  }
  
  public void shootSound()
  {
    try {
      // Open an audio input stream.
      URL url = this.getClass().getClassLoader().getResource("Sounds/Shoot Sound.wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
      // Get a sound clip resource.
      // Open audio clip and load samples from the audio input stream.
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.start();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
  
  public void explode()
  {
    try {
      // Open an audio input stream.
      URL url = this.getClass().getClassLoader().getResource("Sounds/"+explosionSound+".wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
      // Get a sound clip resource.
      // Open audio clip and load samples from the audio input stream.
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.start();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
  
  public void thrustSound()
  {
    try {
      // Open an audio input stream.
      URL url = this.getClass().getClassLoader().getResource("Sounds/Thrust Sound.wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
      // Get a sound clip resource.
      // Open audio clip and load samples from the audio input stream.
      Clip clip = AudioSystem.getClip();
      clip.open(audioIn);
      clip.start();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
  
  public boolean getState()//returns the state of the game
  {
    return gameRunning;
  }
  
  public long getScore()//returns the score of the player
  {
    return sc.getScore();
  }
}