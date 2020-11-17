package milestone2;

import Visitor.Visitor;
import core.Activity;
import core.Interval;
import core.Project;
import core.Task;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisitorTags implements Visitor {
  private String tag;
  private List<Activity> tags = new ArrayList();
  private Logger logger = LoggerFactory.getLogger("milestone2.VisitorTags");

  public VisitorTags(String tag) {
    this.tag = tag;
  }

  @Override
  public Object visitTask(Task t) {
    List<String> projectTags = t.getTags();
    boolean hasTag = false;
    for(String tag : projectTags) {
      if(tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if(hasTag) {
      logger.info("Task "+t.getName()+" has tag "+this.tag);
      tags.add(t);
    }
    return null;
  }

  @Override
  public Object visitProject(Project p) {
    List<String> projectTags = p.getTags();
    boolean hasTag = false;
    for(String tag : projectTags) {
      if(tag.equals(this.tag)) {
        hasTag = true;
      }
    }
    if(hasTag) {
      logger.info("Project "+p.getName()+" has tag "+this.tag);
      tags.add(p);
    }
    for(Activity child : p.getChilds()) {
      child.acceptVisitor(this);
    }
    return this.tags;
  }

  @Override
  public Object visitInterval(Interval i) {
    return null;
  }
}
