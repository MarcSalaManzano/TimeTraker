import core.Clock;
import core.JsonParse;
import core.PrintVisitor;
import core.Project;
import core.Task;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import secondmilestone.TimeVisitor;
import secondmilestone.VisitorTags;

/*
Esta clase es la encargada de simular las acciones del usuario para la primera entrega.
Forma parte del patrón Observer, siendo un Observer de la clase Clock, mostrando por cada tick del
 reloj la información de las tareas activas junto a sus padres.
 */
public class Client implements Observer {

  List<Task> active = new ArrayList<>();
  JsonParse parser = new JsonParse();
  Logger logger = LoggerFactory.getLogger("Client");

  @Override
  public void update(Observable o, Object arg) {
    for (Task a : active) {
      if (a.getStatus()) {
        a.acceptVisitor(new PrintVisitor());
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
    active.add(transport);
    active.add(firstList);
    active.add(secondList);
    active.add(readHandout);
    active.add(firstMilestone);
    Clock c = Clock.getInstance();
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

    Project sd = new Project("software design");
    Project st = new Project("software testing");


    sd.addTag("java");
    sd.addTag("flutter");
    st.addTag("c++");
    st.addTag("Java");
    st.addTag("python");
    Project db = new Project("databases");
    db.addTag("SQL");
    db.addTag("python");
    db.addTag("C++");
    Task transport = new Task("transportation");
    Project root = new Project("root");
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
    root.acceptVisitor(new VisitorTags("SQL"));
    root.acceptVisitor(new VisitorTags("no"));
  }

  public void cuartoTest() {
    Clock c = Clock.getInstance();
    c.addObserver(this);
    c.start();
    Project root = new Project("root");
    Project p0 = new Project("P0");
    Project p1 = new Project("P1");
    Project p3 = new Project("P3");
    Task t4 = new Task("T4");
    Task t5 = new Task("T5");
    root.addActivity(p0);
    root.addActivity(p1);
    root.addActivity(p3);
    root.addActivity(t4);
    root.addActivity(t5);
    Task t0 = new Task("T0");
    p0.addActivity(t0);
    Task t1 = new Task("T1");
    p0.addActivity(t1);
    Task t2 = new Task("T2");
    p0.addActivity(t2);
    Task t3 = new Task("T3");
    p1.addActivity(t3);

    wait(10); // 10s
    t0.startTask();
    wait(10); // 20s
    t4.startTask();
    wait(10); // 30s
    t4.stopTask();
    t0.stopTask();
    t1.startTask();
    t2.startTask();
    wait(10); // 40s
    t0.startTask();
    t5.startTask();
    wait(10); // 50s
    t0.stopTask();
    t1.stopTask();
    t4.startTask();
    wait(20); // 70s
    t1.startTask();
    t5.stopTask();
    wait(10); // 80s
    t5.startTask();
    wait(10); // 90s
    t2.stopTask();
    t5.stopTask();
    wait(10); // 100s
    t5.startTask();
    wait(10); // 110s
    t5.stopTask();
    t2.startTask();
    wait(20); // 130
    t1.stopTask();
    t2.stopTask();
    t4.stopTask();
    t3.startTask();
    wait(10); // 140s
    t0.startTask();
    t3.stopTask();
    t4.startTask();
    wait(10); // 150s
    t0.stopTask();
    wait(10); // 160s
    t4.stopTask();

    parser.saveFile(root, "cuartoTest.json");
    c.cancel();
  }

  public void quintoTest() {
    Project root = parser.loadFile("cuartoTest.json");
    long duration =
        (int)
            root.acceptVisitor(
                new TimeVisitor(
                    LocalDateTime.of(2020, 11, 20, 00, 21, 57),
                    LocalDateTime.of(2020, 11, 20, 00, 22, 57)));
    System.out.println(duration);
  }

  public static void main(String[] args) throws IllegalArgumentException {
    Client client = new Client();
    int opt = Integer.parseInt(args[0]);
    switch (opt) {
      case 0:
        client.primerTest();
        break;
      case 1:
        client.segundoTest();
        break;
      case 2:
        client.tercertTest();
        break;
      case 3:
        client.cuartoTest();
        break;
      case 4:
        client.quintoTest();
        break;
      default:
        throw new IllegalArgumentException(
            "Test to execute must be specified at program arguments");
    }
  }
}
