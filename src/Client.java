import core.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Client implements Observer {
  List<Activity> active = new ArrayList<>();
  JSONParse parser = new JSONParse();
  @Override
  public void update(Observable o, Object arg) {
    for(Activity a : active) {
      System.out.println(a);
    }
  }

  private void wait(int seconds) {
    try {
      Thread.sleep(1100*seconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void primerTest() {
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
    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();
    System.out.println("Start test");
    System.out.println("Transportation start");
    transport.startTask();
    active.add(transport);
    wait(4);
    System.out.println("Transportation stop");
    transport.stopTask();
    active.remove(transport);
    wait(2);
    System.out.println("First list start");
    firstList.startTask();
    active.add(firstList);
    wait(6);
    System.out.println("Second list start");
    secondList.startTask();
    active.add(secondList);
    wait(4);
    System.out.println("First list stop");
    firstList.stopTask();
    active.remove(firstList);
    wait(2);
    System.out.println("Second list stop");
    secondList.stopTask();
    active.remove(secondList);
    wait(2);
    System.out.println("Transportation start");
    transport.startTask();
    active.add(transport);
    wait(4);
    System.out.println("Transportation stop");
    transport.stopTask();
    active.remove(transport);
    parser.saveFile(root, "test.json");
    c.cancel();
  }

  public void segundoTest() {
    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();
    Project root = parser.loadFile("test.json");
    Task transport = (Task) root.find("transportation");
    transport.startTask();
    active.add(transport);
    wait(4);
    transport.stopTask();
    active.remove(transport);
    wait(2);
    Task firstList = (Task) root.find("first list");
    active.add(firstList);
    firstList.startTask();
    wait(6);
    c.cancel();
  }

  public static void main(String[] args) {
    Client client = new Client();
    if(args.length == 0) {
      client.primerTest();
    } else {
      client.segundoTest();
    }

  }
}