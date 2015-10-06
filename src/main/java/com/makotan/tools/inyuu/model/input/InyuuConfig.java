package com.makotan.tools.inyuu.model.input;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by makotan on 2015/09/21.
 */
public class InyuuConfig implements Serializable {

    public String baseDir;
    public String flywayDir;
    public String fixRunDir;

    public Map<String,InyuuDBInfo> jdbc = new HashMap<>();
    public Map<String,DumpInfo> dump = new HashMap<>();

}
