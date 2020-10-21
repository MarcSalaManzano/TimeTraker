package core;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/*
Class Clock que sigue el patron Observer, representando la clase Observable. 
Este patron de diseño nos permite crear la clase Clock sin que sea necesario que esta conozca a las clases observadoras.
Tambien sigue el patron creacionalSingleton, ya que solo es necesario un reloj en el programa.
Notifica a los observadores (interval y Client), en los instantes de tiempo que deben actualizar la información.

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