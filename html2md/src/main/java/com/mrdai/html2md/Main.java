package com.mrdai.html2md;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

/**
 * The main class, containing the PSVM method.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1 || args[0].trim().equals("-h"))
            printUsage();

        File input = new File(args[0]);
        Document doc = null;
        try {
            doc = Jsoup.parse(input, "UTF-8", "");
        } catch (IOException e) {
            // TODO handle the exception
            System.exit(1);
        }
    }

    /** Print the usage information in console */
    private static void printUsage() {

    }

}
