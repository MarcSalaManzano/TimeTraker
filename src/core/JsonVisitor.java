package core;

import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visitor.Visitor;


/*Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para crear los JSONObject, de esta forma los Element no tienen que conocer la creación de los
JSONObject aprovechando el polimorfismo sin necesidad de identificar la clase de los hijos.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements.
Este Visitor.Visitor se encarga de generar el JSONObject que se guardara en el fichero. Para eso,
primero se recoge la información de las instancias y después, en el caso el caso que esta tenga
hijos, se visita a estos con un nuevo Visitor.Visitor (Metodos recursivos).
*/
public class JsonVisitor implements Visitor {

  private Logger logger = LoggerFactory.getLogger("core.JSONVisitor");

  public JSONObject visitTask(Task task) {
    logger.trace("Task visited " + task.getName() + " with visitor JSONVisitor");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject jsonTask = new JSONObject();
    jsonTask.put("name", task.getName());
    jsonTask.put("class", "Task");
    // Necesario comprobar que la fecha sea null, porque sino al formatear la fecha nos daria
    // una excepcion
    jsonTask.put(
        "initialDate",
        task.getInitialDate() == null ? "null" : task.getInitialDate().format(formatter));
    jsonTask.put(
        "finalDate", task.getFinalDate() == null ? "null" : task.getFinalDate().format(formatter));
    jsonTask.put("duration", task.getDuration());
    JSONArray jsonIntervals = new JSONArray();
    for (Interval intervalChild : task.getIntervals()) {
      JSONObject jsonInterval = (JSONObject) intervalChild.acceptVisitor(new JsonVisitor());
      jsonIntervals.put(jsonInterval);
    }
    jsonTask.put("activities", jsonIntervals);
    return jsonTask;
  }

  public JSONObject visitProject(Project project) {
    logger.trace("Project visited " + project.getName() + " with visitor JSONVisitor");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject jsonProject = new JSONObject();
    jsonProject.put("name", project.getName());
    jsonProject.put("class", "Project");
    // Necesario comprobar que la fecha sea null, porque sino al formatear la fecha nos daria
    // una excepcion
    jsonProject.put(
        "initialDate",
        project.getInitialDate() == null ? "null" : project.getInitialDate().format(formatter));
    jsonProject.put(
        "finalDate",
        project.getFinalDate() == null ? "null" : project.getFinalDate().format(formatter));
    jsonProject.put("duration", project.getDuration());
    // Guardamos los hijos en una array JSON
    JSONArray jsonActivities = new JSONArray();
    for (Activity activityChild : project.getChilds()) {
      JSONObject jsonActivity = (JSONObject) activityChild.acceptVisitor(new JsonVisitor());
      jsonActivities.put(jsonActivity);
    }
    jsonProject.put("activities", jsonActivities);
    return jsonProject;
  }

  public JSONObject visitInterval(Interval interval) {
    logger.trace("Project visited with visitor JSONVisitor");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject jsonInterval = new JSONObject();
    jsonInterval.put("class", "Interval");
    // Necesario comprobar que la fecha sea null, porque sino al formatear la fecha nos daria
    // una excepcion
    jsonInterval.put(
        "initialDate",
        interval.getInitialDate() == null ? "null" : interval.getInitialDate().format(formatter));
    jsonInterval.put(
        "finalDate",
        interval.getFinalDate() == null ? "null" : interval.getFinalDate().format(formatter));
    jsonInterval.put("duration", interval.getDuration().toSeconds());
    return jsonInterval;
  }
}
