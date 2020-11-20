package secondmilestone;


import core.Activity;
import core.Interval;
import core.Project;
import core.Task;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visitor.Visitor;

/*
Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para encontrar las actividades con cierto tag, de esta forma las Activity no tienen que implementar
nueva funcionalidad.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements
 */
public class VisitorTags implements Visitor {
  private String tag;
  private List<Activity> activitiesWithTag = new ArrayList();
  private Logger logger = LoggerFactory.getLogger("secondmilestone.VisitorTags");

  public VisitorTags(String tag) {
    this.tag = tag;
  }

  @Override
  public Object visitTask(Task task) {
    logger.trace("Task visited " + task.getName() + " with visitor VisitorTags");
    List<String> taskTags = task.getTags();
    boolean hasTag = false;
    for (String tag : taskTags) {
      if (tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if (hasTag) {
      logger.info("Task " + task.getName() + " has tag " + this.tag);
      activitiesWithTag.add(task);
    }
    return this.activitiesWithTag;
  }

  @Override
  public Object visitProject(Project project) {
    logger.trace("Project visited " + project.getName() + " with visitor VisitorTags");
    List<String> projectTags = project.getTags();
    boolean hasTag = false;
    for (String tag : projectTags) {
      if (tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if (hasTag) {
      logger.info("Project " + project.getName() + " has tag " + this.tag);
      activitiesWithTag.add(project);
    }
    for (Activity child : project.getChilds()) {
      child.acceptVisitor(this);
    }
    return this.activitiesWithTag;
  }

  @Override
  public Object visitInterval(Interval interval) {
    logger.warn("Interval visited with visitor VisitorTags");
    return null;
  }
}
