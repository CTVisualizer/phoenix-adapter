package com.proofpoint.ctvisualizer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.proofpoint.ctvisualizer.executequery.QueryExecutionManager;
import com.proofpoint.ctvisualizer.executequery.ResultSetConverterModule;


import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Path phoenixClient = Paths.get(".","lib", "phoenix-client.jar");

        Injector injector = Guice.createInjector(new ResultSetConverterModule());

        PhoenixClientLoader loader = injector.getInstance(PhoenixClientLoader.class);
        QueryExecutionManager executionManager = injector.getInstance(QueryExecutionManager.class);

        loader.loadPhoenixClient(phoenixClient);

        port(8080);
        get("/execute/:query", (request, response) -> {
            response.type("application/json");
            return executionManager.executeQuery(request.params("query"));
        });
        get("/hello",(req, res) -> "Hello World");
    }

}
