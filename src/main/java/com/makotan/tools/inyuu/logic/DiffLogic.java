package com.makotan.tools.inyuu.logic;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.RenameFieldModel;
import com.makotan.tools.inyuu.model.RenameModel;
import com.makotan.tools.inyuu.model.TableModel;
import com.makotan.tools.inyuu.model.diff.DiffModel;
import com.makotan.tools.inyuu.model.diff.TableMappingModel;
import com.makotan.tools.inyuu.model.input.InputData;
import com.makotan.tools.inyuu.util.Either;
import com.makotan.tools.inyuu.util.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by makotan on 2015/09/20.
 */
public class DiffLogic {

    public Either<InyuuException,Tuple2<InputData,DiffModel>> diff(InputData inputData) {
        Tuple2<InputData,DiffModel> tuple = Tuple2.tuple(inputData, new DiffModel());
        return Either.right(tuple).flatMap(t -> {
            t._2.options = t._1.options;
            return Either.right(t);
        }).flatMap(t -> {
            List<TableModel> nextTableModels = t._1.nextModel.tableModels;
            List<TableModel> currentTableModels = t._1.currentModel.tableModels;
            List<Tuple2<TableMappingModel, String>> currentActionList =
                    currentTableModels.stream().map(current ->
                            currentTableAction(current, nextTableModels, t._1))
                            .collect(Collectors.toList());

            List<String> useNextTableName =
                    currentActionList.stream().
                            map(ca -> ca._2).
                            filter(s -> s != null)
                            .collect(Collectors.toList());

            List<RenameModel> renameModelList = t._1.renameModelList;
            List<String> renameCurrentNameList = renameModelList.stream()
                    .map(renameModel -> {
                        return renameModel.currentName;
                    }).collect(Collectors.toList());

            t._2.createTableModels = createTableList(inputData);

            t._2.dropTableModels = currentActionList.stream()
                    .filter(cat -> cat._1.isDrop())
                    .map(cat -> cat._1.currentTableModel)
                    .collect(Collectors.toList());

            t._2.tableMappingModels = currentActionList.stream()
                    .filter(cat -> !cat._1.isDrop())
                    .map(cat -> cat._1)
                    .collect(Collectors.toList());
            //FIME
            return Either.right(t);
        });
    }

    Tuple2<TableMappingModel,String> currentTableAction(TableModel currentTableModel, List<TableModel> nextTableModels,InputData inputData) {
        String currentTableName = currentTableModel.tableName;
        for (TableModel nextTM : nextTableModels) {
            if (currentTableName.equalsIgnoreCase(nextTM.tableName)) {
                return Tuple2.tuple(new TableMappingModel(currentTableModel,nextTM),nextTM.tableName);
            }
        }
        List<RenameModel> renameModels = inputData.renameModelList;
        for (RenameModel rename : renameModels) {
            if (currentTableName.equalsIgnoreCase(rename.currentName)) {
                for (TableModel nextTM : nextTableModels) {
                    if (rename.nextName.equalsIgnoreCase(nextTM.tableName)) {
                        return Tuple2.tuple(new TableMappingModel(currentTableModel,nextTM,rename),nextTM.tableName);
                    }
                }
            }
        }
        return Tuple2.tuple(new TableMappingModel(currentTableModel,null),null);
    }

    List<TableModel> createTableList(InputData inputData) {
        List<RenameModel> renameModelList = inputData.renameModelList;
        List<TableModel> currentTableList = inputData.currentModel.tableModels;
        List<TableModel> nextTableList = inputData.nextModel.tableModels;
        List<TableModel> createTableList = new ArrayList<>();
        nextTableList.forEach(nextTable -> {
            boolean findCurrent = currentTableList.stream()
                    .filter(currentTable -> nextTable.tableName.equalsIgnoreCase(currentTable.tableName))
                    .findFirst().isPresent();
            if (findCurrent) {
                return;
            }

            List<RenameModel> hitRenameList = renameModelList.stream()
                    .filter(renameModel -> renameModel.nextName.equalsIgnoreCase(nextTable.tableName))
                    .collect(Collectors.toList());
            if (hitRenameList.size() == 0) {
                createTableList.add(nextTable);
                return;
            }
            boolean hasRename = hitRenameList.stream()
                    .filter(rm ->
                                    currentTableList.stream()
                                            .filter(tm -> tm.tableName.equalsIgnoreCase(rm.currentName))
                                            .findFirst().isPresent()
                    ).findFirst().isPresent();
            if (!hasRename) {
                createTableList.add(nextTable);
            }

        });
        return createTableList;
    }

}
