package core;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Activity {
  private String name;
  private Duration duration;
  private Activity father;
  private LocalDateTime initialDate = null;
  private LocalDateTime finalDate = null;

  public Activity(Activity father, String name) {
    this.name = name;
    this.father = father;
    duration = Duration.ZERO;
  }

  public Activity getFather() {
    return father;
  }

  public LocalDateTime getInitialDate() {
    return initialDate;
  }

  public LocalDateTime getFinalDate() {
    return finalDate;
  }

  public void setInitialDate(LocalDateTime initialDate) {
    this.initialDate = initialDate;
  }

  public void setFinalDate(LocalDateTime finalDate) {
    this.finalDate = finalDate;
  }

  public void addDuration(Duration duration) {
    this.duration = this.duration.plus(duration);
  }

  public long getDuration() {
    return duration.getSeconds();
  }
}
