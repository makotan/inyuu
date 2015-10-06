package com.makotan.tools.inyuu.provider;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.input.InputData;
import com.makotan.tools.inyuu.model.input.InyuuConfig;
import com.makotan.tools.inyuu.util.Either;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by makotan on 2015/09/21.
 */
public class ConfigLoad {
    public Either<InyuuException,InyuuConfig> load(InputData data) {
        try {
            Yaml yaml = new Yaml();
            Path configPath = Paths.get(data.options.baseDir, data.options.configFileName);
            InyuuConfig config = yaml.loadAs(Files.newBufferedReader(configPath), InyuuConfig.class);
            return Either.right(config);
        } catch (IOException e) {
            return Either.left(new InyuuException(e));
        }
    }
}
