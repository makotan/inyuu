package com.makotan.tools.inyuu.cli;

import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.util.Either;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by makotan on 2015/09/19.
 */
public class CliOptions {
    @Option(name = "-test", usage = "env check")
    public boolean test = false;

    @Option(name = "-dir", usage = "base dir(inyuu.yaml,etc...)")
    public String baseDir = ".";

    @Option(name = "-conf", usage = "config yaml file name(default inyuu.yaml)")
    public String configFileName = "inyuu.yaml";


    public static Either<InyuuException,CliOptions> parseArgument(String ... args) {
        CliOptions options = new CliOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
            return Either.right(options);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            return Either.left(new InyuuException(e));
        }
    }
}
