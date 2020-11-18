package core;

import Visitor.Visitor;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
La clase Project actua como elemento "Composite", en el patrón de diseño COMPOSITE, puesto que es la clase
que contiene una lista de "Activities", Actividades y hereda los métodos necesarios.
Además, forma parte del patrón de diseño VISITOR, como elemento "ConcretElement".
Por otro lado, un Proyecto es un tipo de Actividad (por ello la aplicación de los patrones de diseño).
Los proyectos pueden englobar otros Proyectos y Tareas, por lo que debemos de ser capazes de almacenar
tanto Proyectos como Tareas de forma anidada para que el cliente pueda tener una organización óptima
de sus Tareas, Pryectos, etc.
*/

public class Project extends Activity {

  private List<Activity> childs;

  public Project(String name) throws IllegalArgumentException{
    super(name);
    childs = new ArrayList<>();
  }

  public Project(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    super(name, initialDate, finalDate, duration);
    childs = new ArrayList<>();
  }

  @Override
  public Object acceptVisitor(Visitor v) {
    return v.visitProject(this);
  }

  public void addActivity(Activity a) {
    childs.add(a);
    a.addFather(this);
  }

  public void removeActivity(Activity a) {
    childs.remove(a);
  }

  public List<Activity> getChilds() {
    return childs;
  }

  public Activity find(String name) {
    /*
    Función que sirve para devolver una Actividad con el nombre pasado por parametro.
    En el caso de Project, primero mira si el nombre coincide con el suyo, en el caso que así sea se devuelve a si mismo.
    En el caso contrario llama a la función find de sus hijos y si uno de estos devuelve algo diferente a null lo devuelve.
     */
    if (name.equals(this.getName())) {
      return this;
    } else {
      for (Activity act : childs) {
        Activity activity = act.find(name);
        if (activity != null) {
          return activity;
        }
      }
    }
    return null;
  }
}
