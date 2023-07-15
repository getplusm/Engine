package t.me.p1azmer.engine.api.data.sql.column;

import org.jetbrains.annotations.NotNull;
import t.me.p1azmer.engine.api.data.StorageType;

public interface ColumnFormer {

    ColumnFormer STRING = (storageType, length) -> {
        if (length < 1 || storageType == StorageType.SQLITE) {
            return storageType == StorageType.SQLITE ? "TEXT NOT NULL" : "MEDIUMTEXT NOT NULL";
        }
        return "VARCHAR(" + length + ") CHARACTER SET utf8 NOT NULL";
    };

    ColumnFormer INTEGER = (storageType, length) -> {
        if (length < 1 || storageType == StorageType.SQLITE) {
            return "INTEGER NOT NULL";
        }
        return "INT(" + length + ") NOT NULL";
    };

    ColumnFormer DOUBLE = (storageType, length) -> {
        return storageType == StorageType.SQLITE ? "REAL NOT NULL" : "DOUBLE NOT NULL";
    };

    ColumnFormer LONG = (storageType, length) -> {
        return length < 1 || storageType == StorageType.SQLITE ? "BIGINT NOT NULL" : "BIGINT(" + length + ") NOT NULL";
    };

    ColumnFormer BOOLEAN = (storageType, length) -> {
        return storageType == StorageType.SQLITE ? "INTEGER NOT NULL" : "TINYINT(1) NOT NULL";
    };

    @NotNull String build(@NotNull StorageType storageType, int length);
}