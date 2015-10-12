package com.makotan.tools.inyuu.model;

import java.util.List;

/**
 * Created by makotan on 2015/09/19.
 */
public class TableModel {
    public String tableSchema;
    public String tableName;
    public String comment;
    public List<ColumnModel> fieldModes;
    public List<IndexModel> indexModels;
    public ExtendProperties extendProperties;

    @Override
    public String toString() {
        return "TableModel{" +
                "tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", comment='" + comment + '\'' +
                ", fieldModes=" + fieldModes +
                ", indexModels=" + indexModels +
                ", extendProperties=" + extendProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableModel that = (TableModel) o;

        if (tableSchema != null ? !tableSchema.equals(that.tableSchema) : that.tableSchema != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (fieldModes != null ? !fieldModes.equals(that.fieldModes) : that.fieldModes != null) return false;
        if (indexModels != null ? !indexModels.equals(that.indexModels) : that.indexModels != null) return false;
        return !(extendProperties != null ? !extendProperties.equals(that.extendProperties) : that.extendProperties != null);

    }

    public boolean equalsSelfFields(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableModel that = (TableModel) o;

        if (tableSchema != null ? !tableSchema.equals(that.tableSchema) : that.tableSchema != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        return !(extendProperties != null ? !extendProperties.equals(that.extendProperties) : that.extendProperties != null);

    }

    @Override
    public int hashCode() {
        int result = tableSchema != null ? tableSchema.hashCode() : 0;
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (fieldModes != null ? fieldModes.hashCode() : 0);
        result = 31 * result + (indexModels != null ? indexModels.hashCode() : 0);
        result = 31 * result + (extendProperties != null ? extendProperties.hashCode() : 0);
        return result;
    }
}
