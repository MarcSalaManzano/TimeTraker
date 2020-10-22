package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;

/*Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades para crear los JSONObject,
de esta forma los Element no tienen que conocer la creación de los JSONObject aprovechando el polimorfismo sin necesidad
de identificar la clase de los hijos.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían ConcreteElements
Este Visitor se encarga de generar el JSONObject que se guardara en el fichero. Para eso, primero se recoge la información
de las instancias y después, en el caso el caso que esta tenga hijos, se visita a estos con un nuevo Visitor (Metodos recursivos).
*/

public class Visitor {
  public JSONObject visitTask(Task t) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject a = new JSONObject();
    a.put("name", t.getName());
    a.put("class", "Task");
    a.put("initialDate", t.getInitialDate() == null ? "null" : t.getInitialDate().format(formatter));
    a.put("finalDate", t.getFinalDate() == null ? "null" : t.getFinalDate().format(formatter));
    a.put("duration", t.getDuration());
    JSONArray b = new JSONArray();
    for(Interval inter : t.getIntervals()) {
      JSONObject ob = inter.acceptVisitor(new Visitor());
      b.put(ob);
    }
    a.put("activities", b);
    return a;
  }

  public JSONObject visitProject(Project p) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject a = new JSONObject();
    a.put("name", p.getName());
    a.put("class", "Project");
    a.put("initialDate", p.getInitialDate() == null ? "null" : p.getInitialDate().format(formatter));
    a.put("finalDate", p.getFinalDate() == null ? "null" : p.getFinalDate().format(formatter));
    a.put("duration", p.getDuration());
    JSONArray b = new JSONArray();
    for(Activity act : p.getChilds()) {
      JSONObject ob = act.acceptVisitor(new Visitor());
      b.put(ob);
    }
    a.put("activities", b);
    return a;
  }

  public JSONObject visitInterval(Interval i) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    JSONObject a = new JSONObject();
    a.put("class", "Interval");
    a.put("initialDate", i.getInitialDate() == null ? "null" : i.getInitialDate().format(formatter));
    a.put("finalDate", i.getFinalDate() == null ? "null" : i.getFinalDate().format(formatter));
    a.put("duration", i.getDuration().toSeconds());
    return a;
  }
}
