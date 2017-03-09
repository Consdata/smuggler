package pl.consdata.security.smuggler;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Smuggler {

    private static final String TEXT = "text";

    private static final String INPUT = "input";

    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption("i", INPUT, true, "input file");
        options.addOption("t", TEXT, true, "text to smuggle");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        String inputFile = commandLine.getOptionValue(INPUT);
        String text = commandLine.getOptionValue(TEXT);

        byte[] output = new BitFlippingAttack().attack(inputFile, text);
        Files.write(Paths.get(inputFile), output);
    }
}
