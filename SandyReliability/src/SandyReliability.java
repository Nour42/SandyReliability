
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*;
import java.util.HashMap;

public class SandyReliability {

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Welcome \n" +
                "To end the program please type \"end\" then press enter");

        File circuitFile = new File("filename.txt");

        int lineCnt = tryCountingLines(circuitFile, 0);    //trying to open the file and counting the number of lines in file

        Scanner formulaFile = new Scanner(circuitFile).useDelimiter(";\\s*");

        Set<String> rawInputs = new HashSet<>();
        Set<String> finalOutput = new HashSet<>();
        HashMap<String, String> formulas = new HashMap<>();
        String[] inputOrder = new String[lineCnt];           //keeping track of the inputOrder (using inputCounter)
        int inputCounter = 0;
        //      formula example                          corresponding variable names
        // Example: c = or(A, b);  <--decomposed:  (c == opOutput), (or == operation), ({A,b} == opInput[])
        while(formulaFile.hasNext()) {                                      //this loop translates circuit to formulas

            String[] equalSplit = formulaFile.next().split("=\\s*");      //split the formula at the equal sign
            String opOutput = removeWhiteSpaces(equalSplit[0]);                 //name of operation output
            inputOrder[inputCounter] = opOutput;                                //record input order
            inputCounter++;
            char operation = translateChar(equalSplit[1].charAt(0));               // (or == |), (and == &), (not == !)
            if (isCapital(opOutput.charAt(0))) finalOutput.add(opOutput);                 //record final output
            String elements = operation + "," + removeBrackets(equalSplit[1]);
            formulas.put(opOutput, elements);
        }
        formulaFile.close();

//            System.out.println("ALL: " + formulas);
        System.out.println("The circuit has been successfully decomposed");
        //once circuit is defined computation processing the input can be carried out


        // System.out.print("Place your binary circuit input: ");
        Scanner bs = new Scanner(System.in);
        String binInput = "uninitiated";

        while (binInput != "end"){                   //while we have an unsolved input
            System.out.print("Type your binary circuit input or \"end\", then press enter: ");
            binInput = bs.next();                 //take new circuit input in binary form

            if(removeWhiteSpaces(binInput).equals("end")){
                break;
            }

            int nextBinaryInputCount = 0;               //used for translating allocating raw input to binary circuit input
            String[] rawInputOrder = new String[binInput.length()];

            HashMap<String, Character> truthBook = new HashMap<>();  // in the form: (wireName, trueOrFalse) {(A,F), (b, T), (c, T)}

            int formulaNr;
            for (formulaNr = 0; formulaNr < lineCnt; formulaNr++){

                String[] wire = formulas.get(inputOrder[formulaNr]).split("\\s*,\\s*");                 //big loop
                char truthValue = '?';
                char loOperation = 'z';
                char wireTruth;

                for(int j = 0; j < wire.length; j++){         //solves boolean by subbing in truths and records in book

                    if (j==0){                      //wire[j==0] is the operation for the loops formula term
                        loOperation = wire[j].charAt(0);            //operation is named loOperation
                        if (wire.length == 1){                  //if there is only a single operation in this formula term
                            truthValue = computeTermlessFormula(wire[0].charAt(0));       //example; c = or()  results in 'F'
                        }
                    } else {
                        if (isCapital(wire[j].charAt(0))) {                      //if input term is capital letter (its raw)
                            if (!rawInputs.contains(wire[j])) {
                                truthBook.put(wire[j], translateChar(binInput.charAt(nextBinaryInputCount)) );            //record the found rawInput
                                rawInputs.add(wire[j]);
                                rawInputOrder[nextBinaryInputCount] = wire[j];
                                nextBinaryInputCount++;
                            }
                        }

                        wireTruth = truthBook.get(wire[j]);


                        if (truthValue == '?'){                                      //if truthValue is not initialized
                            if (loOperation == '!'){
                                truthValue = opposeChar(wireTruth);
                            } else {
                                truthValue = truthBook.get(wire[j]);            //if its & or |
                            }
                        } else {
                            truthValue = solveBoolean(loOperation, truthValue, truthBook.get(wire[j]));
                        }
                    }
                }
                truthBook.put(inputOrder[formulaNr], truthValue);               //big loop
            }

            for (String f : finalOutput) {                     //Printing Outputs
                System.out.println("Output: " + f + " = " + truthBook.get(f));
            }
            System.out.println("Based on:");
            for(int i = 0; i < binInput.length(); i++) {              //manual for loop so that order is preserved
                System.out.println("Input: " + rawInputOrder[i] + " = " + truthBook.get(rawInputOrder[i]));
            }
//                System.out.println("truthBook: " + truthBook);
//                System.out.println("Binary circuit input: " + binInput);

            truthBook.clear();              // prepare for new input
            rawInputs.clear();
        }



        System.out.print("Thank you for using Nours circuit solver, good bye.");

    }

//=======================================Methods=Built=================================================================


    static boolean isCapital(char letter){
        return Character.isUpperCase(letter);
    }

    static int tryCountingLines(File myFile, int lineCnt){
        try {
            Scanner lineCounting = new Scanner(myFile).useDelimiter(";");
            while (lineCounting.hasNext()) {
                lineCounting.next();
                lineCnt++;
            }
            lineCounting.close();
        } catch (FileNotFoundException e) {
            System.out.println("A file reading error occurred.");
            e.printStackTrace();
        }
        return lineCnt;
    }

    static char solveBoolean(char operation, char boo1, char boo2){
        if ( boo1 == 'X' || boo1 == 'x' || boo2 == 'x' || boo2 == 'X')
        {
            return 'X';
        } else {
            switch (operation) {
                case '|':
                    if (boo1 == 't' | boo1 == 'T' | boo2 == 't' | boo2 == 'T'){
                        return 'T';
                    } else {
                        return 'F';
                    }
                case '&':
                    if (boo1 == 'f' | boo1 == 'F' | boo2 == 'F' | boo2 == 'f'){
                        return 'F';
                    } else {
                        return 'T';
                    }
                case '!':
                    throw new java.lang.IllegalArgumentException("shouldn't have reached here because truthValue is initialized");
                default:
                    throw new java.lang.IllegalArgumentException("Invalid operation in solveBoolean: " + operation);
            }
        }

    }




    static String removeWhiteSpaces(String s)             //remove whitespaces
    {
        String s2 = s.replaceAll("\\s","");
        return s2;
    }

    static String removeBrackets(String s)             //remove whitespaces
    {
        s = s.substring(s.indexOf("(")+1);       //Cleaning string opInput[0] eg. "or(A"
        s = s.trim();                                                  //dropping string before "("
        s = s.substring(0, s.indexOf(")"));
        s = s.trim();
        return removeWhiteSpaces(s);
    }

    static char computeTermlessFormula(char operation){
        return switch (operation) {
            case '|' -> 'F';
            case '&' -> 'T';
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }

    static char opposeChar(char c){         //returns negation of char, returns x if c==x
        return switch (c) {
            case 'X', 'x' -> 'X';
            case 'F', 'f' -> 'T';
            case 'T', 't' -> 'F';
            default -> throw new IllegalArgumentException("Invalid truth in oppose char: " + c);
        };
    }

    static char translateChar(char c)
    {
        return switch (c) {
            case 'X', 'x' -> 'X';
            case '1' -> 'T';
            case '0' -> 'F';
            case 'o' -> '|';        //o for or
            case 'a' -> '&';        //a for and
            case 'n' -> '!';        //n for not
            default -> throw new IllegalArgumentException("Invalid character: " + c +
                                             " can't be converted into X, &, !, |, true or false!");
        };
    }
}

