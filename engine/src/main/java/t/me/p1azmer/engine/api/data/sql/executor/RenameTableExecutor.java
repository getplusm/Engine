package t.me.p1azmer.engine.api.data.sql.executor;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.data.StorageType;
import t.me.p1azmer.engine.api.data.connection.AbstractDataConnector;
import t.me.p1azmer.engine.api.data.sql.SQLExecutor;
import t.me.p1azmer.engine.api.data.sql.SQLQueries;

public final class RenameTableExecutor extends SQLExecutor<Void> {

    private final StorageType     storageType;
    private String renameTo;

    private RenameTableExecutor(@NotNull String table, @NotNull StorageType storageType) {
        super(table);
        this.storageType = storageType;
    }

    @NotNull
    public static RenameTableExecutor builder(@NotNull String table, @NotNull StorageType storageType) {
        return new RenameTableExecutor(table, storageType);
    }

    @NotNull
    public RenameTableExecutor renameTo(@NotNull String renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    @Override
    @NotNull
    public Void execute(@NotNull AbstractDataConnector connector) {
        if (this.renameTo == null || this.renameTo.isEmpty()) return null;
        if (!SQLQueries.hasTable(connector, this.getTable())) return null;

        StringBuilder sql = new StringBuilder();
        if (this.storageType == StorageType.MYSQL) {
            sql.append("RENAME TABLE ").append(this.getTable()).append(" TO ").append(this.renameTo).append(";");
        }
        else {
            sql.append("ALTER TABLE ").append(this.getTable()).append(" RENAME TO ").append(this.renameTo);
        }
        SQLQueries.executeStatement(connector, sql.toString());
        return null;
    }
}