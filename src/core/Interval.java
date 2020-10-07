package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observer;
import java.util.Observable;


public class Interval implements Observer {

  private Duration duration;
  private Task father;
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;

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
