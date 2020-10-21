package core;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
Parte del patrón Composite (Activity y Project).
En esta clase, se guardan los valores de cada actividad añadida. Además se controla el orden de actuación,
si tiene que ser un proyecto padre, o una tasca hija, por ejemplo.
 */

public abstract class Activity {
  private String name;
  private Duration duration;
  private Activity father;
  private LocalDateTime initialDate = null;
  private LocalDateTime finalDate = null;

  public Activity(String name) {
    this.name = name;
    duration = Duration.ZERO;
  }

  public Activity(String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    this.name = name;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
    this.duration = duration;
  }

  public void addFather(Activity a) { this.father = a;}

  public Activity getFather() {
    return father;
  }

  public LocalDateTime getInitialDate() { return initialDate; }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }

  public void setInitialDate(LocalDateTime initialDate) {
    this.initialDate = initialDate;
    if(father != null) {
      if (this.getFather().getInitialDate() == null) {
        this.getFather().setInitialDate(initialDate);
        this.getFather().setFinalDate(initialDate);
      }

    }
  }

  public void setFinalDate(LocalDateTime finalDate) {
    this.finalDate = finalDate;
    if(father != null) {
      father.setFinalDate(finalDate);
    }
  }

  public void addDuration(Duration duration) {
    this.duration = this.duration.plus(duration);
    if(father != null) {
      father.addDuration(duration);
    }
  }

  public long getDuration() {
    return duration.getSeconds();
  }

  public String getName() { return this.name; }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String name = this.getName();
    String fatherName = this.getFather() == null ? "null" : this.getFather().getName();
    String startTime = this.getInitialDate() == null ? "null" : this.getInitialDate().format(formatter);
    String finalDate = this.getFinalDate() == null ? "null" : this.getFinalDate().format(formatter);
    long duration = this.getDuration();
    String description = String.format("Activity %-15s child of %-15s %-20s %-20s %d%n", name, fatherName, startTime, finalDate, duration);
    if(father != null) {
      description = description+father.toString();
    }
    return description;
  }

  public abstract JSONObject acceptVisitor(Visitor v);

  public abstract Activity find(String name);
}
