package core;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Observer;
import java.util.Observable;

/*
La clase Interval actua como "Observador", en el patrón de diseño OBSERVER de la clase Clock. De esta
forma se es capaz de actualizar la duración de los intervalos de forma síncrona y precisa.
Por otro aldo, esta clase debe actuar como elemento "ConcretElement" de VISITOR.
Como clase, Interval contiene los datos necesarios para guardar un intervalo de tiempo. Duración del mismo,
fecha inicial y final, y Tarea a la que pertenece este intervalo de tiempo.
 */
public class Interval implements Observer {

  private Duration duration;
  private Task father;
  private final LocalDateTime INITIAL_DATE;
  private LocalDateTime finalDate;

  public Interval(Task father) {
    duration = Duration.ZERO;
    this.father = father;
    // Coge la hora de Clock e inicializa initalDate y finalDate para calcular Duration
    INITIAL_DATE = Clock.getInstance().getDate();
    finalDate = Clock.getInstance().getDate();
  }

  public Interval(LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    this.duration = duration;
    this.INITIAL_DATE = initialDate;
    this.finalDate = finalDate;
  }

  @Override
  public void update(Observable o, Object ob) {
    /*
    Se llama cada vez que Clock hace notify().
    Mira la hora y actualiza su fecha final, la duración actual y la de su Task padre.
     */
    LocalDateTime newDate = (LocalDateTime) ob;
    Duration newDuration =
        Duration.between(
            finalDate.truncatedTo(ChronoUnit.SECONDS),
            newDate.truncatedTo(
                ChronoUnit
                    .SECONDS)); // suma la diferencia de tiempo transcurrido a la duración actual
    duration = duration.plus(newDuration);

    father.addDuration(newDuration); // suma la duración a la task padre
    finalDate = newDate; // Actualiza la finalDate con la nueva fecha actual
    father.setFinalDate(finalDate);
  }

  public LocalDateTime getInitialDate() {
    return INITIAL_DATE;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }

  public Duration getDuration() {
    return duration;
  }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fatherName = this.father == null ? "null" : this.father.getName();
    String startTime =
        this.getInitialDate() == null ? "null" : this.getInitialDate().format(formatter);
    String finalDate = this.getFinalDate() == null ? "null" : this.getFinalDate().format(formatter);
    long duration = this.getDuration().getSeconds();
    return String.format(
        "Interval %-15s child of %-15s %-20s %-20s %d%n",
        "", fatherName, startTime, finalDate, duration);
  }

  public JSONObject acceptVisitor(Visitor v) {
    return v.visitInterval(this);
  }

  public void setFather(Task t) {
    father = t;
  }
}
