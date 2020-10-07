package core;

import java.util.ArrayList;
import java.util.List;

public class Project extends Activity{

  private List<Activity> childs;

  public Project(String name) {
    super(name);
    childs = new ArrayList<>();
  }

  public void addActivity(Activity a)
  {
    childs.add(a);
    a.addFather(this);
  }

  public void removeActivity(Activity a)
  {
    childs.remove(a);
  }
}
