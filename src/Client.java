import core.*;
import secondmilestone.TimeVisitor;
import secondmilestone.VisitorTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

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
    for (Task a : active) {
      if (a.getStatus()) {
        a.acceptVisitor(new PrintVisitor());
        // System.out.println(a);
      }
    }
  }


  private void wait(int seconds) {
    try {
      Thread.sleep(1001 * seconds);
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

  public void tercertTest() {
    Project root = new Project("root");
    Project sd = new Project("software design");
    Project st = new Project("software testing");
    Project db = new Project("databases");
    Task transport = new Task("transportation");
    sd.addTag("java");
    sd.addTag("flutter");
    st.addTag("c++");
    st.addTag("Java");
    st.addTag("python");
    db.addTag("SQL");
    db.addTag("python");
    db.addTag("C++");
    root.addActivity(transport);
    root.addActivity(sd);
    root.addActivity(st);
    root.addActivity(db);
    Project ptt = new Project("project time tracker");
    Project prob = new Project("problems");
    sd.addActivity(prob);
    sd.addActivity(ptt);
    Task firstList = new Task("first list");
    firstList.addTag("java");
    Task secondList = new Task("second list");
    secondList.addTag("Dart");
    prob.addActivity(firstList);
    prob.addActivity(secondList);
    Task readHandout = new Task("read handout");
    Task firstMilestone = new Task("first milestone");
    firstMilestone.addTag("Java");
    firstMilestone.addTag("IntelliJ");
    ptt.addActivity(readHandout);
    ptt.addActivity(firstMilestone);
    root.acceptVisitor(new VisitorTags("c++"));
    root.acceptVisitor(new VisitorTags("Java"));
    root.acceptVisitor(new VisitorTags("C++"));
    root.acceptVisitor(new VisitorTags("no"));
  }

  public void cuartoTest() {

    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();


    Project root = new Project("root");
    Project P0 = new Project("P0");
    Project P1 = new Project("P1");
    Project P3 = new Project("P3");
    Task T0 = new Task("T0");
    Task T1 = new Task("T1");
    Task T2 = new Task("T2");
    Task T3 = new Task("T3");
    Task T4 = new Task("T4");
    Task T5 = new Task("T5");

    root.addActivity(P0);
    root.addActivity(P1);
    root.addActivity(P3);
    root.addActivity(T4);
    root.addActivity(T5);
    P0.addActivity(T0);
    P0.addActivity(T1);
    P0.addActivity(T2);
    P1.addActivity(T3);

    wait(10); //10s
    T0.startTask();
    wait(10); //20s
    T4.startTask();
    wait(10); //30s
    T4.stopTask();
    T0.stopTask();
    T1.startTask();
    T2.startTask();
    wait(10); //40s
    T0.startTask();
    T5.startTask();
    wait(10); //50s
    T0.stopTask();
    T1.stopTask();
    T4.startTask();
    wait(20); //70s
    T1.startTask();
    T5.stopTask();
    wait(10); //80s
    T5.startTask();
    wait(10); //90s
    T2.stopTask();
    T5.stopTask();
    wait(10); //100s
    T5.startTask();
    wait(10); //110s
    T5.stopTask();
    T2.startTask();
    wait(20); //130
    T1.stopTask();
    T2.stopTask();
    T4.stopTask();
    T3.startTask();
    wait(10); //140s
    T0.startTask();
    T3.stopTask();
    T4.startTask();
    wait(10); //150s
    T0.stopTask();
    wait(10); //160s
    T4.stopTask();

    parser.saveFile(root, "cuartoTest.json");
    c.cancel();

  }

  public void cuartoTest2ElectricBogaloo() {
      Project root = parser.loadFile("cuartoTest.json");
      long duration = (int) root.acceptVisitor(new TimeVisitor(LocalDateTime.of(2020, 11, 17, 20, 7, 50),
              LocalDateTime.of(2020, 11, 17, 20, 8, 50)));
      System.out.println(duration);

  }


  public static void main(String[] args) {
    Client client = new Client();
    if (args.length == 0) {
      client.primerTest();
    } else if(args.length == 1){
      client.segundoTest();
    } else if(args.length == 2){
      client.tercertTest();
    } else if(args.length == 3){
      client.cuartoTest();
    } else {
      client.cuartoTest2ElectricBogaloo();
    }
  }
}
