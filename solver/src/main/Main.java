/**
 *
 */
package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.GreedyInsertionHeuristic;
import common.Driver;
import common.Order;
import common.RouteCostFunction;
import exceptions.UnservicableOrderException;
import input.CsvInputDataConsumer;
import input.InputDataConsumer;
import input.Instance;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import exceptions.InvalidInputException;
import exceptions.NoSolutionException;
import output.Route;
import output.Solution;

/**
 * @author gokhanceyhan
 *
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, InvalidInputException, NoSolutionException {

        System.out.println("Started...");

        /* Create argument options */
        Options options = createArgumentOptions();

        /* Parse arguments */
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("pdptw-solver", options);
            System.exit(1);
            return;
        }
        String inputPath = cmd.getOptionValue("path");
        String driverFileName = cmd.getOptionValue("driver-file-name");
        String orderFileName = cmd.getOptionValue("order-file-name");

        /* Read input data */
        Instance instance = createInstance(inputPath, driverFileName, orderFileName);

        /* Create the cost function */
        RouteCostFunction routeCostFunction = new RouteCostFunction(
                1.0, 1000000, 1000,
                1.0
        );

        /* Create a simple solution by the greedy insertion heuristic */
        try {
            Solution solution = new GreedyInsertionHeuristic(routeCostFunction).run(instance);
            System.out.println("Found a solution!");
        } catch (UnservicableOrderException e) {
            e.printStackTrace();
        }


        System.out.println("Done!");
    }

    /**
     * Creates the options to be specified in the argument list of the main function
     *
     * @return <code>Options </code> object.
     */
    private static Options createArgumentOptions() {

        Options options = new Options();

        Option path = new Option("p", "path", true, "input folder path");
        path.setRequired(true);
        options.addOption(path);

        Option driverFile = new Option(
                "d", "driver-file-name", true, "driver data file name");
        driverFile.setRequired(true);
        options.addOption(driverFile);

        Option orderFile = new Option(
                "o", "order-file-name", true, "order data file name");
        orderFile.setRequired(true);
        options.addOption(orderFile);

        return options;
    }

    /**
     *
     * @param inputPath
     * @param driverFileName
     * @param orderFileName
     * @return <code>Instance </code> object
     * @throws InvalidInputException
     */
    private static Instance createInstance(String inputPath, String driverFileName, String orderFileName) throws
            InvalidInputException {

        List<Driver> drivers;
        List<Order> orders;

        InputDataConsumer inputDataConsumer = new CsvInputDataConsumer();
        try {
            drivers = inputDataConsumer.fetchDrivers(inputPath + driverFileName);
        } catch (IOException e) {
            throw new InvalidInputException(String.format("Failed to fetch the drivers, details: %s", e.getMessage()));
        }
        try {
            orders = inputDataConsumer.fetchOrders(inputPath + orderFileName);
        } catch (IOException e) {
            throw new InvalidInputException(String.format("Failed to fetch the orders, details: %s", e.getMessage()));
        }
        return new Instance(drivers, orders);
    }

}
