package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;

//implementación del patrón de diseño visitor, que utilizamos para recorrer el arbol de actividades y
//convertirlo en formato Json para poder almacenarlo en un fichero.

public class Visitor {
  public JSONObject visitTask(Task t) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    JSONObject a = new JSONObject();
    a.put("class", "Interval");
    a.put("initialDate", i.getInitialDate() == null ? "null" : i.getInitialDate().format(formatter));
    a.put("finalDate", i.getFinalDate() == null ? "null" : i.getFinalDate().format(formatter));
    a.put("duration", i.getDuration().toSeconds());
    return a;
  }
}
