package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task extends Activity{

  private List<Interval> intervals = null;
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

    if (this.getFather().getInitialDate() == null) {
      this.getFather().setInitialDate(Clock.getInstance().getDate());
      this.getFather().setFinalDate(this.getFather().getInitialDate());
    }

  }

  public void stopTask() {
    Clock.getInstance().deleteObserver(intervals.get(intervals.size()-1));
    this.status = false;
    
  }

}
