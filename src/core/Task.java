package core;

import java.util.List;

public class Task extends Activity{

  private List<Interval> intervals = null;
  private boolean status;

  public Task(Activity father, String name) {
    super(father, name);
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
