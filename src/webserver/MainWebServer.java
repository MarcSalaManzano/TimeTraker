package webserver;

import core.Activity;
import core.Clock;
import core.Project;
import core.Task;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Activity root = makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock
    Clock.getInstance().start();
    new WebServer(root);
  }

  private static Activity makeTreeCourses(){
    Project root = new Project("root");
    Project sd = new Project("software design");
    Project st = new Project("software testing");
    Project db = new Project("databases");
    Task transport = new Task("transportation");
    root.addActivity(transport);
    root.addActivity(sd);
    root.addActivity(st);
    root.addActivity(db);
    Project ptt = new Project("project time tracker");
    Project prob = new Project("problems");
    sd.addActivity(prob);
    sd.addActivity(ptt);
    Task firstList = new Task("first list");
    Task secondList = new Task("second list");
    prob.addActivity(firstList);
    prob.addActivity(secondList);
    Task readHandout = new Task("read handout");
    Task firstMilestone = new Task("first milestone");
    ptt.addActivity(readHandout);
    ptt.addActivity(firstMilestone);
    return root;
  }
}