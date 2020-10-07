package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task extends Activity{

  private List<Interval> intervals;
  private boolean status;

  public Task(String name) {
    super(name);
    intervals = new ArrayList<>();
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void startTask() {
    Interval newInterval = new Interval(this);
    intervals.add(newInterval);
    Clock.getInstance().addObserver(newInterval);
    this.setInitialDate(Clock.getInstance().getDate());
    this.setFinalDate(Clock.getInstance().getDate());
    this.status = true;

  }

  public void stopTask() {
    Clock.getInstance().deleteObserver(intervals.get(intervals.size()-1));
    this.status = false;
    
  }

  @Override
  public String toString() {
    String description = intervals.get(intervals.size()-1).toString();
    return description+super.toString();
  }
}
