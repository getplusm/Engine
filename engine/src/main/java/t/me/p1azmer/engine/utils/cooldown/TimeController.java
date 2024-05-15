package t.me.p1azmer.engine.utils.cooldown;

public class TimeController {

  private long initialTime;
  private long waitTime;
  private long endTime;


  public TimeController(long waitTime) {
    this.initialTime = System.currentTimeMillis();
    this.waitTime = waitTime;
    this.endTime = initialTime + waitTime;
  }

  public TimeController(long initialTime, long waitTime, long endTime) {
    this.initialTime = initialTime;
    this.waitTime = waitTime;
    this.endTime = endTime;
  }

  public long timeLeft() {
    return endTime - (System.currentTimeMillis());
  }

  public long getInitialTime() {
    return initialTime;
  }

  public void setInitialTime(long initialTime) {
    this.initialTime = initialTime;
  }

  public long getWaitTime() {
    return waitTime;
  }

  public void setWaitTime(long waitTime) {
    this.waitTime = waitTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  @Override
  public String toString() {
    return "TimeController{" +
      "initialTime=" + initialTime +
      ", waitTime=" + waitTime +
      ", endTime=" + endTime +
      '}';
  }
}