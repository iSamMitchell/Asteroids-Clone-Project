import java.awt.*;

public class ScoreCounter
{
  private Font font = null;
  private int width = 0;
  private long score = 0;
  
  public void setFont(Font newFont)
  {
    //Sets the font of the scorecounter
    font = newFont;
  }
  
  public void increment(int increment)
  {
    //Increments the value displayed on the score counter by the values collected from destroying asteroids
    for (int i = increment; 0<i; i--)
    {
      score++;
    }
  }
  
  public long getScore() //Returns the score
  {
    return score;
  }
  
  public void paint(Graphics g)
  {
    //Sets the font of the score counter
    g.setFont(font.deriveFont(Font.PLAIN, 30));
    //Sets the color of the score counter
    g.setColor(Color.WHITE);
    //calculates the width to make sure this value can be used to subtract from the edge so it is always painted
    //where the score is not cut off
    width = g.getFontMetrics().stringWidth(Long.toString(score));
    //Draws the score on to the screen
    g.drawString(Long.toString(score),764-width,30);
  }
}