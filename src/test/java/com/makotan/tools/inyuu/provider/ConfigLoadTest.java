package com.makotan.tools.inyuu.provider;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.input.InputData;
import com.makotan.tools.inyuu.model.input.InyuuConfig;
import com.makotan.tools.inyuu.util.Either;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by makotan on 2015/09/22.
 */

public class ConfigLoadTest {
    @Test
    public void load() {
        ConfigLoad load = new ConfigLoad();
        InputData data = new InputData();
        data.options = new CliOptions();
        data.options.baseDir = "SampleData";
        data.options.configFileName = "inyuu.yaml";
        Either<InyuuException, InyuuConfig> configEither = load.load(data);
        assertTrue(configEither.isRight());
        if (configEither.isLeft()) {
            configEither.getLeft().printStackTrace();
        } else {
            InyuuConfig config = configEither.getRight();
            assertThat("SampleData" , is(config.baseDir) );
        }
    }
}
