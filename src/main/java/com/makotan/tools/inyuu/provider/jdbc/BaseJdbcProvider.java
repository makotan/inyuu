package com.makotan.tools.inyuu.provider.jdbc;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.ColumnModel;
import com.makotan.tools.inyuu.model.MetadataModel;
import com.makotan.tools.inyuu.model.TableModel;
import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import org.flywaydb.core.internal.dbsupport.DbSupport;
import org.flywaydb.core.internal.dbsupport.DbSupportFactory;
import org.flywaydb.core.internal.util.jdbc.DriverDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by makotan on 2015/09/22.
 */
public class BaseJdbcProvider implements JdbcProvider {
    Logger logger = LoggerFactory.getLogger(BaseJdbcProvider.class);

    protected InyuuDBInfo dbInfo;
    protected DbSupport dbSupport;
    protected String databaseProductName;
    protected String databaseProductVersion;
    protected Integer databaseMajorVersion;
    protected Integer databaseMinorVersion;


    public BaseJdbcProvider() {

    }

    public BaseJdbcProvider(InyuuDBInfo dbInfo) {
        setInyuuDBInfo(dbInfo);
    }

    @Override
    public void setInyuuDBInfo(InyuuDBInfo dbInfo) {
        try {
            this.dbSupport = DbSupportFactory.createDbSupport(getDataSource(dbInfo).getConnection(),true);
            this.dbInfo = dbInfo;
        } catch (SQLException e) {
            logger.error("getConnection " + dbInfo.url);
        }
    }

    @Override
    public MetadataModel getMetadataModel() {
        final MetadataModel model = new MetadataModel();
        metadata(metadata -> {
            model.tableModels = new ArrayList<>();
            ResultSet schemas = metadata.getSchemas();
            List<String> schemaList = new ArrayList<>();
            String defautSchemaName = "";
            while (schemas.next()) {
                if (schemas.getBoolean("IS_DEFAULT")) {
                    defautSchemaName = schemas.getString("TABLE_SCHEM");
                }
                String schemaName = schemas.getString("TABLE_SCHEM");
                schemaList.add(schemaName);
            }

            ResultSet catalogs = metadata.getCatalogs();
            String catalog = null;
            while (catalogs.next()) {
                catalog = catalogs.getString("TABLE_CAT");
            }

            ResultSet tables = metadata.getTables(catalog, defautSchemaName, "%", new String[] {"TABLE"});
            while (tables.next()) {
                TableModel table = new TableModel();
                model.tableModels.add(table);
                table.tableName = tables.getString("TABLE_NAME");
                table.tableSchema = tables.getString("TABLE_SCHEM");
                table.comment = tables.getString("REMARKS");
                table.fieldModes = new ArrayList<>();

                ResultSet columns = metadata.getColumns(catalog, defautSchemaName, table.tableName, "%");
                while (columns.next()) {
                    ColumnModel column = new ColumnModel();
                    column.columnName = columns.getString("COLUMN_NAME");
                    column.typeName = columns.getString("TYPE_NAME");
                    column.columnSize = columns.getInt("COLUMN_SIZE");
                    column.decimalDigits = columns.getInt("DECIMAL_DIGITS");
                    column.comment = columns.getString("REMARKS");
                    column.columnDef = columns.getString("COLUMN_DEF");
                    column.isNullable = columns.getString("IS_NULLABLE");
                    column.isAutoIncrement = columns.getString("IS_AUTOINCREMENT");
                    column.ordinalPosition = columns.getInt("ORDINAL_POSITION");
                    column.sqlDataType = columns.getInt("SQL_DATA_TYPE");
                    column.sqlDatetimeSub = columns.getInt("SQL_DATETIME_SUB");
                    column.notNull = ! "NO".equalsIgnoreCase(column.isNullable);
                    table.fieldModes.add(column);
                }
            }

            return "";
        });
        return model;
    }


    @Override
    public int getDatabaseMinorVersion() {
        initDatabaseInfo();
        return databaseMinorVersion;
    }

    @Override
    public int getDatabaseMajorVersion() {
        initDatabaseInfo();
        return databaseMajorVersion;
    }

    @Override
    public String getDatabaseProductName() {
        initDatabaseInfo();
        return databaseProductName;
    }

    @Override
    public String getDatabaseProductVersion() {
        initDatabaseInfo();
        return databaseProductVersion;
    }

    protected synchronized void initDatabaseInfo() {
        if (databaseProductName != null) {
            return;
        }
        metadata(metadata -> {
            databaseProductName = metadata.getDatabaseProductName();
            databaseProductVersion = metadata.getDatabaseProductVersion();
            databaseMajorVersion = metadata.getDatabaseMajorVersion();
            databaseMinorVersion = metadata.getDatabaseMinorVersion();
            return "";
        });
    }

    @Override
    public DatabaseMetaData getMetaData() {
        try {
            return getConnection().getMetaData();
        } catch (SQLException e) {
            throw new InyuuException(e);
        }
    }

    @Override
    public <R> R metadata(SqlFunction<DatabaseMetaData, R> fn) {
        return connection(connection -> {
            DatabaseMetaData metaData = getConnection().getMetaData();
            return fn.apply(metaData);
        });
    }

    @Override
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new InyuuException(e);
        }
    }

    @Override
    public <R> R connection(SqlFunction<Connection, R> fn) {
        try (Connection conn = getDataSource().getConnection()){
            return fn.apply(conn);
        } catch (SQLException e) {
            throw new InyuuException(e);
        }
    }

    @Override
    public DataSource getDataSource() {
        if (this.dbInfo == null) {
            throw new InyuuException("dbInfo is null");
        }
        return getDataSource(this.dbInfo);
    }

    protected DataSource getDataSource(InyuuDBInfo dbInfo) {
        return new DriverDataSource(getClass().getClassLoader(),dbInfo.driverClassName,dbInfo.url,dbInfo.user,dbInfo.password);
    }

    @Override
    public boolean canConnect(InyuuDBInfo dbInfo) {
        DataSource dataSource = getDataSource(dbInfo);
        try {
            dataSource.getConnection().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
