import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.*;


public class AboutGameScreen
{
  private String line;
  private BufferedImage img = null;
  private BufferedImage imgTwo = null;
  private BufferedImage imgThree = null;
  private Font font = null;
  
  public void setFont(Font newFont)
  {
    font = newFont;
  }
  
  
  public String readFile()
  {
    try
    {
      //Create a new instance of the FileReader and pass it the
      //file that needs to be read
      FileReader fr = new FileReader("txt Files/About Game.txt");
      //Create a new instance of the BufferedReader and
      //add the FileReader to it
      BufferedReader br = new BufferedReader(fr);
      //A string variable that will temporarily what you’re reading
      //A dual purpose line! First it reads the next line and then
      //it checks to see if that line was null. If it’s null, then
      //that means you’re at the end of the file.
      while((line = br.readLine()) != null)
      {
        return line;
      }
      //close the file when you’re done
      br.close();
    }
    catch(IOException e)
    {
      System.err.println("About Game.txt could not be found!");
    } 
    return line;
  }
  
  //paints the 8 bit portraits, names and titles, and the about game paragraph
  public void paint(Graphics g)
  {
    readFile();
    String strings[] = line.split("Q");
    int y = 400;
    int x = 100;
    g.setFont(font.deriveFont(Font.PLAIN, 30));
    g.setColor(Color.WHITE);
    int width = g.getFontMetrics().stringWidth("About Game");
    g.drawString("About Game", 390-(width/2), 106);
    g.setFont(font.deriveFont(Font.PLAIN, 16));
    for(int i = 0; i<strings.length; i++)
    {
      g.drawString(strings[i], 20, y+=32);
    }
    for (int i = 0; i<3; i++)
    {
      g.drawOval(x, 175, 180, 180);
      x+=200;
    }
    width = g.getFontMetrics().stringWidth("Samuel Mitchell"); //uses the current font and a string to find the width of
    //the specific string
    g.drawString("Samuel Mitchell", 190-(width/2), 375);
    width = g.getFontMetrics().stringWidth("Team Manager");
    //Load in the image of sam
    try {
      img = ImageIO.read(new File("Images/Sam.png"));
    } 
    catch (IOException e) {
      System.err.println("File cannot be found!");
    }
    //Draw the avatar
    g.drawImage(img, 100, 175, null);
    //Draw text
    g.drawString("Team Manager", 190-(width/2), 391);
    width = g.getFontMetrics().stringWidth("Kevin Zhu");
    g.drawString("Kevin Zhu", 390-(width/2), 375);
    width = g.getFontMetrics().stringWidth("Team Member");
    //Load in the image of kevin
    try {
      imgTwo = ImageIO.read(new File("Images/Kevin.png"));
    } 
    catch (IOException e) {
      System.err.println("File cannot be found!");
    }
    //Draw the avatar
    g.drawImage(imgTwo, 300, 175, null);
    //Draw text
    g.drawString("Team Member", 390-(width/2), 391);
    width = g.getFontMetrics().stringWidth("Noah Aziz");
    g.drawString("Noah Aziz", 590-(width/2), 375);
    width = g.getFontMetrics().stringWidth("Team Member");
    g.drawString("Team Member", 590-(width/2), 391);
    //Load in the image of noah
    try {
      imgThree = ImageIO.read(new File("Images/Noah.png"));
    } 
    catch (IOException e) {
      System.err.println("File cannot be found!");
    }
    //Draw the avatar
    g.drawImage(imgThree, 500, 175, null);
    
  }
}