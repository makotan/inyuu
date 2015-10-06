package com.makotan.tools.inyuu.provider.jdbc;

import com.makotan.tools.inyuu.model.MetadataModel;
import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import com.makotan.tools.inyuu.provider.DiffProvider;
import com.makotan.tools.inyuu.provider.InputProvider;
import com.makotan.tools.inyuu.provider.OutputProvider;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Created by makotan on 2015/09/22.
 */
public interface JdbcProvider extends InputProvider,OutputProvider,DiffProvider {

    @FunctionalInterface
    interface SqlFunction<T,R> {
        R apply(T t) throws SQLException;
    }

    int getDatabaseMinorVersion();

    int getDatabaseMajorVersion();

    String getDatabaseProductName();

    String getDatabaseProductVersion();

    DatabaseMetaData getMetaData();

    <R> R metadata(SqlFunction<DatabaseMetaData, R> fn);

    DataSource getDataSource();

    Connection getConnection();

    <R> R connection(SqlFunction<Connection, R> fn);

    boolean canConnect(InyuuDBInfo dbInfo);

    void setInyuuDBInfo(InyuuDBInfo dbInfo);

    MetadataModel getMetadataModel();

}
