package com.makotan.tools.inyuu.model.diff;

import com.makotan.tools.inyuu.model.RenameModel;
import com.makotan.tools.inyuu.model.TableModel;

/**
 * Created by makotan on 2015/09/20.
 */
public class TableMappingModel {
    public TableModel currentTableModel;
    public TableModel nextTableModel;
    public RenameModel renameModel;

    public TableMappingModel(TableModel currentTableModel,TableModel nextTableModel,RenameModel renameModel) {
        this.currentTableModel = currentTableModel;
        this.nextTableModel = nextTableModel;
        this.renameModel = renameModel;
    }

    public TableMappingModel(TableModel currentTableModel,TableModel nextTableModel) {
        this(currentTableModel,nextTableModel,null);
    }

    public boolean isRename() {
        return renameModel != null;
    }
    public boolean isDrop() {
        return nextTableModel == null;
    }
    public boolean isNoAction() {
        if (nextTableModel == null) {
            return false;
        }
        return currentTableModel.tableName.equalsIgnoreCase(nextTableModel.tableName);
    }
}
