package core;

import Visitor.Visitor;
import java.util.ArrayList;
import java.util.List;


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
  private List<String> tags = new ArrayList();

  public Activity(String name) throws IllegalArgumentException {
    if (name == null || name == "") {
      throw new IllegalArgumentException("Null or empty name in Activity constructor");
    }
    this.name = name;
    duration = Duration.ZERO;
  }

  public Activity(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) throws IllegalArgumentException{
    if (name == null || name == "" ) {
      throw new IllegalArgumentException("Null or empty name in Activity constructor");
    }
    if (duration.isZero() || duration.isNegative()) {
      throw new IllegalArgumentException("duration smaller or equal than 0 in Activity constructor");
    }
    if (finalDate.isBefore(initialDate) || finalDate.equals(initialDate)) {
      throw new IllegalArgumentException("initialDate greater than finalDate in Activity constructor");
    }
    this.name = name;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
    this.duration = duration;
  }

  public void addFather(Activity a) {
    this.father = a;
  }

  public void addTag(String tag) {
    tags.add(tag);
  }

  public List<String> getTags() {
    return tags;
  }

  public void removeTag(String tag) {
    tags.remove(tag);
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
  public void setInitialDate(LocalDateTime initialDate) throws IllegalArgumentException {
    if (initialDate.isBefore(LocalDateTime.of(0, 0, 0, 0, 0))) {
      throw new IllegalArgumentException("initialDate is negative at setFinalDate from Activity");
    }
    this.initialDate = initialDate;
    if (father != null) {
      if (this.getFather().getInitialDate() == null) {
        this.getFather().setInitialDate(initialDate);
        this.getFather().setFinalDate(initialDate);
        assert (getFather().getInitialDate() != getFather().getFinalDate()): "initialDate and finalDate are not equal at initialDate initialization at setInitialDate from Activity";
      }
    }
  }

  public void setFinalDate(LocalDateTime finalDate) throws IllegalArgumentException {
    if (finalDate.isBefore(LocalDateTime.of(0, 0, 0, 0, 0))) {
      throw new IllegalArgumentException("finalDate is negative at setFinalDate from Activity");
    }
    this.finalDate = finalDate;
    if (father != null) {
      father.setFinalDate(finalDate);
      assert (father.getFinalDate() == null): "father finalDate is null at setFinalDate from Activity";
    }
  }

  public void addDuration(Duration duration) throws IllegalArgumentException {
    if (duration.isZero() || duration.isNegative()) {
      throw new IllegalArgumentException("duration is smaller or equal to 0 in addDuration from Activity");
    }
    this.duration = this.duration.plus(duration);
    if (father != null) {
      father.addDuration(duration);
      assert (father.getFinalDate() == null): "father finalDate is null at setFinalDate from Activity";
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
    assert (description == null || description == "" ): "description is null at toString from Activity";
    return description;
  }

  public abstract Object acceptVisitor(Visitor v);

  public abstract Activity find(String name);
}
