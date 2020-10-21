package core;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Observer;
import java.util.Observable;

/*
Un intervalo contiene su propia fecha final, inicial y el tiempo durante el cual
ha estado activo.

 */
public class Interval implements Observer {

  private Duration duration;
  private Task father;
  private LocalDateTime initialDate;
  private LocalDateTime finalDate;

  public Interval(Task father){
    duration = Duration.ZERO;
    this.father = father;
    //Coge la hora de Clock e inicializa initalDate y finalDate para calcular Duration
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
    //Explicacion ...
    LocalDateTime newDate = (LocalDateTime) ob;
    Duration newDuration = Duration.between(finalDate, newDate); //suma la diferencia de tiempo transcurrido a la duración actual*
    duration = duration.plus(newDuration);

    father.addDuration(newDuration);  //suma la duración a la task padre
    finalDate = newDate; //Actualiza la finalDate con la nueva fecha actual
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
