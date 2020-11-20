package secondmilestone;

import core.Activity;
import core.Interval;
import core.Project;
import core.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visitor.Visitor;

/*
Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para calcular el tiempo total que se ha invertido en proyectos o tareas dado un intervalo de tiempo.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements
 */
public class TimeVisitor implements Visitor {

  LocalDateTime initialDate;
  LocalDateTime endDate;
  private Logger logger = LoggerFactory.getLogger("secondmilestone.TimeVisitor");

  public TimeVisitor(LocalDateTime initialDate, LocalDateTime endDate) {
    logger.debug("Visitor's initial date is " + initialDate + " and end date is " + endDate);
    this.initialDate = initialDate;
    this.endDate = endDate;
  }

  @Override
  public Object visitTask(Task task) {
    int duration = 0;
    if (initialDate.isBefore(task.getFinalDate()) && endDate.isAfter(task.getInitialDate())) {
      logger.trace("Task " + task.getName() + " visited with visitor TimeVisitor");
      for (Interval interval : task.getIntervals()) {
        duration += (int) interval.acceptVisitor(this);
      }
      logger.debug("Total time for this task is: " + duration);
    }
    return duration;
  }

  @Override
  public Object visitProject(Project project) {
    logger.trace("Project " + project.getName() + " visited with visitor VisitorTags");
    int duration = 0;
    for (Activity activity : project.getChilds()) {
      duration += (int) activity.acceptVisitor(this);
    }
    logger.debug("Total time for this project is: " + duration);
    return duration;
  }

  /*
  Existen 5 casos distintos al calcular el tiempo de un intervalo: el intervalo esta fuera de las
  fechas especificadas, el intervalo empieza fuera de las fechas pero termina dentro, el intervalo
  empieza entre las fechas pero termina fuera, el intervalo empieza fuera de las fechas pero
  transcurre dentro de ellas y el intervalo solo existe entre las fechas.
   */
  @Override
  public Object visitInterval(Interval interval) {
    if (initialDate.isBefore(interval.getFinalDate())
        && endDate.isAfter(interval.getInitialDate())) {
      logger.trace("Interval visited with visitor TimeVisitor");
      if (initialDate.isBefore(interval.getInitialDate())
          && endDate.isAfter(interval.getFinalDate())) {
        logger.debug("Interval starts and stops inside TimeVisitor's duration");
        return (int) interval.getDuration().getSeconds();
      } else if (initialDate.isBefore(interval.getInitialDate())
          && endDate.isBefore(interval.getFinalDate())) {
        logger.debug(
            "Interval starts before TimeVisitor's initial date "
                + "and stops inside TimeVisitor's duration");
        return (int) Duration.between(interval.getInitialDate(), endDate).getSeconds();
      } else if (initialDate.isAfter(interval.getInitialDate())
          && endDate.isAfter(interval.getFinalDate())) {
        logger.debug(
            "Interval ends after TimeVisitor's end date and starts inside TimeVisitor's duration");
        return (int) Duration.between(initialDate, interval.getFinalDate()).getSeconds();
      } else {
        logger.debug("Interval starts and stops outside the TimeVisitor's duration");
        return (int) Duration.between(initialDate, endDate).getSeconds();
      }
    }
    return 0;
  }
}
