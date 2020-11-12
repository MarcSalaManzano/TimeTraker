package Visitor;

import core.Interval;
import core.Project;
import core.Task;

public interface Visitor {
  public Object visitTask(Task t);
  public Object visitProject(Project p);
  public Object visitInterval(Interval i);
}
