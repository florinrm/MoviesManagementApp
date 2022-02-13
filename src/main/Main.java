package main;

import exceptions.IncorrectFormatException;
import org.json.simple.parser.ParseException;
import parsing.InputParsing;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, IncorrectFormatException {
        InputParsing.parse(args[0]);
    }
}
