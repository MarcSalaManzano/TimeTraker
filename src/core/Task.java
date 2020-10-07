package core;

import java.util.ArrayList;
import java.util.List;

public class Task extends Activity{

  private List<Interval> intervals = null;
  private boolean status;

  public Task(Activity father, String name) {
    super(father, name);
    intervals = new ArrayList<>();
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void startTask() {
    Interval newInterval = new Interval();
    intervals.add(newInterval);
    Clock.getInstance().addObserver(newInterval);
  }

  public void stopTask() {
    Clock.getInstance().deleteObserver(intervals.get(intervals.size()-1));
  }
}
