import org.junit.Test;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

import static org.junit.Assert.*;

public class SandyReliabilityTest {

    protected SandyReliability instance;

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionIsThrown(){
        checkOpposeChar("opposeCharEx", '/', '/');
        checkTranslateChar("translateCharEx", '/', '/');
        checkCapital("isCapitalEx", '/', false);
        checkTermlessFormula("computeTermlessFormulaEx", '/', '/');
        checkSolveBoolean("SolveBoolean1", '&', 't', 'T', 'T');
    }

    public void checkCapital(String msg, char input, boolean expectedResult){
        boolean result = instance.isCapital(input);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void isCapital() {
        checkCapital("isCapital_A",'A',true);
        checkCapital("isCapital_Z",'Z',true);
        checkCapital("isCapital_d",'d',false);
        checkCapital("isCapital_i",'i',false);
    }

    public void checkFile(String msg, String input, int expectedResult){

        File myFile = new File("filenameOut.txt");
        try {
            if (!myFile.createNewFile()) {
                System.out.println("File already exists.");
            }

            FileWriter myWriter = new FileWriter("filenameOut.txt");
            myWriter.write(input);
            myWriter.close();

            int result = instance.tryCountingLines(myFile, 0);

            if (!myFile.delete()) {
                System.out.println("Failed to delete the file.");
            }

            assertEquals(msg+"Test", expectedResult, result);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    @Test
    public void tryCountingLines() {                //counting lines that are seperated by ";"'s
        checkFile("CountingLines ", "", 0);
        checkFile("CountingLines ", "one;  ", 2);
        checkFile("CountingLines ", "one;  ", 2);
        checkFile("CountingLines ", "one; two; three ", 3);
        checkFile("CountingLines ", "one; two; three ; four", 4);
    }

    public void checkSolveBoolean(String msg, char operation, char bool1, char bool2, char expectedResult){
        char result = instance.solveBoolean(operation, bool1, bool2);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void solveBoolean() {
        checkSolveBoolean("SolveBoolean1", '&', 't', 'T', 'T');
        checkSolveBoolean("SolveBoolean1", '&', 'T', 'F', 'F');
        checkSolveBoolean("SolveBoolean1", '&', 'F', 'T', 'F');
        checkSolveBoolean("SolveBoolean1", '&', 'F', 'F', 'F');
        checkSolveBoolean("SolveBoolean1", '|', 'F', 'F', 'F');
        checkSolveBoolean("SolveBoolean1", '|', 'F', 'T', 'T');
        checkSolveBoolean("SolveBoolean1", '|', 'T', 'F', 'T');
        checkSolveBoolean("SolveBoolean1", '|', 'T', 'T', 'T');
        checkSolveBoolean("SolveBoolean1", '|', 'x', 'x', 'X');
        checkSolveBoolean("SolveBoolean1", '&', 'X', 'F', 'X');
    }

    public void checkSpaces(String msg, String input, String expectedResult){
        String result = instance.removeWhiteSpaces(input);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void removeWhiteSpaces() {
        checkSpaces("checkSpaces", "H", "H");
        checkSpaces("checkSpaces", "Hello ", "Hello");
        checkSpaces("checkSpaces", " Hello ", "Hello");
        checkSpaces("checkSpaces", " Hello", "Hello");
        checkSpaces("checkSpaces", " Hello World ", "HelloWorld");
        checkSpaces("checkSpaces", " Hello     World ", "HelloWorld");
    }


//      I apologize as I was unable to succesfully implement a test that checks the removal of brackets
//    public void checkRemoveBrackets(String msg, String input, String expectedResult){
//        String result = instance.removeBrackets(input);
//        assertEquals(msg+"Test", expectedResult, result);
//    }
//
//    @Test
//    public void removeBrackets() {
//        checkRemoveBrackets("removeBrackets", "Hello World", "HelloWorld");
////        checkRemoveBrackets("removeBrackets)", "Hello World)", "HelloWorld");
////        checkRemoveBrackets("removeBrackets(", "(Hello World", "HelloWorld");
////        checkRemoveBrackets("removeBrackets()", "(Hello World)", "HelloWorld");
//
//    }

    private void checkTermlessFormula(String msg, char input, char expectedResult) {
        char result = instance.computeTermlessFormula(input);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void computeTermlessFormula() {
        checkTermlessFormula("computeTermlessFormula_|", '|', 'F');
        checkTermlessFormula("computeTermlessFormula_&", '&', 'T');
    }

    private void checkOpposeChar(String msg, char input, char expectedResult) {
        char result = instance.opposeChar(input);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void opposeChar() {
        checkOpposeChar("opposeCharx", 'x', 'X');
        checkOpposeChar("opposeCharX", 'X', 'X');
        checkOpposeChar("opposeCharF", 'F', 'T');
        checkOpposeChar("opposeCharf", 'f', 'T');
        checkOpposeChar("opposeChart", 't', 'F');
        checkOpposeChar("opposeCharT", 'T', 'F');
    }

    private void checkTranslateChar(String msg, char input, char expectedResult) {
        char result = instance.translateChar(input);
        assertEquals(msg+"Test", expectedResult, result);
    }

    @Test
    public void translateChar() {
        checkTranslateChar("binToCharx", 'x', 'X');
        checkTranslateChar("binToCharX", 'X', 'X');
        checkTranslateChar("binToChar0", '0', 'F');
        checkTranslateChar("binToChar1", '1', 'T');
        checkTranslateChar("binToChar_O", 'o', '|');
        checkTranslateChar("binToChar_A", 'a', '&');
        checkTranslateChar("binToChar_N", 'n', '!');
    }
}