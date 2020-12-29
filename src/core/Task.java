package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visitor.Visitor;

/*
Las clase Task, actua como elemento "Leaf", en el patrón de diseño COMPOSITE. Esta clase por su
jerarquía y programación no puede contener hijos, como la clase "Composite". Por ello solo
implementa los métodos de "Component", en nuestro caso de Activity.
También aplica el patrón VISITOR, como elemento "ConcretElement".
Además como clase, una tarea es un tipo de actividad que solo puede contener intervalos de tiempo.
Esto se debe a que el trabajo de un usuario se divide en tareas y estas tareas son contavilizadas en
dichos intervalos. El usuario tiene la capadidad de decidir cuando empezar a trabajar en una tarea,
cancelarla, pararla para luego retomarla, etc. Por ello dentro de cada tarea se guarda el intervalo
de tiempo que se ha dedicado a dicha tarea.
*/
public class Task extends Activity {

  private List<Interval> intervals;
  private boolean status;
  private Logger logger = LoggerFactory.getLogger("core.Task");
  private int id;

  public Task(String name) {
    super(name);
    this.id = IdConstructor.getNextId();
    logger.debug("Task Constructor | Name: " + name);
    intervals = new ArrayList<>();
    assert (invariant()) : "Invariant violated";
  }

  public Task(String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    super(name, initialDate, finalDate, duration);
    this.id = IdConstructor.getNextId();
    logger.debug("Task Constructor | Name: " + name);
    logger.debug("Task Constructor | Initial Date: " + initialDate);
    logger.debug("Task Constructor | Final Date: " + finalDate);
    logger.debug("Task Constructor | Duration: " + duration);
    intervals = new ArrayList<>();
    assert (invariant()) : "Invariant violated";
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
    if (!status) {
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

  @Override
  public Activity findActivityById(int n) {
    if (n == id) {
      return this;
    } else {
      return null;
    }
  }

  @Override
  public JSONObject toJson(int it) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject jsonTask = new JSONObject();
    jsonTask.put("name", this.getName());
    jsonTask.put("class", "Task");
    jsonTask.put("id", this.id);
    // Necesario comprobar que la fecha sea null, porque sino al formatear la fecha nos daria
    // una excepcion
    jsonTask.put(
        "initialDate",
        this.getInitialDate() == null ? "null" : this.getInitialDate().format(formatter));
    jsonTask.put(
        "finalDate", this.getFinalDate() == null ? "null" : this.getFinalDate().format(formatter));
    jsonTask.put("duration", this.getDuration());
    jsonTask.put("active", this.status);
    JSONArray jsonIntervals = new JSONArray();
    if(it > 0) {
      for (Interval intervalChild : this.getIntervals()) {
        JSONObject jsonInterval = (JSONObject) intervalChild.acceptVisitor(new JsonVisitor());
        jsonIntervals.put(jsonInterval);
      }
      jsonTask.put("activities", jsonIntervals);
    }
    return jsonTask;
  }

  protected boolean invariant() {
    if (getDuration() != 0) {
      if (intervals == null) {
        return false;
      }
    }
    return super.invariant();
  }
}
