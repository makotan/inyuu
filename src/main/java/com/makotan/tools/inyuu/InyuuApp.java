package com.makotan.tools.inyuu;

import com.makotan.tools.inyuu.cli.CliOptions;
import com.makotan.tools.inyuu.exception.InyuuException;
import com.makotan.tools.inyuu.util.Either;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by makotan on 2015/09/19.
 */
@SpringBootApplication
public class InyuuApp  implements CommandLineRunner {

    @Override
    public void run(String... args) {
        Either<InyuuException, CliOptions> cliOptionsEither = CliOptions.parseArgument(args);
        cliOptionsEither.flatMap(op -> {
            System.out.println(op);
            return Either.right("step1");
        }).flatMap(str -> {
            System.out.println(str);
            return Either.right(100);
        });
    }

    public static void main(String[] args) throws Exception {
        Either<InyuuException, CliOptions> cliOptionsEither = CliOptions.parseArgument(args);
        if (cliOptionsEither.isLeft()) {
            return;
        }
        CliOptions cliOptions = cliOptionsEither.getRight();

        SpringApplication application = new SpringApplication(InyuuApp.class);
        application.setApplicationContextClass(AnnotationConfigApplicationContext.class);
        SpringApplication.run(InyuuApp.class, args);
    }

}
