import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.io.*;
import java.awt.*;


public class InstructionScreen
{
  private String line;
  private BufferedImage img = null;
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
      FileReader fr = new FileReader("txt Files/Instructions.txt");
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
      System.err.println("txt Files/Instructions.txt could not be found!");
    } 
    return line;
  }
  
  public void paint(Graphics g)
  {
    //Reads the instructions off the text file
    readFile();
    String strings[] = line.split("Q");//splits the instruction file at spots with a "Q" into multiple strings to read
    //and draw
    int y = 211;
    
    //sets the font for the instructions
    g.setFont(font.deriveFont(Font.PLAIN, 30));
    g.setColor(Color.WHITE);
    int width = g.getFontMetrics().stringWidth("Instructions");
    try {
      img = ImageIO.read(new File("Images/Quick Controls.png"));
    } 
    catch (IOException e) {
      System.err.println("File cannot be found!");
    }
    //Paints the instruction screen
    g.drawImage(img, 390-(img.getWidth()/2), 155, null);
    g.drawString("Instructions", 390-(width/2), 106);
    g.setFont(font.deriveFont(Font.PLAIN, 14));
    for(int i = 0; i<strings.length; i++)
    {
      g.drawString(strings[i], 20, y+=25);
    }
  }
}