package edu.jmu.decaf;

import java.io.*;
import java.util.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for lexer
 */
public class TestLexer extends TestCase
{
    /**
     * Initialization
     *
     * @param testName name of the test case
     */
    public TestLexer(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(TestLexer.class);
    }

    /**
     * Lex a single token and verify that it has the correct type and contents
     * @param text Decaf source code
     * @param expectedType Correct token type
     * @param expectedText Correct token contents
     */
    protected static void verifySingleToken(String text,
            Token.Type expectedType, String expectedText)
    {
        try {
            Queue<Token> tokens = (new MyDecafLexer()).lex(text);
            assertTrue(tokens.size() == 1);
            assertTrue(tokens.peek().type == expectedType);
            assertTrue(tokens.peek().text.equals(expectedText));
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(false);
        }
    }
    
    protected static void testFullFunction(File file)
    {
      String[] toks;
      toks = new String[19];
      
      toks[0] = "KEY\t\"def\"\t[pa02-sample.decaf:1]";
      toks[1] = "KEY\t\"int\"\t[pa02-sample.decaf:1]";
      toks[2] = "ID \t\"main\"\t[pa02-sample.decaf:1]" ;
      toks[3] = "SYM\t\"(\"\t[pa02-sample.decaf:1]";
      toks[4] = "SYM\t\")\"\t[pa02-sample.decaf:1]";
      toks[5] = "SYM\t\"{\"\t[pa02-sample.decaf:2]";
      toks[6] = "KEY\t\"int\"\t[pa02-sample.decaf:3]";
      toks[7] = "ID \t\"a\"\t[pa02-sample.decaf:3]";
      toks[8] = "SYM\t\";\"\t[pa02-sample.decaf:3]";
      toks[9] = "ID \t\"a\"\t[pa02-sample.decaf:4]";
      toks[10] = "SYM\t\"=\"\t[pa02-sample.decaf:4]";
      toks[11] = "DEC\t\"4\"\t[pa02-sample.decaf:4]";
      toks[12] ="SYM\t\"+\"\t[pa02-sample.decaf:4]";
      toks[13] ="DEC\t\"5\"\t[pa02-sample.decaf:4]";
      toks[14] ="SYM\t\";\"\t[pa02-sample.decaf:4]";
      toks[15] ="KEY\t\"return\"\t[pa02-sample.decaf:5]";
      toks[16] ="ID \t\"a\"\t[pa02-sample.decaf:5]";
      toks[17] ="SYM\t\";\"\t[pa02-sample.decaf:5]";
      toks[18] ="SYM\t\"}\"\t[pa02-sample.decaf:6]";
      
      
      String actual = "";
      try
      {
        Queue<Token> tokens = (new MyDecafLexer()).lex(file);
        assertTrue(tokens.size() == toks.length);
        for (int i = 0; i < toks.length; i++)
        {
          assertTrue(tokens.poll().toString().equals(toks[i]));
        }
      } catch (IOException ex) {
        assertTrue(false);
      } catch (InvalidTokenException ex) {
        assertTrue(false);
      }
      
    }
    
    /**
     * Lex multiple tokens and verify that the correct number of tokens were
     * found.
     * @param text Decaf source code
     * @param expectedNumTokens Correct number of tokens
     */
    protected static void verifyMultipleTokens(String text, int expectedNumTokens)
    {
        try {
            Queue<Token> tokens = (new MyDecafLexer()).lex(text);
            assertTrue(tokens.size() == expectedNumTokens);
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(false);
        }
    }

    /**
     * Lex multiple tokens and verify that the tokens match the expected types.
     * @param text Decaf source code
     * @param expectedTypes Correct token types, in sequence
     */
    protected static void verifyMultipleTokens(String text, Token.Type expectedTypes[])
    {
        try {
            Queue<Token> tokens = (new MyDecafLexer()).lex(text);
            assertTrue(tokens.size() == expectedTypes.length);
            for (int i=0; i<expectedTypes.length; i++) {
                assertTrue(tokens.poll().type == expectedTypes[i]);
            }
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(false);
        }
    }
    
    /**
     * Make sure the lexer throws an exception for an invalid token.
     * @param text Invalid Decaf source code
     */
    protected static void verifyInvalidToken(String text)
    {
        try {
            (new MyDecafLexer()).lex(text);
            assertTrue(false);
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(true);
        }
    }

    /**
     * Make sure the lexer does not throw an exception for an empty string.
     */
    public static void testEmpty()
    {
        try {
            assertTrue((new MyDecafLexer()).lex("").size() == 0);
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(false);
        }
    }

    public void testSingleLetterID() { verifySingleToken("a",   Token.Type.ID, "a"); }
    public void testMultiLetterID()  { verifySingleToken("abc", Token.Type.ID, "abc"); }
    public void testSingleDigit()    { verifySingleToken("1",   Token.Type.DEC, "1"); }
    public void testKeyword()        { verifySingleToken("def", Token.Type.KEY, "def"); }
    public void testKeywordID()      { verifySingleToken("int3",   Token.Type.ID, "int3"); }
    public void testSymbol()         { verifySingleToken("+",      Token.Type.SYM, "+"); }
    public void testString()         { verifySingleToken("\"hi\"", Token.Type.STR, "\"hi\""); }

    public void testComments()       { verifyMultipleTokens("// test", 0); }
    public void testMultiTokens()    { verifyMultipleTokens("def foo", 2); }
    public void testInvalidToken()   { verifyInvalidToken("@"); }
    public void testFullFunction()   { testFullFunction(new File("pa02-sample.decaf")); }

}
