package edu.jmu.decaf;

import java.io.*;
import java.util.*;

/**
 * Decaf compiler driver class.
 */
class DecafCompiler
{
    private File mainFile;

    /**
     * Program entry point.
     * @param args Command-line arguments
     */
    public static void main(String[] args)
    {
        try {
            (new DecafCompiler(args)).run();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidTokenException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Create a new compiler instance
     * @param args Command-line arguments
     */
    public DecafCompiler(String[] args)
    {
    	mainFile = null;
        parseCompilerArguments(args);
    }

    /**
     * Run all phases of the compiler.
     *
     * @throws IOException Thrown if there is an I/O problem
     * @throws InvalidTokenException Thrown if there is a lexing problem
     */
    public void run() throws IOException, InvalidTokenException
    {
        // PHASE 1 - LEXER

        // tokenize (Decaf source => Queue<Token>)
        DecafLexer lexer = new MyDecafLexer();
        BufferedReader input = new BufferedReader(new FileReader(mainFile));
        Queue<Token> tokens = lexer.lex(input, mainFile.getName());
        for (Token t : tokens) {
            System.out.println(t.toString());
        }
    }

    /**
     * Parse command line arguments and set member variables
     *
     * @param args Command-line arguments from {@code main()}
     */
    private void parseCompilerArguments(String[] args)
    {
        if (args.length != 1) {
            System.out.println("Usage: ./decaf.sh <file>");
            System.exit(-1);
        }

        mainFile = new File(args[0]);
    }
}

