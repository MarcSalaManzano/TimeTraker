package core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Activity {
  private String name;
  private Duration duration;
  private Activity father;
  private LocalDateTime initialDate = null;
  private LocalDateTime finalDate = null;

  public Activity(String name) {
    this.name = name;
    duration = Duration.ZERO;
  }

  public void addFather(Activity a) { this.father = a;}

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
    if(father != null) {
      if (this.getFather().getInitialDate() == null) {
        this.getFather().setInitialDate(initialDate);
        this.getFather().setFinalDate(initialDate);
      }

    }
  }

  public void setFinalDate(LocalDateTime finalDate) {
    this.finalDate = finalDate;
    if(father != null) {
      father.setFinalDate(finalDate);
    }
  }

  public void addDuration(Duration duration) {
    this.duration = this.duration.plus(duration);
    if(father != null) {
      father.addDuration(duration);
    }
  }

  public long getDuration() {
    return duration.getSeconds();
  }

  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String description = "activity:   "+this.name+"   "+
        this.initialDate.format(formatter)+"   "+
        this.finalDate.format(formatter)+"   "+
        this.duration.getSeconds()+"\n";
    if(father != null) {
      description = description+father.toString();
    }
    return description;
  }
}
