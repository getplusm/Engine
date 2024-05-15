package t.me.p1azmer.engine.utils.cooldown;

import java.util.HashMap;
import java.util.Map;

public class CoolDown<T> {

  private Map<T, TimeController> items;
  private boolean autoRemove = true;

  public CoolDown() {
    this.items = new HashMap<>();
  }

  public CoolDown(int size) {
    this.items = new HashMap<>(size);
  }

  public CoolDown(boolean autoRemove) {
    this.items = new HashMap<>();
    this.autoRemove = autoRemove;
  }

  public CoolDown(boolean autoRemove, int size) {
    this.items = new HashMap<>(size);
    this.autoRemove = autoRemove;
  }


  public boolean timeOver(T item) {
    if (!items.containsKey(item)) return true;
    if (items.get(item).timeLeft() > 0) return false;
    if (autoRemove) {
      items.remove(item);
    }
    return true;
  }

  public void addItem(T item, long time) {
    items.put(item, new TimeController(time));
  }

  public long timeLeft(T item) {
    TimeController controller = items.get(item);
    return controller == null ? 0 : Math.max(controller.timeLeft(), 0);
  }

  public int secondsLeft(T item) {
    return (int) (timeLeft(item) / 1000);
  }

  public double secondsLeftCorrected(T item, double numbers) {
    numbers = Math.pow(10, Math.min(numbers, 3));
    return (Math.round(timeLeft(item) / 1000d * numbers) / numbers);
  }

  public double secondsLeftCorrected(T item) {
    return (Math.round(timeLeft(item) / 100d) / 10d);
  }

  public void clearCache() {
    items.entrySet().removeIf((i) -> i.getValue().timeLeft() < 0);
  }

  public Map<T, TimeController> getItems() {
    return items;
  }

  public void setItems(Map<T, TimeController> items) {
    this.items = items;
  }

  public boolean isAutoRemove() {
    return autoRemove;
  }

  public void setAutoRemove(boolean autoRemove) {
    this.autoRemove = autoRemove;
  }

  @Override
  public String toString() {
    return "CoolDown{" +
      "items=" + items +
      ", autoRemove=" + autoRemove +
      '}';
  }
}