package core;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Observer;
import java.util.Observable;


public class Interval implements Observer {

  private Duration duration;
  private Task father;
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;

  public Interval(Task father)
  {
    duration = Duration.ZERO;
    this.father = father;
    //get the clock's time for initialDate and initialize finalDate for Duration's calculations
    initialDate = Clock.getInstance().getDate();
    finalDate = Clock.getInstance().getDate();
  }

  @Override
  public void update(Observable o, Object ob)
  {
    LocalDateTime newDate = (LocalDateTime) ob;
    Duration newDuration = Duration.between(finalDate, newDate); //adds the duration to the current Interval duration
    duration = duration.plus(newDuration);

    father.addDuration(newDuration);  //adds the duration to the task's duration

    finalDate = newDate; //updates the current finalDate
  }

  public LocalDateTime getInitialDate() {
    return initialDate;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }
}
