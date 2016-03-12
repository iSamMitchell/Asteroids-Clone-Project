import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.awt.event.MouseEvent;

public class Button
{
  private String type, color;
  private int x, y, borderSize, alpha;
  private int whereMouseX, whereMouseY;
  private boolean isVisible, isActive, isClicked;
  private BufferedImage img = null;
  
  
  public Button(String type, String color, int x, int y, int borderSize, int alpha, boolean isVisible, boolean isActive)
  {
    //Initialize all the attributes of the button
    this.type = type;
    this.color = color;
    this.x = x;
    this.y = y;
    this. borderSize = borderSize;
    this.isVisible = isVisible;
    this.isActive = isActive;
    this.alpha = alpha;
    //Loads in the image for button, the image can be changed when the button is initialized
    try {
      img = ImageIO.read(new File("Images/"+type+".png"));
    } 
    catch (IOException e) {
      System.err.println("Images/"+type+".png could not be found!");
    }
  }
  
  
  public void buttonSound()
  {
    try {
      // Open an audio input stream.
      URL url = this.getClass().getClassLoader().getResource("Sounds/Button Sound.wav");
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
  
  public void paint(Graphics g)
  {
    //lights up the image when clicked on
    if(isVisible)
    {
      if(isActive || type.equals("Back"))
      {
        Color currentColor = new Color(1, 1, 1, 0);
        if((whereMouseX >= x && whereMouseX <= x + img.getWidth()) &&(whereMouseY >= y && whereMouseY <= y + img.getHeight()))
        {
          //Converts the color value from hexadecimal to RGB
          currentColor = new Color(Integer.valueOf( color.substring( 1, 3 ), 16 ), 
                                   Integer.valueOf( color.substring( 3, 5 ), 16 ), 
                                   Integer.valueOf( color.substring( 5, 7 ), 16 ), alpha);
          
        }
        g.setColor(currentColor);
        g.fillRect(x - borderSize, y - borderSize, img.getWidth() + borderSize * 2, img.getHeight() + borderSize * 2);
      }
      //draws the image
      g.drawImage(img, x, y, null); 
    }
  }
  public void mousePressed(MouseEvent e)
  {
    //checks where the mouse cursor is
    if(isVisible && isActive)
    {
      whereMouseX = e.getX();
      whereMouseY = e.getY();
    }
  }
  
  public String getType()//Returns the button "type"/name
  {
    if(isVisible)
    {
      return type;
    }
    return "";
  }
  
  public boolean getClicked()//Returns if the button was clicked or not
  {
    if(isVisible)
      return isClicked;
    return false;
  }
  public void setClicked()
  {
    isClicked = false;
  }
  
  public void setActive(boolean active)//Sets the button to accept mouse clicks or not
  {
    isActive = active;
  }
  
  public void mouseReleased(MouseEvent e)
  {
    //Controls if the button blinks and if the button plays the button sound when clicked and released
    if(isVisible && isActive)
    {
      if((whereMouseX >= x && whereMouseX <= x + img.getWidth()) &&(whereMouseY >= y && whereMouseY <= y + img.getHeight()))
      {
        
        isClicked = true;
        if(isActive)
          buttonSound();
      }
    }
    whereMouseX = 0;
    whereMouseY = 0;
  }   
}