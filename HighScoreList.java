import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class HighScoreList
{
  private Font font = null;
  private String line = "";
  
  public void setFont(Font newFont)
  {
    font = newFont;
  }
  
  public void saveScore(String score)
  {
    try
    {
      //creates a new instance of the FileWriter and passes it
      //the file you’re writing to
      FileWriter fw = new FileWriter("txt Files/Highscores.txt");
      //creates an instance of PrintWriter and passes it
      //the instance of the FileWriter
      PrintWriter pw = new PrintWriter(fw);
      
      //Write the text to the file
      pw.println(score);
      //close the file
      pw.close();
    }
    catch(IOException e)
    {
      System.err.println("txt Files/HighScores.txt could not be found!");
    }
  }
  
  public void resetScores()
  {
    try
    {
      //creates a new instance of the FileWriter and passes it
      //the file you’re writing to
      FileWriter fw = new FileWriter("txt Files/Highscores.txt");
      //creates an instance of PrintWriter and passes it
      //the instance of the FileWriter
      PrintWriter pw = new PrintWriter(fw);
      
      //Write the text to the file
      pw.println("");
      //close the file
      pw.close();
    }
    catch(IOException e)
    {
      System.err.println("Highscores.txt not found!");
    }
  }
  
  public String readFile()
  {
    try
    {
      //Create a new instance of the FileReader and pass it the
      //file that needs to be read
      FileReader fr = new FileReader("txt Files/Highscores.txt");
      //Create a new instance of the BufferedReader and
      //add the FileReader to it
      BufferedReader br = new BufferedReader(fr);
      //A string variable that will temporarily what you’re reading
      //A dual purpose line! First it reads the next line and then
      //it checks to see if that line was null. If it’s null, then
      //that means you’re at the end of the file.
      while ((line=br.readLine()) != null)
      {
        return line;
      }
      //close the file when you’re done
      br.close();
    }
    catch(IOException e)
    {
      System.err.println("File Not Found!");
    }
    if(line == null)
      line = "";
    return line;
  }
  
  public void paint(Graphics g)
  {
    //Paints the high score screen
    int y = 136; //The spot where high scores are starting to be painted from
    int count = 1;
    
    //Sets the font of the text to the Cosmic Alien font
    g.setFont(font.deriveFont(Font.PLAIN, 30));
    g.setColor(Color.WHITE);
    int width = g.getFontMetrics().stringWidth("High Scores");
    g.drawString("High Scores", 390-(width/2), 106);
    //Reads high scores from the text file
    readFile();
    if (!(line.equals("")))
    {
    String[] strings = line.split(",");//splits the read file into multiple strings
    Long[] scores = new Long[strings.length];
    for(int i = 0; i < scores.length; i++)
    {
      scores[i] = Long.parseLong(strings[i]);//converts the read strings (scores) to longs, to sort properly
    }
    
    //Paint the text of all the high scores
    Arrays.sort(scores, Collections.reverseOrder());//sorts the scores
    for(int i = 0; i < strings.length && !(count>14); i++)//draws up to 14 highscores
    {
      strings[i] = Long.toString(scores[i]);
      g.drawString(count+".", 10, y+32);
      width = g.getFontMetrics().stringWidth(strings[i]);
      g.drawString(strings[i], 764-width, y+32);
      y+=32;
      count++;
    }
    }
    else
    {
     width = g.getFontMetrics().stringWidth("No scores! Go earn some!");//if there are no scores to draw, this is displayed
      g.drawString("No scores! Go earn some!", 390-(width/2), 320); 
    }
  }
}