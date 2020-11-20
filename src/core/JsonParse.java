package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Hace de Cliente del patrón visitor.
Esta clase recorre y almacena el árbol de actividades en un fichero en formato Json, también lee el
fichero y reconstruye el arbol a partir de los datos leídos, creando las instancias de cada clase.
*/

public class JsonParse {
  private Logger logger = LoggerFactory.getLogger("core.JSONParse");

  public void saveFile(Activity activity, String fileName) {
    JSONObject object = (JSONObject) activity.acceptVisitor(new JsonVisitor());
    try (FileWriter file = new FileWriter(fileName)) {
      logger.info("Writing " + activity.getName() + " in file: " + fileName);
      file.write(object.toString());
    } catch (IOException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
  }

  public Project loadFile(String fileName) {
    InputStream is = null;
    try {
      is = new FileInputStream(fileName);
    } catch (FileNotFoundException e) {
      logger.warn(e.getMessage());
      e.printStackTrace();
    }
    if (is == null) {
      logger.warn("Cannot find resource file " + fileName);
      throw new NullPointerException("Cannot find resource file " + fileName);
    }
    JSONTokener tokener = new JSONTokener(is);
    JSONObject jsonObject = new JSONObject(tokener);
    return createProjectFromJson(jsonObject);
  }

  private Task createTaskFromJson(JSONObject jsonTask) {
    logger.trace("Entered in createTaskFromJson");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    String name = jsonTask.getString("name");
    String initialDateString = jsonTask.getString("initialDate");
    LocalDateTime initialDate = initialDateString.equals("null") ? null
        : LocalDateTime.parse(initialDateString, formatter);
    String finalDateString = jsonTask.getString("finalDate");
    LocalDateTime finalDate =
        finalDateString.equals("null") ? null : LocalDateTime.parse(finalDateString, formatter);
    Duration duration = Duration.ofSeconds(jsonTask.getInt("duration"));
    Task newTask = new Task(name, initialDate, finalDate, duration);
    JSONArray jsonIntervals = jsonTask.getJSONArray("activities");
    for (int i = 0; i < jsonIntervals.length(); i++) {
      JSONObject child = jsonIntervals.getJSONObject(i);
      newTask.addInterval(createIntervalFromJson(child));
    }
    return newTask;
  }

  private Project createProjectFromJson(JSONObject jsonProject) {
    logger.trace("Entered in createProjectFromJson");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    String name = jsonProject.getString("name");
    String initialDateString = jsonProject.getString("initialDate");
    LocalDateTime initialDate = initialDateString.equals("null") ? null
        : LocalDateTime.parse(initialDateString, formatter);
    String finalDateString = jsonProject.getString("finalDate");
    LocalDateTime finalDate =
        finalDateString.equals("null") ? null : LocalDateTime.parse(finalDateString, formatter);
    Duration duration = Duration.ofSeconds(jsonProject.getInt("duration"));
    Project newProject = new Project(name, initialDate, finalDate, duration);
    JSONArray jsonActivities = jsonProject.getJSONArray("activities");
    for (int i = 0; i < jsonActivities.length(); i++) {
      JSONObject child = jsonActivities.getJSONObject(i);
      switch (child.getString("class")) {
        case "Project" -> newProject.addActivity(createProjectFromJson(child));
        case "Task" -> newProject.addActivity(createTaskFromJson(child));
        default -> logger.warn("Child of Project is not a Project or a Task");
      }
    }
    return newProject;
  }

  private Interval createIntervalFromJson(JSONObject jsonInterval) {
    logger.trace("Entered in createIntervalFromJson");
    // Formato de fecha a guardar: año-mes-dia hora:minuto:segundo
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
    String initialDateString = jsonInterval.getString("initialDate");
    LocalDateTime initialDate = initialDateString.equals("null") ? null
        : LocalDateTime.parse(initialDateString, formatter); //YY:MM:DD HH:mm:ss
    String finalDateString = jsonInterval.getString("finalDate");
    LocalDateTime finalDate =
        finalDateString.equals("null") ? null : LocalDateTime.parse(finalDateString, formatter);
    Duration duration = Duration.ofSeconds(jsonInterval.getInt("duration"));
    return new Interval(initialDate, finalDate, duration);
  }
}
