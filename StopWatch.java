/*
 * Class: StopWatch
 * Orginal Author: Unknown.  Source: http://stackoverflow.com/questions/8255738/is-there-a-stopwatch-in-java
 * Modified by: Mr. Donald, used by Samuel Mitchell and Kevin Zhu for Space Shooter 2015
 * Date Modified: Nov 18, 2014
 * 
 * Description:
 *   start() ->  Starts the timer
 *   stop()  ->  Stops the timer
 *   long getElapsedTime()  ->  returns the difference between stop() and star() as a long
 * 
 */

public class StopWatch {
  
  private long startTime = 0;
  private long stopTime = 0;
  
  
  public void start() //Starts the timer by setting the start time to the current system time
  {
    this.startTime = System.nanoTime();
  }
  
  
  public void stop() //sets the current system time to stoptime
  {
    this.stopTime = System.nanoTime(); 
  }
  
  
  //elaspsed time in milliseconds (nanosecond/10*10*6)
  public long getElapsedTime()
  {
    long elapsed;   
    elapsed = (stopTime - startTime)/1000000;    
    return elapsed;
  }
  
  //elapsed time in seconds (nanosecond/10*10*9)
  public long getElapsedTimeSeconds()
  {
    long elapsed;
    elapsed = (System.nanoTime() - startTime)/1000000000;//returns the time passes since the timer was started
    return elapsed;
  }
  
}