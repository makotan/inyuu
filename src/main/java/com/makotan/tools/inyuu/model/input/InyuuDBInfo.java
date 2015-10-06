package com.makotan.tools.inyuu.model.input;

import org.flywaydb.core.Flyway;

/**
 * Created by makotan on 2015/09/21.
 */
public class InyuuDBInfo {
    public String driverClassName;
    public String url;
    public String user;
    public String password;

    public void hoge() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(url,user,password);
        flyway.setLocations("","");
        flyway.clean();
        flyway.migrate();

    }
}
