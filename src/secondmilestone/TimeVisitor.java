package secondmilestone;

import Visitor.Visitor;
import core.Activity;
import core.Interval;
import core.Project;
import core.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

/*
Implementación del patrón de diseño visitor, que utilizamos para recorrer el árbol de actividades
para calcular el tiempo total que se ha invertido en proyectos o tareas dado un intervalo de tiempo.
Las clase Activity hace la función de Element, mientras que Project, Task y Interval serían
ConcreteElements
 */
public class TimeVisitor  implements Visitor {

    LocalDateTime initialDate;
    LocalDateTime endDate;
    private Logger logger = LoggerFactory.getLogger("secondmilestone.TimeVisitor");


    public TimeVisitor(LocalDateTime initialDate, LocalDateTime endDate) {
        logger.debug("Visitor's initial date is " + initialDate + " and end date is " + endDate);
        this.initialDate = initialDate;
        this.endDate = endDate;
    }

    @Override
    public Object visitTask(Task t) {
        int duration = 0;
        if(initialDate.isBefore(t.getFinalDate()) && endDate.isAfter(t.getInitialDate())) {
            logger.trace("Task "+t.getName()+" visited with visitor TimeVisitor");
            for (Interval i : t.getIntervals())
                duration += (int) i.acceptVisitor(this);
            logger.debug("Total time for this task is: " + duration);
        }
        return duration;
    }

    @Override
    public Object visitProject(Project p) {
        logger.trace("Project "+p.getName()+" visited with visitor VisitorTags");
        int duration = 0;
        for(Activity a : p.getChilds()) {
            duration += (int) a.acceptVisitor(this);
        }
        logger.debug("Total time for this project is: " + duration);
        return duration;
    }

    @Override
    public Object visitInterval(Interval i) {
        if (initialDate.isBefore(i.getFinalDate()) && endDate.isAfter(i.getInitialDate())) {
            logger.trace("Interval visited with visitor TimeVisitor");
            if (initialDate.isBefore(i.getInitialDate()) && endDate.isAfter(i.getFinalDate())) {
                logger.debug("Interval starts and stops inside TimeVisitor's duration");
                return (int) i.getDuration().getSeconds();
            }
            else if (initialDate.isBefore(i.getInitialDate()) && endDate.isBefore(i.getFinalDate())) {
                logger.debug("Interval starts before TimeVisitor's initial date and stops inside TimeVisitor's duration");
                return (int) Duration.between(i.getInitialDate(), endDate).getSeconds();
            }
            else if (initialDate.isAfter(i.getInitialDate()) && endDate.isAfter(i.getFinalDate())) {
                logger.debug("Interval ends after TimeVisitor's end date and starts inside TimeVisitor's duration");
                return (int) Duration.between(initialDate, i.getFinalDate()).getSeconds();
            }
            else {
                logger.debug("Interval starts and stops outside the TimeVisitor's duration");
                return (int) Duration.between(initialDate, endDate).getSeconds();
            }
        }
        return 0;
    }
}
