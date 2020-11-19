package core;

import Visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para printar las tareas activas, de esta forma las Activity no tienen que implementar
nueva funcionalidad.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements
 */
public class PrintVisitor implements Visitor {

  private Logger logger = LoggerFactory.getLogger("core.PrintVisitor");

  @Override
  public Object visitTask(Task t) {
    logger.trace("Task visited " + t.getName() + " with visitor PrintVisitor");
    t.getLastInterval().acceptVisitor(new PrintVisitor());
    logger.info(t.toString());
    t.getFather().acceptVisitor(new PrintVisitor());
    return null;
  }

  @Override
  public String visitProject(Project p) {
    logger.trace("Project visited " + p.getName() + " with visitor PrintVisitor");
    logger.info(p.toString());
    return null;
  }

  @Override
  public Object visitInterval(Interval i) {
    logger.trace("Inerval visited with visitor PrintVisitor");
    logger.info(i.toString());
    return null;
  }
}
