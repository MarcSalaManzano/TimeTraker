package core;

import java.util.List;

public class Task {

  private List<Interval> intervals = null;
  private boolean status;

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
