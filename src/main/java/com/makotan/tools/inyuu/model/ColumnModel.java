package com.makotan.tools.inyuu.model;

/**
 * Created by makotan on 2015/09/19.
 */
public class ColumnModel {
    public String columnName;
    public String typeName;
    public Integer columnSize;
    public Integer decimalDigits;
    public Boolean notNull;
    public String comment;
    public String columnDef;
    public Integer sqlDataType;
    public Integer sqlDatetimeSub;
    public Integer ordinalPosition;
    public String isNullable;
    public String isAutoIncrement;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnModel that = (ColumnModel) o;

        if (columnName != null ? !columnName.equals(that.columnName) : that.columnName != null) return false;
        if (typeName != null ? !typeName.equals(that.typeName) : that.typeName != null) return false;
        if (columnSize != null ? !columnSize.equals(that.columnSize) : that.columnSize != null) return false;
        if (decimalDigits != null ? !decimalDigits.equals(that.decimalDigits) : that.decimalDigits != null)
            return false;
        if (notNull != null ? !notNull.equals(that.notNull) : that.notNull != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (columnDef != null ? !columnDef.equals(that.columnDef) : that.columnDef != null) return false;
        if (sqlDataType != null ? !sqlDataType.equals(that.sqlDataType) : that.sqlDataType != null) return false;
        if (sqlDatetimeSub != null ? !sqlDatetimeSub.equals(that.sqlDatetimeSub) : that.sqlDatetimeSub != null)
            return false;
        if (ordinalPosition != null ? !ordinalPosition.equals(that.ordinalPosition) : that.ordinalPosition != null)
            return false;
        if (isNullable != null ? !isNullable.equals(that.isNullable) : that.isNullable != null) return false;
        return !(isAutoIncrement != null ? !isAutoIncrement.equals(that.isAutoIncrement) : that.isAutoIncrement != null);

    }

    @Override
    public int hashCode() {
        int result = columnName != null ? columnName.hashCode() : 0;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (columnSize != null ? columnSize.hashCode() : 0);
        result = 31 * result + (decimalDigits != null ? decimalDigits.hashCode() : 0);
        result = 31 * result + (notNull != null ? notNull.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (columnDef != null ? columnDef.hashCode() : 0);
        result = 31 * result + (sqlDataType != null ? sqlDataType.hashCode() : 0);
        result = 31 * result + (sqlDatetimeSub != null ? sqlDatetimeSub.hashCode() : 0);
        result = 31 * result + (ordinalPosition != null ? ordinalPosition.hashCode() : 0);
        result = 31 * result + (isNullable != null ? isNullable.hashCode() : 0);
        result = 31 * result + (isAutoIncrement != null ? isAutoIncrement.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ColumnModel{" +
                "columnName='" + columnName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", columnSize=" + columnSize +
                ", decimalDigits=" + decimalDigits +
                ", notNull=" + notNull +
                ", comment='" + comment + '\'' +
                ", columnDef='" + columnDef + '\'' +
                ", sqlDataType=" + sqlDataType +
                ", sqlDatetimeSub=" + sqlDatetimeSub +
                ", ordinalPosition=" + ordinalPosition +
                ", isNullable='" + isNullable + '\'' +
                ", isAutoIncrement='" + isAutoIncrement + '\'' +
                '}';
    }
}
