package com.makotan.tools.inyuu.provider.jdbc.h2;

import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import com.makotan.tools.inyuu.provider.jdbc.BaseJdbcProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by makotan on 2015/09/22.
 */
public class H2JdbcProvider extends BaseJdbcProvider {
    Logger logger = LoggerFactory.getLogger(H2JdbcProvider.class);


    public H2JdbcProvider() {
        super();
    }

    public H2JdbcProvider(InyuuDBInfo dbInfo) {
        super(dbInfo);
    }


}
