package com.makotan.tools.inyuu.provider.jdbc;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.model.input.InyuuDBInfo;
import com.makotan.tools.inyuu.provider.jdbc.h2.H2JdbcProvider;

/**
 * Created by makotan on 2015/09/22.
 */
public class JdbcProviderFactory {
    public static JdbcProvider getProvider(InyuuDBInfo dbInfo) {
        if (dbInfo.url.startsWith("jdbc:h2:")) {
            return new H2JdbcProvider(dbInfo);
        }
        throw new InyuuException("unknown url type " + dbInfo.url);
    }
}
