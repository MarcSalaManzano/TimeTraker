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
  private final Timer TIMER;
  private LocalDateTime date;
  private static Clock clock;

  private Clock() {
    TIMER = new Timer();
    date = LocalDateTime.now();
  }

  public LocalDateTime getDate(){
    return date;
  }


  public void start() {
    /*
    Función que crea un TimerTask que se ejecuta cada 2 segundos en un thread separado, este es el encargado de recuperar la fecha actual y
    avisar a los Observadores.
    */
    TimerTask tt = new TimerTask() {
      @Override
      public void run() {
        date = LocalDateTime.now();
        setChanged();
        notifyObservers(date);
      }
    };
    TIMER.scheduleAtFixedRate(tt, 0, 2*1000);
  }


  //metodo para parar el reloj y finalizar el test
  public void cancel() {
    /*
    Función que para la ejecucion del TimerTask.
    */
    TIMER.cancel();
  }


  public static Clock getInstance() {
    /*
    Función necesaria para el patrón Singleton, esta devuelve la instancia del reloj en caso de que exista
    o la crea y devuelve (Lazy initialization).
    */
    if(clock == null) {
      clock = new Clock();
    }
    return clock;
  }

}