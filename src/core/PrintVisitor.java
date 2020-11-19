package core;

import Visitor.Visitor;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintVisitor implements Visitor {

  private Logger logger = LoggerFactory.getLogger("core.PrintVisitor");

  @Override
  public Object visitTask(Task t) {
    t.getLastInterval().acceptVisitor(new PrintVisitor());
    logger.info(t.toString());
    t.getFather().acceptVisitor(new PrintVisitor());
    return null;
  }

  @Override
  public String visitProject(Project p) {
    logger.info(p.toString());
    return null;
  }

  @Override
  public Object visitInterval(Interval i) {
    logger.info(i.toString());
    return null;
  }
}
