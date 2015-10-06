package com.makotan.tools.inyuu.logic;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.MetadataModel;
import com.makotan.tools.inyuu.model.RenameModel;
import com.makotan.tools.inyuu.model.TableModel;
import com.makotan.tools.inyuu.model.diff.DiffModel;
import com.makotan.tools.inyuu.model.diff.TableMappingModel;
import com.makotan.tools.inyuu.model.input.InputData;
import com.makotan.tools.inyuu.model.input.InyuuConfig;
import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import com.makotan.tools.inyuu.provider.ConfigLoad;
import com.makotan.tools.inyuu.provider.jdbc.JdbcProvider;
import com.makotan.tools.inyuu.provider.jdbc.JdbcProviderFactory;
import com.makotan.tools.inyuu.util.Either;
import com.makotan.tools.inyuu.util.Tuple2;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * Created by makotan on 2015/09/26.
 */
public class DiffLogicTest {

    Either<InyuuException, InyuuConfig> inyuuConfigEither;

    Either<InyuuException, MetadataModel> test1Either;

    Either<InyuuException, MetadataModel> test2Either;

    @Before
    public void setup() {
        ConfigLoad load = new ConfigLoad();
        InputData data = new InputData();
        data.options = new CliOptions();
        data.options.baseDir = "SampleData";
        data.options.configFileName = "inyuu.yaml";
        inyuuConfigEither = load.load(data);
        test1Either = inyuuConfigEither.flatMap(inyuuConfig -> {
            try {
                InyuuDBInfo test1 = inyuuConfig.jdbc.get("test1");
                JdbcProvider provider = JdbcProviderFactory.getProvider(test1);
                Flyway flyway = new Flyway();
                flyway.setDataSource(provider.getDataSource());
                flyway.setLocations("filesystem:SampleData/flyway","filesystem:SampleData/flyway2");
                flyway.migrate();

                MetadataModel metadataModel = provider.getMetadataModel();

                return Either.right(metadataModel);
            } catch (Exception e) {
                e.printStackTrace();
                return Either.left(new InyuuException(e));
            }
        });

        test2Either = inyuuConfigEither.flatMap(inyuuConfig -> {
            try {
                InyuuDBInfo test1 = inyuuConfig.jdbc.get("test2");
                JdbcProvider provider = JdbcProviderFactory.getProvider(test1);
                Flyway flyway = new Flyway();
                flyway.setDataSource(provider.getDataSource());
                flyway.setLocations("filesystem:SampleData/flyway","filesystem:SampleData/flyway3");
                flyway.migrate();

                MetadataModel metadataModel = provider.getMetadataModel();

                return Either.right(metadataModel);
            } catch (Exception e) {
                e.printStackTrace();
                return Either.left(new InyuuException(e));
            }
        });

    }

    @Test
    public void currentTableActionTest() {
        DiffLogic diffLogic = new DiffLogic();
        InputData inputData = new InputData();

        RenameModel renameModel = new RenameModel();
        renameModel.currentName = "TEST_FROM";
        renameModel.nextName = "TEST_TO";
        inputData.renameModelList.add(renameModel);

        test1Either.processRight(test1Model -> {
            test2Either.processRight(test2Model -> {
                inputData.currentModel = test1Model;
                inputData.nextModel = test2Model;
                List<Tuple2<TableMappingModel,String>> tupleList = test1Model.tableModels.stream().map(tm -> {
                    return diffLogic.currentTableAction(tm , test2Model.tableModels , inputData);
                }).collect(Collectors.toList());
                tupleList.forEach( t2 -> {
                    System.out.println(t2._1.currentTableModel.tableName);
                    System.out.println(" isDrop     " + t2._1.isDrop());
                    System.out.println(" isNoAction " + t2._1.isNoAction());
                    System.out.println(" isRename   " + t2._1.isRename());
                });
            });
        });

    }


    @Test
    public void createTableTest() {
        DiffLogic diffLogic = new DiffLogic();
        InputData inputData = new InputData();

        RenameModel renameModel = new RenameModel();
        renameModel.currentName = "TEST_FROM";
        renameModel.nextName = "TEST_TO";
        inputData.renameModelList.add(renameModel);



        test1Either.processRight(test1Model -> {
            test2Either.processRight(test2Model -> {
                inputData.currentModel = test1Model;
                inputData.nextModel = test2Model;
                List<TableModel> createTableList = diffLogic.createTableList(inputData);
                assertThat(1,is(createTableList.size()));
            });
        });

    }

    @Test
    public void diffTest() {
        DiffLogic diffLogic = new DiffLogic();
        InputData inputData = new InputData();

        RenameModel renameModel = new RenameModel();
        renameModel.currentName = "TEST_FROM";
        renameModel.nextName = "TEST_TO";
        inputData.renameModelList.add(renameModel);

        test1Either.processRight(test1Model -> {
            test2Either.processRight(test2Model -> {
                inputData.currentModel = test1Model;
                inputData.nextModel = test2Model;
            });
        });

        Either<InyuuException, Tuple2<InputData, DiffModel>> diff = diffLogic.diff(inputData);
        diff.processRight(r -> {
            assertThat(1,is(r._2.createTableModels.size()));
            assertThat(1,is(r._2.dropTableModels.size()));
            assertThat(3,is(r._2.tableMappingModels.size()));
        });

    }

}
