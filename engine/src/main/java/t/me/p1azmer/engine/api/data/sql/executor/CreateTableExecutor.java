package t.me.p1azmer.engine.api.data.sql.executor;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.data.StorageType;
import t.me.p1azmer.engine.api.data.connection.AbstractDataConnector;
import t.me.p1azmer.engine.api.data.sql.SQLColumn;
import t.me.p1azmer.engine.api.data.sql.SQLExecutor;
import t.me.p1azmer.engine.api.data.sql.SQLQueries;
import t.me.p1azmer.engine.api.data.sql.column.ColumnFormer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CreateTableExecutor extends SQLExecutor<Void> {

    private final StorageType storageType;
    private final List<SQLColumn> columns;

    private CreateTableExecutor(@NotNull String table, @NotNull StorageType storageType) {
        super(table);
        this.storageType = storageType;
        this.columns = new ArrayList<>();
    }

    @NotNull
    public static CreateTableExecutor builder(@NotNull String table, @NotNull StorageType storageType) {
        return new CreateTableExecutor(table, storageType);
    }

    @NotNull
    public CreateTableExecutor columns(@NotNull SQLColumn... columns) {
        return this.columns(Arrays.asList(columns));
    }

    @NotNull
    public CreateTableExecutor columns(@NotNull List<SQLColumn> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
        return this;
    }

    @Override
    @NotNull
    public Void execute(@NotNull AbstractDataConnector connector) {
        if (this.columns.isEmpty()) return null;

        String id = "`id` " + ColumnFormer.INTEGER.build(this.storageType, 11);
        if (this.storageType == StorageType.SQLITE) {
            id += " PRIMARY KEY AUTOINCREMENT";
        }
        else {
            id += " PRIMARY KEY AUTO_INCREMENT";
        }

        String columns = id + "," + this.columns.stream()
                .map(column -> column.getNameEscaped() + " " + column.formatType(this.storageType))
                .collect(Collectors.joining(", "));

        String sql = "CREATE TABLE IF NOT EXISTS " + this.getTable() + "(" + columns + ") CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;";

        SQLQueries.executeStatement(connector, sql);
        return null;
    }
}