package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observer;
import java.util.Observable;


public class Interval implements Observer {

  Duration duration;
  Task father;
  LocalDateTime initialDate;
  LocalDateTime finalDate;

  public void update(Observable o, Object ob)
  {

  }

  public LocalDateTime getInitialDate() {
    return initialDate;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }
}
