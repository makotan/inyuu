package com.makotan.tools.inyuu.logic;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.ColumnModel;
import com.makotan.tools.inyuu.model.RenameColumnModel;
import com.makotan.tools.inyuu.model.RenameModel;
import com.makotan.tools.inyuu.model.TableModel;
import com.makotan.tools.inyuu.model.diff.DiffModel;
import com.makotan.tools.inyuu.model.diff.TableColumnMappingModel;
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
        return diffTableModel(inputData);
    }

    Either<InyuuException,Tuple2<InputData,DiffModel>> diffTableModel(InputData inputData) {
        Tuple2<InputData,DiffModel> tuple = Tuple2.tuple(inputData, new DiffModel());
        return Either.right(tuple).flatMap(t -> {
            t._2.options = t._1.options;
            return Either.right(t);
        }).flatMap(t -> {
            List<TableModel> nextTableModels = t._1.nextModel.tableModels;
            List<TableModel> currentTableModels = t._1.currentModel.tableModels;
            List<TableMappingModel> currentActionList =
                    currentTableModels.stream().map(current ->
                            currentTableAction(current, nextTableModels, t._1))
                            .collect(Collectors.toList());

            t._2.createTableModels = createTableList(inputData);

            t._2.dropTableModels = currentActionList.stream()
                    .filter(cat -> cat.isDrop())
                    .map(cat -> cat.currentTableModel)
                    .collect(Collectors.toList());

            t._2.tableMappingModels = currentActionList.stream()
                    .filter(cat -> !cat.isDrop())
                    .collect(Collectors.toList());
            //FIME
            return Either.right(t);
        });
    }

    TableMappingModel currentTableAction(TableModel currentTableModel, List<TableModel> nextTableModels,InputData inputData) {
        String currentTableName = currentTableModel.tableName;
        for (TableModel nextTM : nextTableModels) {
            if (currentTableName.equalsIgnoreCase(nextTM.tableName)) {
                return new TableMappingModel(currentTableModel,nextTM);
            }
        }
        List<RenameModel> renameModels = inputData.renameModelList;
        for (RenameModel rename : renameModels) {
            if (currentTableName.equalsIgnoreCase(rename.currentName)) {
                for (TableModel nextTM : nextTableModels) {
                    if (rename.nextName.equalsIgnoreCase(nextTM.tableName)) {
                        return new TableMappingModel(currentTableModel,nextTM,rename);
                    }
                }
            }
        }
        return new TableMappingModel(currentTableModel,null);
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

    List<TableColumnMappingModel> createAddColumnModel(TableMappingModel tmm,InputData inputData) {
        List<String> currentColumns = tmm.currentTableModel.fieldModes.stream()
                .map(cm -> cm.columnName).collect(Collectors.toList());
        List<String> nextColumnNames = tmm.nextTableModel.fieldModes.stream()
                .map(cm -> cm.columnName).collect(Collectors.toList());
        List<String> renameColumnNames = inputData.renameColumnModels.stream()
                .filter(rcm -> rcm.tableName.equals(tmm.nextTableModel.tableName))
                .filter(rcm -> nextColumnNames.contains(rcm.nextColumnName))
                .filter(rcm -> currentColumns.contains(rcm.currentColumnName))
                .map(rcm -> rcm.nextColumnName)
                .collect(Collectors.toList());

        return tmm.nextTableModel.fieldModes.stream()
                .filter(cm -> ! currentColumns.contains(cm.columnName))
                .filter(cm -> ! renameColumnNames.contains(cm.columnName))
                .map(cm -> new TableColumnMappingModel(tmm,null,cm))
                .collect(Collectors.toList());
    }

    List<TableColumnMappingModel> createDropColumnModel(TableMappingModel tmm,InputData inputData) {
        List<String> currentColumns = tmm.currentTableModel.fieldModes.stream()
                .map(cm -> cm.columnName).collect(Collectors.toList());
        List<String> nextColumnNames = tmm.nextTableModel.fieldModes.stream()
                .map(cm -> cm.columnName).collect(Collectors.toList());
        List<String> renameColumnNames = inputData.renameColumnModels.stream()
                .filter(rcm -> rcm.tableName.equals(tmm.nextTableModel.tableName))
                .filter(rcm -> nextColumnNames.contains(rcm.nextColumnName))
                .filter(rcm -> currentColumns.contains(rcm.currentColumnName))
                .map(rcm -> rcm.currentColumnName)
                .collect(Collectors.toList());

        return tmm.currentTableModel.fieldModes.stream()
                .filter(nm -> ! nextColumnNames.contains(nm.columnName))
                .filter(nm -> ! renameColumnNames.contains(nm.columnName))
                .map(nm -> new TableColumnMappingModel(tmm,nm,null))
                .collect(Collectors.toList());

    }

    List<TableColumnMappingModel> createModiyColumnModel(TableMappingModel tmm,InputData inputData, List<TableColumnMappingModel> addColumn,List<TableColumnMappingModel> dropColumn) {
        List<String> addColumnName = addColumn.stream()
                .map(c -> c.nextCoulmn.columnName).collect(Collectors.toList());
        List<String> dropColumnName = dropColumn.stream()
                .map(c -> c.currentColumn.columnName).collect(Collectors.toList());

        List<ColumnModel> currentColumns = tmm.currentTableModel.fieldModes.stream()
                .filter(cn -> !dropColumnName.contains(cn.columnName))
                .collect(Collectors.toList());
        List<String> currentColumnNames = currentColumns.stream()
                .map(c -> c.columnName)
                .collect(Collectors.toList());
        List<ColumnModel> nextColumns = tmm.nextTableModel.fieldModes.stream()
                .filter(cn -> ! addColumnName.contains(cn.columnName))
                .collect(Collectors.toList());
        List<String> nextColumnNames = nextColumns.stream()
                .map(c -> c.columnName)
                .collect(Collectors.toList());
        List<RenameColumnModel> renameColumns = inputData.renameColumnModels.stream()
                .filter(rcm -> rcm.tableName.equals(tmm.nextTableModel.tableName))
                .filter(rcm -> nextColumnNames.contains(rcm.nextColumnName))
                .filter(rcm -> currentColumnNames.contains(rcm.currentColumnName))
                .collect(Collectors.toList());

        List<TableColumnMappingModel> modify = new ArrayList<>();

        nextColumns.stream().forEach(ncm -> {
            if (currentColumnNames.contains(ncm.columnName)) {
                ColumnModel ccm = currentColumns.stream()
                        .filter(cc -> cc.columnName.equals(ncm.columnName)).findFirst().get();
                if (! ncm.equals(ccm)) {
                    modify.add(new TableColumnMappingModel(tmm,ccm,ncm));
                }
            } else {
                renameColumns.stream()
                        .filter(nc -> nc.nextColumnName.equals(ncm.columnName))
                        .findFirst().ifPresent(rcm -> {
                    ColumnModel ccm = currentColumns.stream()
                            .filter(cc -> cc.columnName.equals(rcm.currentColumnName)).findFirst().get();
                    if (ccm != null) {
                        modify.add(new TableColumnMappingModel(tmm, ccm, ncm));
                    }
                });
            }
        });

        return modify;
    }


}
