package com.makotan.tools.inyuu.model.diff;

import com.makotan.tools.inyuu.model.ColumnModel;

/**
 * Created by makotan on 2015/09/20.
 */
public class TableColumnMappingModel {
    public TableMappingModel tableMappingModel;
    public ColumnModel currentColumn;
    public ColumnModel nextCoulmn;

    public TableColumnMappingModel(TableMappingModel tableMappingModel , ColumnModel currentColumn, ColumnModel nextCoulmn) {
        this.tableMappingModel = tableMappingModel;
        this.currentColumn = currentColumn;
        this.nextCoulmn = nextCoulmn;
    }
}
