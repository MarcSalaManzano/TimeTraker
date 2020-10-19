package core;

import java.util.List;

/*La clase Controller, como su nombre indica, controla el inicio y finalización de todos los procesos.
Estos se van añadiendo a la lista de actividadaes, que formaran el árbol que consituye el proyecto.
Además nos permite la búsqueda de actividades para el JSON
*/

public class Controller {
  private Activity root;
  private List<Activity> activeTasks;

  public void addTask(String name, String projectName) {}

  public void addProject(String name, String projectName) {}

  public void stopTask(String name) {}

  public void startTask(String name) {}

  private Activity findActivityByName(String Name) { return null;}

  public List<Activity> getActiveTask() {return null;}
}
