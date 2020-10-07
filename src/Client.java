import core.Activity;
import core.Clock;
import core.Project;
import core.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Client implements Observer {
  List<Activity> active = new ArrayList<>();
  @Override
  public void update(Observable o, Object arg) {
    for(Activity a : active) {
      System.out.println(a);
    }
  }

  private void wait(int seconds) {
    try {
      Thread.sleep(1000*seconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void start() {

    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();
    Project root = new Project("root");
    Task transport = new Task("transportation");
    root.addActivity(transport);
    Project sd = new Project("software design");
    root.addActivity(sd);
    Project prob = new Project("problems");
    sd.addActivity(prob);
    Task firstList = new Task("first list");
    Task secondList = new Task("second list");
    prob.addActivity(firstList);
    prob.addActivity(secondList);
    //active.add(transport);
    //transport.startTask();
    active.add(firstList);
    firstList.startTask();
    wait(4);
    //transport.stopTask();
    wait(2);
    active.add(firstList);

    //wait(6);
    c.cancel();
  }

  public static void main(String[] args) {
    Client client = new Client();
    client.start();

  }
}