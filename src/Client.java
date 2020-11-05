import core.*;

import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Esta clase es la encargada de simular las acciones del usuario para la primera entrega.
Forma parte del patrón Observer, siendo un Observer de la clase Clock, mostrando por cada tick del reloj la
información de las tareas activas junto a sus padres.
 */
public class Client implements Observer {
  List<Task> active = new ArrayList<>();
  JSONParse parser = new JSONParse();
  Logger logger = LoggerFactory.getLogger("Client");

  @Override
  public void update(Observable o, Object arg) {
    for(Iterator<Task> itr = active.iterator(); itr.hasNext();){
      Task a = itr.next();
      if(a.getStatus()) {
        System.out.println(a);
      }
    }
  }

  private void wait(int seconds) {
    try {
      Thread.sleep(1000*seconds+1);
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
    active.add(transport);
    active.add(firstList);
    active.add(secondList);
    active.add(readHandout);
    active.add(firstMilestone);
    c.addObserver(this);
    c.start();
    logger.trace("Start test");
    logger.trace("Transportation start");
    transport.startTask();
    wait(4);
    logger.trace("Transportation stop");
    transport.stopTask();
    wait(2);
    logger.trace("First list start");
    firstList.startTask();
    wait(6);
    logger.trace("Second list start");
    secondList.startTask();
    wait(4);
    logger.trace("First list stop");
    firstList.stopTask();
    wait(2);
    logger.trace("Second list stop");
    secondList.stopTask();
    wait(2);
    logger.trace("Transportation start");
    transport.startTask();
    wait(4);
    logger.trace("Transportation stop");
    transport.stopTask();
    parser.saveFile(root, "test.json");
    c.cancel();
  }

  public void segundoTest() {
    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();
    Project root = parser.loadFile("test.json");
    Task transport = (Task) root.find("transportation");
    Task firstList = (Task) root.find("first list");
    active.add(transport);
    active.add(firstList);
    transport.startTask();
    wait(4);
    transport.stopTask();
    wait(2);
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