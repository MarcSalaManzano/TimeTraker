import core.Clock;

import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

public class Client implements Observer {

  @Override
  public void update(Observable o, Object arg) {
    System.out.println((LocalDateTime) arg);
  }

  public void start() {
    Clock c = new Clock();
    c.addObserver(this);
    c.start();
    try {
      Thread.sleep(1000*10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    c.cancel();
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.start();
  }
}