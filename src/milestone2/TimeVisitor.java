package milestone2;

import Visitor.Visitor;
import core.Activity;
import core.Interval;
import core.Project;
import core.Task;

import java.time.Duration;
import java.time.LocalDateTime;


public class TimeVisitor  implements Visitor {

    LocalDateTime initialDate;
    LocalDateTime endDate;


    public TimeVisitor(LocalDateTime initialDate, LocalDateTime endDate) {
        this.initialDate = initialDate;
        this.endDate = endDate;

    }

    @Override
    public Object visitTask(Task t) {
        int duration = 0;
        if(initialDate.isBefore(t.getFinalDate()) && endDate.isAfter(t.getInitialDate())) {
            for (Interval i : t.getIntervals())
                duration += (int) i.acceptVisitor(this);
        }
        return duration;
    }

    @Override
    public Object visitProject(Project p) {
        int duration = 0;
        for(Activity a : p.getChilds()) {
            duration += (int) a.acceptVisitor(this);
        }
        return duration;
    }

    @Override
    public Object visitInterval(Interval i) {
        if (initialDate.isBefore(i.getFinalDate()) && endDate.isAfter(i.getInitialDate()))
            if (initialDate.isBefore(i.getInitialDate()) && endDate.isAfter(i.getFinalDate()))
                return  (int) i.getDuration().getSeconds();
            else if (initialDate.isBefore(i.getInitialDate()) && endDate.isBefore(i.getFinalDate()))
                return (int) Duration.between(i.getInitialDate(), endDate).getSeconds();
            else if (initialDate.isAfter(i.getInitialDate()) && endDate.isAfter(i.getFinalDate()))
                return (int) Duration.between(initialDate, i.getFinalDate()).getSeconds();
            else
                return (int) Duration.between(initialDate, endDate).getSeconds();
        return 0;
    }
}
