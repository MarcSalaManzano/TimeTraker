package secondmilestone;

import Visitor.Visitor;
import core.Activity;
import core.Interval;
import core.Project;
import core.Task;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para encontrar las actividades con cierto tag, de esta forma las Activity no tienen que implementar
nueva funcionalidad.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements
 */
public class VisitorTags implements Visitor {
  private String tag;
  private List<Activity> tags = new ArrayList();
  private Logger logger = LoggerFactory.getLogger("milestone2.VisitorTags");

  public VisitorTags(String tag) {
    this.tag = tag;
  }

  @Override
  public Object visitTask(Task t) {
    logger.trace("Task visited " + t.getName() + " with visitor VisitorTags");
    List<String> taskTags = t.getTags();
    boolean hasTag = false;
    for (String tag : taskTags) {
      if (tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if (hasTag) {
      logger.info("Task " + t.getName() + " has tag " + this.tag);
      tags.add(t);
    }
    return this.tags;
  }

  @Override
  public Object visitProject(Project p) {
    logger.trace("Project visited " + p.getName() + " with visitor VisitorTags");
    List<String> projectTags = p.getTags();
    boolean hasTag = false;
    for (String tag : projectTags) {
      if (tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if (hasTag) {
      logger.info("Project " + p.getName() + " has tag " + this.tag);
      tags.add(p);
    }
    for (Activity child : p.getChilds()) {
      child.acceptVisitor(this);
    }
    return this.tags;
  }

  @Override
  public Object visitInterval(Interval i) {
    logger.warn("Interval visited with visitor VisitorTags");
    return null;
  }
}