package core;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.*;

/*
Patrón Visitor.
Esta clase recorre y almacena el árbol de actividades en un fichero en formato Json, también lee el fichero y
reconstruye el arbol a partir de los datos leídos.
*/

public class JSONParse {
  public void saveFile(Activity activity, String fileName) {
    JSONObject object = activity.acceptVisitor(new Visitor());
    try (FileWriter file = new FileWriter(fileName)) {
      file.write(object.toString());
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public Project loadFile(String fileName) {
    InputStream is = null;
    try {
      is = new FileInputStream(fileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (is == null) {
      throw new NullPointerException("Cannot find resource file " + fileName);
    }
    JSONTokener tokener = new JSONTokener(is);
    JSONObject object = new JSONObject(tokener);
    return createProject(object);
  }

  private Task createTask(JSONObject ob) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String name = ob.getString("name");
    String sInitialDate = ob.getString("initialDate");
    LocalDateTime initialDate = sInitialDate.equals("null") ? null : LocalDateTime.parse(sInitialDate, formatter); //YY:MM:DD HH:mm:ss
    String sFinalDate = ob.getString("finalDate");
    LocalDateTime finalDate = sFinalDate.equals("null") ? null : LocalDateTime.parse(sFinalDate, formatter);
    Duration duration = Duration.ofSeconds(ob.getInt("duration"));
    Task newTask = new Task(name, initialDate, finalDate, duration);
    JSONArray activities = ob.getJSONArray("activities");
    for(int i = 0; i<activities.length(); i++) {
      JSONObject child = activities.getJSONObject(i);
      newTask.addInterval(createInterval(child));
    }
    return newTask;
  }

  private Project createProject(JSONObject ob) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String name = ob.getString("name");
    String sInitialDate = ob.getString("initialDate");
    LocalDateTime initialDate = sInitialDate.equals("null") ? null : LocalDateTime.parse(sInitialDate, formatter); //YY:MM:DD HH:mm:ss
    String sFinalDate = ob.getString("finalDate");
    LocalDateTime finalDate = sFinalDate.equals("null") ? null : LocalDateTime.parse(sFinalDate, formatter);
    Duration duration = Duration.ofSeconds(ob.getInt("duration"));
    Project newProject = new Project(name, initialDate, finalDate, duration);
    JSONArray activities = ob.getJSONArray("activities");
    for(int i = 0; i<activities.length(); i++) {
      JSONObject child = activities.getJSONObject(i);
      switch (child.getString("class")){
        case "Project":
          newProject.addActivity(createProject(child));
          break;
        case "Task":
          newProject.addActivity(createTask(child));
      }
    }
    return newProject;
  }

  private Interval createInterval(JSONObject ob) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String sInitialDate = ob.getString("initialDate");
    LocalDateTime initialDate = sInitialDate.equals("null") ? null : LocalDateTime.parse(sInitialDate, formatter); //YY:MM:DD HH:mm:ss
    String sFinalDate = ob.getString("finalDate");
    LocalDateTime finalDate = sFinalDate.equals("null") ? null : LocalDateTime.parse(sFinalDate, formatter);
    Duration duration = Duration.ofSeconds(ob.getInt("duration"));
    Interval newInterval = new Interval(initialDate, finalDate, duration);
    return newInterval;
  }
}
