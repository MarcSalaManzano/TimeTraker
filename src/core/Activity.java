package core;

import java.util.ArrayList;
import java.util.List;

import Visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private Logger logger = LoggerFactory.getLogger("core.Activity");

  public Activity(String name) {
    invalidArguments(name);
    this.name = name;
    duration = Duration.ZERO;
    assert (invariant()):"Invariant violated";
  }

  public Activity(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    invalidArguments(name, initialDate, finalDate, duration);
    this.name = name;
    this.initialDate = initialDate;
    this.finalDate = finalDate;
    this.duration = duration;
    assert (invariant()):"Invariant violated";
  }

  public void addFather(Activity a) {
    assert (invariant()):"Invariant violated";
    this.father = a;
    assert (invariant()):"Invariant violated";
  }

  public void addTag(String tag) {
    assert (invariant()):"Invariant violated";
    tags.add(tag);
    assert (invariant()):"Invariant violated";
  }

  public List<String> getTags() {
    assert (invariant()):"Invariant violated";
    return tags;
  }

  public void removeTag(String tag) {
    assert (invariant()):"Invariant violated";
    tags.remove(tag);
    assert (invariant()):"Invariant violated";
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

  /*setInitialDate, setFinalDate y addDuration actualizan dichos atributos del proyecto,
  la tarea o el intervalo que llama a la función
  y propaga los cambios al proyecto o tarea padre hasta root*/

  public void setInitialDate(LocalDateTime initialDate) {
    assert (invariant()):"Invariant violated";
    invalidArguments(initialDate);
    logger.debug("Activity Initial Date updated | Old Date: " + this.initialDate);
    this.initialDate = initialDate;
    logger.debug("Activity Initial Date updated | New Date: " + this.initialDate);
    if (father != null) {
      if (this.getFather().getInitialDate() == null) {
        this.getFather().setInitialDate(initialDate);
        this.getFather().setFinalDate(initialDate);
        assert (getFather().getInitialDate() != getFather().getFinalDate()): "initialDate and finalDate are not equal at setInitialDate from Activity";
      }
    }
    assert (invariant()):"Invariant violated";
  }

  public void setFinalDate(LocalDateTime finalDate) {
    assert (invariant()):"Invariant violated";
    invalidArguments(finalDate);
    logger.debug("Activity Final Date updated | Old Date: " + this.finalDate);
    this.finalDate = finalDate;
    logger.debug("Activity Final Date updated | New Date: " + this.finalDate);
    if (father != null) {
      father.setFinalDate(finalDate);
      assert (father.getFinalDate() != null): "father finalDate is null at setFinalDate from Activity";
    }
    assert (invariant()):"Invariant violated";
  }

  public void addDuration(Duration duration) {
    assert (invariant()):"Invariant violated";
    invalidArguments(duration);
    logger.debug("Activity Duration updated | Old Duration: " + this.duration);
    this.duration = this.duration.plus(duration);
    logger.debug("Activity Duration updated | New Duration: " + this.duration);
    if (father != null) {
      father.addDuration(duration);
      assert (father.getFinalDate() != null): "father finalDate is null at setFinalDate from Activity";
    }
    assert (invariant()):"Invariant violated";
  }

  public long getDuration() {
    return duration.getSeconds();
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String toString() {
    assert (invariant()):"Invariant violated";
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
    assert (invariant()):"Invariant violated";
    return description;
  }

  public abstract Object acceptVisitor(Visitor v);

  public abstract Activity find(String name);

  public void invalidArguments(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration)
      throws IllegalArgumentException {
    if (name == null || name == "") {
      throw new IllegalArgumentException("Null or empty name in Activity constructor");
    }
    if (duration.isNegative()) {
      throw new IllegalArgumentException(
          "duration smaller or equal than 0 in Activity constructor");
    }
    if (initialDate != null
        && finalDate != null
        && (finalDate.isBefore(initialDate) || finalDate.equals(initialDate))) {
      throw new IllegalArgumentException(
          "initialDate greater than finalDate in Activity constructor");
    }
  }

  public void invalidArguments(String name) throws IllegalArgumentException {
    if (name == null || name == "") {
      throw new IllegalArgumentException("Null or empty name of an activity");
    }
  }

  public void invalidArguments(LocalDateTime date) throws IllegalArgumentException {
    if (date.isBefore(LocalDateTime.of(1, 1, 1, 0, 0))) {
      throw new IllegalArgumentException("initialDate is negative at setFinalDate from Activity");
    }
  }

  public void invalidArguments(Duration duration) throws IllegalArgumentException {
    if (duration.isZero() || duration.isNegative()) {
      throw new IllegalArgumentException(
          "duration is smaller or equal to 0 in addDuration from Activity");
    }
  }

  public void visitorCheck(Visitor vis) throws IllegalArgumentException {
    if (vis == null) {
      throw new IllegalArgumentException("null visitor");
    }
  }

  protected boolean invariant() {
    if (!duration.isZero()) {
      if (finalDate.isBefore(initialDate)) {return false;}
      if (finalDate == null || initialDate == null) {return false;}
    }
    if (name == null || name == "") {return false;}
    return true;
  }
}
