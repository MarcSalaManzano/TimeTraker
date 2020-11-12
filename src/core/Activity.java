package core;

import Visitor.Visitor;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
La clase Activity actua como elemento "Component", en el patrón de diseño COMPOSITE formado por las clases
Activity y Project, en nustro proyecto.
También actua como elemento "Element", en el patrón de diseño VISITOR, siendo la clase de la que heredan sus
"ConcretsElements".
Por otro lado, se almacenan los valores necesarios cada vez que se añade una actividad a la lista y controla
su orden de actuación dependiendo del tipo de actividad que sea; un Proyecto padre, una Tarea hije, etc.
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

  public Activity(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    this.name = name;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
    this.duration = duration;
  }

  public void addFather(Activity a) {
    this.father = a;
  }

  public Activity getFather() {
    return father;
  }

  public LocalDateTime getInitialDate() {
    return initialDate;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }

  /*setInitialDate, setFinalDate y addDuration actualizan dichos atributos del proyecto, la tarea o el intervalo que llama a la función
  y propaga los cambios al proyecto o tarea padre hasta root*/
  public void setInitialDate(LocalDateTime initialDate) {
    this.initialDate = initialDate;
    if (father != null) {
      if (this.getFather().getInitialDate() == null) {
        this.getFather().setInitialDate(initialDate);
        this.getFather().setFinalDate(initialDate);
      }
    }
  }

  public void setFinalDate(LocalDateTime finalDate) {
    this.finalDate = finalDate;
    if (father != null) {
      father.setFinalDate(finalDate);
    }
  }

  public void addDuration(Duration duration) {
    this.duration = this.duration.plus(duration);
    if (father != null) {
      father.addDuration(duration);
    }
  }

  public long getDuration() {
    return duration.getSeconds();
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String name = this.getName();
    String fatherName = this.getFather() == null ? "null" : this.getFather().getName();
    String startTime =
        this.getInitialDate() == null ? "null" : this.getInitialDate().format(formatter);
    String finalDate = this.getFinalDate() == null ? "null" : this.getFinalDate().format(formatter);
    long duration = this.getDuration();
    String description =
        String.format(
            "Activity %-15s child of %-15s %-20s %-20s %d%n",
            name, fatherName, startTime, finalDate, duration);
    return description;
  }

  public abstract Object acceptVisitor(Visitor v);

  public abstract Activity find(String name);
}
