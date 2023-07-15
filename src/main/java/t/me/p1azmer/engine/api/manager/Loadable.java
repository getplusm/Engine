package t.me.p1azmer.engine.api.manager;

public interface Loadable {

    void setup();

    void shutdown();

    default void reload() {
        this.shutdown();
        this.setup();
    }
}