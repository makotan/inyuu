package com.makotan.tools.inyuu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makotan on 2015/09/26.
 */
public class MetadataModel {
    public List<TableModel> tableModels = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetadataModel that = (MetadataModel) o;

        return !(tableModels != null ? !tableModels.equals(that.tableModels) : that.tableModels != null);

    }

    @Override
    public int hashCode() {
        return tableModels != null ? tableModels.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MetadataModel{" +
                "tableModels=" + tableModels +
                '}';
    }
}
