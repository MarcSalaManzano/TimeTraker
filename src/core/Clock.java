package core;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/*Class Clock observable. Notifica a los observadores (Proyecto, tarea e intervalo),
en los instantes de tiempo que deben actualizar la información.
Este paatron de diseño nos permite ahorrar codigo
*/

public class Clock extends Observable {
  private Timer timer;
  private LocalDateTime date;
  private static Clock clock;

  private Clock() {
    timer = new Timer();
    date = LocalDateTime.now();
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

  public static Clock getInstance() {
    if(clock == null) {
      clock = new Clock();
    }
    return clock;
  }

}