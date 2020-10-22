package core;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
Las clase Task, actua como elemento "Leaf", en el patrón de diseño COMPOSITE. Esta clase por su gerarquía y
programación no puede contener hijos, como la clase "Composite". Por ello solo implementa los métodos de
"Component", en nuestro caso de Activity.
También aplica el patrón VISITOR, como elemento "ConcretElement".
Además como clase, una tarea es un tipo de actividad que solo puede contener intervalos de tiempo.
Esto se debe a que el trabajo de un usuario se divide en tareas y estas tareas son contavilizadas en dichos
intervalos. El usuario tiene la capadidad de decidir cuando empezar a trabajar en una tarea, cancelarla,
pararla para luego retomarla, etc. Por ello dentro de cada tarea se guarda el intervalo de tiempo que se ha
dedicado a dicha tarea.
*/
public class Task extends Activity{

  private List<Interval> intervals;
  private boolean status;

  public Task(String name) {
    super(name);
    intervals = new ArrayList<>();
  }

  public Task(String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    super(name, initialDate, finalDate, duration);
    intervals = new ArrayList<>();
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void startTask() {
    /*
    Al activar la Task, se marca como activa y se crea un nuevo Interval que se añade a su lista de intervalos, i este nuevo Interval
    se añade como Observer al clock. Si es la primera vez que se activa, se le asigna la hora actual como hora inicial.
     */
    LocalDateTime newInitialDate = Clock.getInstance().getDate();
    Interval newInterval = new Interval(this);
    intervals.add(newInterval);
    Clock.getInstance().addObserver(newInterval);
    if(getInitialDate() == null) {
      this.setInitialDate(newInitialDate);
    }
    this.status = true;
  }

  public void stopTask() {
    /*
    Al parar la Task borramos el intervalo de la lista de observers y cambiamos el estado a inactivo
    para que no se muestre por pantalla.
     */
    Clock.getInstance().deleteObserver(intervals.get(intervals.size()-1));
    this.status = false;
  }

  public List<Interval> getIntervals() { return this.intervals; }

  @Override
  public String toString() {
    String description = intervals.get(intervals.size()-1).toString();
    return description+super.toString();
  }

  @Override
  public JSONObject acceptVisitor(Visitor v) { return v.visitTask(this); }

  public void addInterval(Interval interval) {
    intervals.add(interval);
    interval.setFather(this);
  }

  public Activity find(String name) {
    /*
    Función que sirve para devolver una Actividad con el nombre pasado por parametro.
    En el caso de Task, si el nombre coincide con el suyo se devuelve a si mismo, en caso contrario devuelve un null.
     */
    if(name.equals(this.getName())) {
      return this;
    } else {
      return null;
    }
  }
}
