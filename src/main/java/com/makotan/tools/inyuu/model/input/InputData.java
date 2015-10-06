package com.makotan.tools.inyuu.model.input;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by makotan on 2015/09/20.
 */
public class InputData {
    public CliOptions options;
    public InyuuConfig config;
    public MetadataModel currentModel;
    public MetadataModel nextModel;
    public List<RenameModel> renameModelList = new ArrayList<>();
    public List<RenameFieldModel> renameFieldModels = new ArrayList<>();
}
