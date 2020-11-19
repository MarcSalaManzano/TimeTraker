package core;

import Visitor.Visitor;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
Las clase Task, actua como elemento "Leaf", en el patrón de diseño COMPOSITE. Esta clase por su
jerarquía y programación no puede contener hijos, como la clase "Composite". Por ello solo
implementa los métodos de "Component", en nuestro caso de Activity.
También aplica el patrón VISITOR, como elemento "ConcretElement".
Además como clase, una tarea es un tipo de actividad que solo puede contener intervalos de tiempo.
Esto se debe a que el trabajo de un usuario se divide en tareas y estas tareas son contavilizadas
en dichos intervalos. El usuario tiene la capadidad de decidir cuando empezar a trabajar en una
tarea, cancelarla, pararla para luego retomarla, etc. Por ello dentro de cada tarea se guarda el
intervalo de tiempo que se ha dedicado a dicha tarea.
*/
public class Task extends Activity {

  private List<Interval> intervals;
  private boolean status;
  private final Logger logger = LoggerFactory.getLogger("core.Task");

  public Task(String name) {
    super(name);
    logger.debug("Task Constructor | Name: " + name);
    intervals = new ArrayList<>();
  }

  public Task(String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    super(name, initialDate, finalDate, duration);
    logger.debug("Task Constructor | Name: " + name);
    logger.debug("Task Constructor | Initial Date: " + initialDate);
    logger.debug("Task Constructor | Final Date: " + finalDate);
    logger.debug("Task Constructor | Duration: " + duration);
    intervals = new ArrayList<>();
  }

  public boolean getStatus() {
    return status;
  }

  public void startTask() {
    /*
    Al activar la Task, se marca como activa y se crea un nuevo Interval que se añade a su lista de
    intervalos, i este nuevo Interval se añade como Observer al clock. Si es la primera vez que se
    activa, se le asigna la hora actual como hora inicial.
     */
    assert (invariant()) : "Invariant violated";
    LocalDateTime newInitialDate = Clock.getInstance().getDate();
    Interval newInterval = new Interval(this);
    intervals.add(newInterval);
    Clock.getInstance().addObserver(newInterval);
    if (getInitialDate() == null) {
      this.setInitialDate(newInitialDate);
    }
    this.status = true;
    assert (getInitialDate() == null) : "initialDate null when task has started";
    assert (invariant()) : "Invariant violated";
    logger.trace("Task has started");
  }

  public void stopTask() {
    /*
    Al parar la Task borramos el intervalo de la lista de observers y cambiamos el estado a inactivo
    para que no se muestre por pantalla.
     */
    assert (invariant()) : "Invariant violated";
    Clock.getInstance().deleteObserver(intervals.get(intervals.size() - 1));
    this.status = false;
    assert (getInitialDate() == getFinalDate() || getFinalDate().isBefore(getInitialDate()))
        : "initialDate equal or smaller than finalDate when task has stopped";
    assert (invariant()) : "Invariant violated";
    logger.trace("Task has stopped");
  }

  public List<Interval> getIntervals() {
    return this.intervals;
  }

  public Interval getLastInterval() {
    return intervals.get(intervals.size() - 1);
  }

  @Override
  public Object acceptVisitor(Visitor v) {
    visitorCheck(v);
    return v.visitTask(this);
  }

  public void addInterval(Interval interval) throws IllegalArgumentException {
    assert (invariant()) : "Invariant violated";
    if (interval == null) {
      throw new IllegalArgumentException();
    }
    intervals.add(interval);
    interval.setFather(this);
    assert (invariant()) : "Invariant violated";
    logger.trace("Interval added to task " + this.getName());
  }

  public Activity find(String name) {
    /*
    Función que sirve para devolver una Actividad con el nombre pasado por parametro.
    En el caso de Task, si el nombre coincide con el suyo se devuelve a si mismo, en caso contrario
    devuelve un null.
     */
    assert (invariant()) : "Invariant violated";
    invalidArguments(name);
    if (name.equals(this.getName())) {
      return this;
    } else {
      return null;
    }
  }

  protected boolean invariant() {
    if (getDuration() != 0) {
      if (intervals == null) {
        return false;
      }
    }
    return true;
  }
}
