package com.makotan.tools.inyuu.provider.jdbc.h2;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.MetadataModel;
import com.makotan.tools.inyuu.model.input.InputData;
import com.makotan.tools.inyuu.model.input.InyuuConfig;
import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import com.makotan.tools.inyuu.provider.ConfigLoad;
import com.makotan.tools.inyuu.provider.jdbc.JdbcProvider;
import com.makotan.tools.inyuu.provider.jdbc.JdbcProviderFactory;
import com.makotan.tools.inyuu.util.Either;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * Created by makotan on 2015/09/22.
 */
public class H2JdbcProviderTest {

    Either<InyuuException, InyuuConfig> inyuuConfigEither;

    @Before
    public void setup() {
        ConfigLoad load = new ConfigLoad();
        InputData data = new InputData();
        data.options = new CliOptions();
        data.options.baseDir = "SampleData";
        data.options.configFileName = "inyuu.yaml";
        inyuuConfigEither = load.load(data);

    }

    @Test
    public void connectionTest() {
        Either<InyuuException, String> either = inyuuConfigEither.flatMap(inyuuConfig -> {
            try {
                InyuuDBInfo test1 = inyuuConfig.jdbc.get("test1");
                assertNotNull(test1);
                JdbcProvider provider = JdbcProviderFactory.getProvider(test1);
                assertNotNull(provider);
                boolean canConnect = provider.canConnect(test1);
                assertTrue(canConnect);
                return Either.right("");
            } catch (Exception e) {
                return Either.left(new InyuuException(e));
            }
        });
        assertTrue(either.isRight());

    }

    @Test
    public void getDBProductTest() {
        Either<InyuuException, String> either = inyuuConfigEither.flatMap(inyuuConfig -> {
            try {
                InyuuDBInfo test1 = inyuuConfig.jdbc.get("test1");
                JdbcProvider provider = JdbcProviderFactory.getProvider(test1);
                System.out.println("ProductName:" + provider.getDatabaseProductName());
                System.out.println("ProductVersion:" + provider.getDatabaseProductVersion());
                System.out.println("MajorVersion:" + provider.getDatabaseMajorVersion());
                System.out.println("MinorVersion:" + provider.getDatabaseMinorVersion());
                assertThat("H2", is(provider.getDatabaseProductName()));
                assertThat(1, is(provider.getDatabaseMajorVersion()));
                assertThat(4, is(provider.getDatabaseMinorVersion()));
                return Either.right("");
            } catch (Exception e) {
                e.printStackTrace();
                return Either.left(new InyuuException(e));
            }
        });
        assertTrue(either.isRight());

    }

    @Test
    public void metadataTest() {
        Either<InyuuException, String> either = inyuuConfigEither.flatMap(inyuuConfig -> {
            try {
                InyuuDBInfo test1 = inyuuConfig.jdbc.get("test1");
                JdbcProvider provider = JdbcProviderFactory.getProvider(test1);
                Flyway flyway = new Flyway();
                flyway.setDataSource(provider.getDataSource());
                flyway.setLocations("filesystem:SampleData/flyway");
                flyway.migrate();

                MetadataModel metadataModel = provider.getMetadataModel();
                assertNotNull(metadataModel);

                return Either.right("");
            } catch (Exception e) {
                e.printStackTrace();
                return Either.left(new InyuuException(e));
            }
        });
        assertTrue(either.isRight());
    }


    protected void printResultSetMetaData(ResultSet rs,boolean printDetail) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.print("columnCount:" + i);
            switch (md.getColumnTypeName(i)) {
                case "VARCHAR":
                    System.out.println("  varchar:" + md.getColumnLabel(i) + ":" + rs.getString(i));
                    break;
                case "BOOLEAN":
                    System.out.println("  boolean:" + md.getColumnLabel(i) + ":" + rs.getBoolean(i));
                    break;
                case "INTEGER":
                    System.out.println("  integer:" + md.getColumnLabel(i) + ":" + rs.getInt(i));
                    break;
                case "SMALLINT":
                    System.out.println("  smallint:" + md.getColumnLabel(i) + ":" + rs.getShort(i));
                    break;
                default: System.out.println("");
            }
            if (!printDetail) {
                continue;
            }
            System.out.println("  CatalogName:" + md.getCatalogName(i));
            System.out.println("  ColumnClassName:" + md.getColumnClassName(i));
            System.out.println("  ColumnLabel:" + md.getColumnLabel(i));
            System.out.println("  ColumnName:" + md.getColumnName(i));
            System.out.println("  ColumnTypeName:" + md.getColumnTypeName(i));
            System.out.println("  SchemaName:" + md.getSchemaName(i));
            System.out.println("  TableName:" + md.getTableName(i));
            System.out.println("  ColumnDisplaySize:" + md.getColumnDisplaySize(i));
            System.out.println("  Precision:" + md.getPrecision(i));
            System.out.println("  Scale:" + md.getScale(i));
            System.out.println("  isAutoIncrement:" + md.isAutoIncrement(i));
            System.out.println("  isCaseSensitive:" + md.isCaseSensitive(i));
            System.out.println("  isCurrency:" + md.isCurrency(i));
            System.out.println("  isDefinitelyWritable:" + md.isDefinitelyWritable(i));
            System.out.println("  isNullable:" + md.isNullable(i));
            System.out.println("  isReadOnly:" + md.isReadOnly(i));
            System.out.println("  isSearchable:" + md.isSearchable(i));
            System.out.println("  isSigned:" + md.isSigned(i));
            System.out.println("  isWritable:" + md.isWritable(i));
            System.out.println("  toString:" + md.toString());
            System.out.println("");

        }
        System.out.println("");

    }

}
