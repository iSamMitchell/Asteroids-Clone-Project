import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.awt.Robot;
import java.util.ArrayList;

public class SpaceShooter2015 extends JPanel
{
  //variables
  private int sAmount = (int)(Math.random() * 500 + 250), count = 0, savesRun = 0;
  private int[] starWidth = new int[sAmount];
  private int[] starHeight = new int[sAmount];
  private String type;
  private boolean isClicked, buttonClicked = false, gameRunning, gameOver = false;
  private Font font = null;
  private double roboX = 0.0, roboY = 0.0;
  //objects
  private Button[] buttons = new Button[8];
  private HighScoreList h = new HighScoreList();
  private InstructionScreen i = new InstructionScreen();
  private AboutGameScreen ags = new AboutGameScreen();
  private ArrayList<Game> games = new ArrayList<Game>(1);
  
  public SpaceShooter2015 ()
  {
    loadFont();
    //Create all the star positions
    for(int i = 0; i < sAmount; i++)
    {
      starWidth[i] = (int)(Math.random() * 780 + 0);
      starHeight[i] = (int)(Math.random() * 640 + 0);
    }
    //Parameters of the buttons are: File name, color, shade color, x coord, y coord, border size, visible and functioning.
    buttons[0] = new Button ("Instructions", "#cccccc", 315, 136, 2, 35, true, true);
    buttons[1] = new Button ("Start Game", "#cccccc", 380, 214, 2, 25, true, true);
    buttons[2] = new Button ("High Scores", "#cccccc", 475, 292, 2, 25, true, true);
    buttons[3] = new Button ("About Game", "#cccccc", 381, 386, 2, 25, true, true);
    buttons[4] = new Button ("Logo", "#cccccc", 0, 136, 0, 25, true, false);
    buttons[5] = new Button ("Back", "#cccccc", 10, 10, 2, 65, true, false);
    buttons[6] = new Button ("Yes", "#cccccc", 296, 471, 2, 65, true, false);
    buttons[7] = new Button ("Reset Scores", "#cccccc", 323, 10, 2, 65, true, false);
    
    //mouse listener for all "buttons"
    addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
      }
      @Override
      public void mouseEntered(MouseEvent e) {       
      }
      @Override
      public void mouseExited(MouseEvent e) {
      }
      @Override
      public void mousePressed(MouseEvent e) {
        for (Button b:buttons)
          b.mousePressed(e);
      }
      @Override
      public void mouseReleased(MouseEvent e) {
        for (Button b:buttons)
          b.mouseReleased(e);
      }
    }
    );
    
    //key listener for keys used in game
    addKeyListener(new KeyListener(){
      @Override 
      public void keyTyped(KeyEvent e){ 
      }
      @Override 
      public void keyReleased(KeyEvent e){
        if(games.size() > 0)
        {
          games.get(0).keyReleased(e);
        }
      }
      @Override 
      public void keyPressed(KeyEvent e) { 
        if(games.size() > 0)
        {
          games.get(0).keyPressed(e);
        }
      }
    });
    setFocusable(true);
  }
  
  public void loadFont() //Gets the text font used to paint text
  {
    try
    {
      font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("Font/Cosmic Alien.ttf")));
    }
    catch (Exception ex)//Catch a missing file exception
    {
      ex.printStackTrace();
    }
  }
  
  //paints everything when needed on JPanel
  public void paint (Graphics g)
  {
    //background starting colours
    int red = 1;
    int green = 1;
    int blue = 1;
    //For the whole screen size
    for(int i = 0; i <= 720; i++)
    {
      //Sets a new color slightly lighter in colour per layer of pixel
      Color currentColor = new Color(red, green, blue);
      g.setColor(currentColor);
      g.fillRect(0, i, 780, 1);
      if(red < 255 && green < 255 && blue < 255)
      {  
        //Increments RGB value to give a blending effect
        if(i % 10 < 1 && red > 0){red += 1;}
        if(i % 15 < 1 && green > 0){green += 1;}
        if(i % 12 < 1 && blue > 0){blue += 1;}
      }    
    }
    //Paints a star at set coordinates
    g.setColor(Color.decode("#E5E5FF"));
    for(int i = 0; i < sAmount; i++)
    {
      g.fillRect(starWidth[i], starHeight[i], 1, 1);
    }
    
    //Title Screen Buttons
    if (!buttonClicked)
    {
      for (int i = 0; i < 5; i++)
      {
        buttons[i].paint(g);//paint all but the back button
      }
      buttons[6].setActive(false);
    }
    else
    {
      if(!gameRunning || gameOver)
      {
        buttons[5].paint(g);//paints "Back" button
        buttons[5].setActive(true);
        if(gameOver)
        {
          buttons[6].paint(g);//paints "Yes" button when game is over
          buttons[6].setActive(true);
        }
      }
      //if the game is running, do not paint the buttons and set all buttons to not active
      else
      {
        for (int i = 0; i<8; i++)
          buttons[i].setActive(false);
      }
    }  
    for (Button b:buttons)
    {
      type = b.getType();
      isClicked = b.getClicked();
      //paints the high scores screen
      if(type.equals("High Scores")&&isClicked)
      {
        buttonClicked = true;
        h.setFont(font);
        h.paint(g);
        for (int i = 0; i<5; i++)
          buttons[i].setActive(false);
        buttons[6].setActive(false);
        buttons[5].paint(g);//paints "Back button"
        buttons[5].setActive(true);
        if(!(h.readFile().equals("")))
        {
          buttons[7].paint(g);
          buttons[7].setActive(true);
        }
      }
      //paints the instructions screen
      if(type.equals("Instructions")&&isClicked)
      {
        
        buttonClicked = true;
        i.setFont(font);
        i.paint(g);
        for (int i = 0; i<5; i++)
          buttons[i].setActive(false);
        buttons[6].setActive(false);
        buttons[5].paint(g);//paints "Back button"
        buttons[5].setActive(true);
      }
      //paints the about game screen
      if(type.equals("About Game")&&isClicked)
      {
        buttonClicked = true;
        ags.setFont(font);
        ags.paint(g);
        for (int i = 0; i<5; i++)
          buttons[i].setActive(false);
        buttons[6].setActive(false);
        buttons[5].paint(g);//paints "Back button"
        buttons[5].setActive(true);
      }
      //sets the game to running
      if(type.equals("Start Game")&&isClicked)
      {
        buttonClicked = true;
        gameRunning = true;
        while (count == 0)
        {
          games.add(new Game());
          savesRun = 0;// allows saves to be run again after this game ends
          count++;
          try
          {
            Robot robot = new Robot();//this is used to move the mouse to the corner area of the JFrame only when the
            //game is started, allowing the mouse to be out of the way when the game first starts
            robot.mouseMove((int)roboX+624, (int)roboY);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
        }
      }
      //paints the menu again when clicked
      if(type.equals("Back")&&isClicked)
      {
        buttonClicked = false;
        buttons[5].setActive(false);
        for (int i = 0; i<5; i++)
        {
          if(i!=4)
          {
            buttons[i].setActive(true);
          }
        }
        buttons[7].setActive(false);
        for (int i = 0; i < 8; i++)
          buttons[i].setClicked();
        if(gameOver)
        {
          //if the "Back" button is clicked on the game over screen, delete the last instance of the game and go back
          //to the main menu
          games.set(0, null);
          games.remove(games.get(0));
          count = 0;
          gameOver = false;
        }
      }
      
      //Button which saves score when clicked, only appears on the game over screen
      if (type.equals("Yes")&&isClicked&&gameOver)
      {
        buttons[6].setActive(false);
        g.setFont(font.deriveFont(Font.PLAIN, 25));
        g.setColor(Color.WHITE);
        if (savesRun == 0)
        {
          if(!(h.readFile().equals("")))
            h.saveScore((h.readFile())+","+(games.get(0).getScore()));
          else
            h.saveScore(Long.toString(games.get(0).getScore()));
          savesRun++;
        }
      }
      
      //Overwrites all the text in the high score file if reset score is selected with a blank string, meaning no scores
      //are saved anymore
      if (type.equals("Reset Scores")&&isClicked)
      {
        if(!(h.readFile().equals("")))
          h.resetScores();
        else
          buttons[7].setActive(false);//stops the user from continuously clicking the reset scores button
      }
      
    }
    //paints the PLAYABLE game
    if(gameRunning&& games.size() > 0)
    {
      games.get(0).setFont(font);//sets in game font to Cosmic Alien font
      games.get(0).paint(g);
      gameRunning = games.get(0).getState();
      if(!gameRunning)//checks if the game has resulted in a game over
      {
        gameOver = true;
      }
    }
    
    if(gameOver && savesRun > 0)//Prints a message to tell the user the score has been saved if they chose so
    {
      g.setFont(font.deriveFont(Font.PLAIN, 25));
      //g.setColor(Color.WHITE);
      int width = g.getFontMetrics().stringWidth("Score saved!");
      g.drawString("Score saved!", 390-(width/2), 560);
      width = g.getFontMetrics().stringWidth("Click back to return to the main menu.");
      g.drawString("Click back to return to the main menu.", 390-(width/2), 587);
    }
  }
  
  public void setRoboX(JFrame f)//checks where the JFrame x coord is
  {
    roboX = f.getLocation().getX();
  }
  
  public void setRoboY(JFrame f)//checks where the JFrame y coord is
  {
    roboY = f.getLocation().getY();
  }
  
  public static void main (String[] args)
  {
    //Main class
    JFrame f = new JFrame ("Space Shooter 2015");
    SpaceShooter2015 a = new SpaceShooter2015();
    f.add(a);
    f.setSize(780, 640);
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image image = toolkit.getImage("Images/Cursor.png");
    Cursor c = toolkit.createCustomCursor(image , new Point(a.getX(), a.getY()), "img");//changes cursor to asteroid cursor 
    a.setCursor (c);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
    f.setLocationRelativeTo(null);
    
    while(true)
    {
      a.setRoboX(f);//continuously sets the points for the mouse to go in the event of a new game being created
      a.setRoboY(f);
      a.repaint();
    }
  }
}