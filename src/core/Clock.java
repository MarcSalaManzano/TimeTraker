package core;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

public class Clock extends Observable {
  private Timer timer;
  private LocalDateTime date;
  private static Clock clock;

  public Clock() {
    timer = new Timer();
  }

  public LocalDateTime getDate(){
    return date;
  }

  public void start() {
    TimerTask tt = new TimerTask() {
      @Override
      public void run() {
        date = LocalDateTime.now();
        setChanged();
        notifyObservers(date);
      }
    };
    timer.scheduleAtFixedRate(tt, 0, 2*1000);
  }

  public void cancel() {
    timer.cancel();
  }


}