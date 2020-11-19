package core;

import Visitor.Visitor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private Logger logger = LoggerFactory.getLogger("core.Project");

  public Project(String name) {
    super(name);
    logger.debug("Project Constructor | Name: " + this.getName());
    childs = new ArrayList<>();
    assert (invariant()):"Invariant violated";
  }

  public Project(
      String name, LocalDateTime initialDate, LocalDateTime finalDate, Duration duration) {
    super(name, initialDate, finalDate, duration);
    logger.debug("Project Constructor | Name: " + name);
    logger.debug("Project Constructor | Initial Date: " + initialDate);
    logger.debug("Project Constructor | Final Date: " + finalDate);
    logger.debug("Project Constructor | Duration: " + duration);
    childs = new ArrayList<>();
    assert (invariant()):"Invariant violated";
  }

  @Override
  public Object acceptVisitor(Visitor v) {
    visitorCheck(v);
    return v.visitProject(this);
  }

  public void addActivity(Activity a) throws IllegalArgumentException {
    assert (invariant()):"Invariant violated";
    if (a == null) {
      throw new IllegalArgumentException("Null activity passed to addActivity function in project");
    }
    childs.add(a);
    a.addFather(this);
    logger.debug("Father " + a.getName() + " added to " + this.getName() + " project");
    if (!this.getName().equals("root")) {
      assert (a.getFather() == null) : "Father different from root is null";
    }
    assert (invariant()):"Invariant violated";
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
    assert (invariant()):"Invariant violated";
    invalidArguments(name);
    if (name.equals(this.getName())) {
      return this;
    } else {
      for (Activity act : childs) {
        Activity activity = act.find(name);
        if (activity != null) {
          return activity;

        }
      }
      assert (invariant()):"Invariant violated";
      return null;
    }
  }

  protected boolean invariant() {
    if (getInitialDate() != null && childs == null) {return false;}
    return true;
  }
}
