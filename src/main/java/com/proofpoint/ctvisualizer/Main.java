package com.proofpoint.ctvisualizer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proofpoint.ctvisualizer.cliparser.CLIParserModule;
import com.proofpoint.ctvisualizer.executequery.QueryExecutionManager;
import com.proofpoint.ctvisualizer.executequery.ResultSetConverterModule;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new ResultSetConverterModule(), new CLIParserModule(args));

        PhoenixClientLoader loader = injector.getInstance(PhoenixClientLoader.class);
        loader.loadPhoenixClient();

        QueryExecutionManager executionManager = injector.getInstance(QueryExecutionManager.class);

        port(8080);
        get("/execute/:query", (request, response) -> executionManager.execute(request.params("query"), response));
        get("/health", (request, response) -> executionManager.health());
        delete("/stop", (request, response) -> executionManager.stop());
        delete("/kill", (request, response) -> {
            stop();
            return "Stopped";
        });
    }

}
