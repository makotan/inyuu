package com.makotan.tools.inyuu.model.diff;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.model.TableModel;

import java.util.List;

/**
 * Created by makotan on 2015/09/20.
 */
public class DiffModel {
    public CliOptions options;
    public List<TableModel> createTableModels;
    public List<TableModel> dropTableModels;
    public List<TableMappingModel> tableMappingModels;
    public List<TableFieldMappingModel> tableFieldMappingModels;
}
