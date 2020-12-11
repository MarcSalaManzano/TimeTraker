package core;

public class IdConstructor {
  private static int actualId = 0;

  private IdConstructor() {}

  public static int getNextId() {
    int retorn = actualId;
    actualId += 1;
    return retorn;
  }
}
