package core;

import java.util.List;

public class Task {

  List<Interval> intervals = null;
  boolean status;

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }
}
