package core;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  public Interval(LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    this.duration = duration;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
  }

  @Override
  public void update(Observable o, Object ob)
  {
    LocalDateTime newDate = (LocalDateTime) ob;
    Duration newDuration = Duration.between(finalDate, newDate); //adds the duration to the current Interval duration
    duration = duration.plus(newDuration);

    father.addDuration(newDuration);  //adds the duration to the task's duration
    finalDate = newDate; //updates the current finalDate
    father.setFinalDate(finalDate);
  }

  public LocalDateTime getInitialDate() {
    return initialDate;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }

  public Duration getDuration() { return duration; }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fatherName = this.father == null ? "null" : this.father.getName();
    String startTime = this.getInitialDate() == null ? "null" : this.getInitialDate().format(formatter);
    String finalDate = this.getFinalDate() == null ? "null" : this.getFinalDate().format(formatter);
    long duration = this.getDuration().getSeconds();
    return String.format("Interval %-15s child of %-15s %-20s %-20s %d%n","", fatherName, startTime, finalDate, duration);
  }

  public JSONObject acceptVisitor(Visitor v) {
    return v.visitInterval(this);
  }

  public void setFather(Task t) {
    father = t;
  }
}
