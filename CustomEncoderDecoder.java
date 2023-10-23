// Created By: Chu Zer-Min

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class CustomEncoderDecoder {
    // stores the encoded text here
    private static String encodedText = "";

    // character table used for encoding and decoding
    private static final char[] CHARACTER_TABLE = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '(', ')', '*', '+',
            ',', '-', '.', '/'
    };

    // make a copy of the character table for reference
    private static final char[] MAIN_TABLE = CHARACTER_TABLE.clone();

    // to store the offset characters
    private static char offsetCharacter;
    // to store the indexes
    private static int offsetIndex;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Offset Character: ");
        char offsetChar = scanner.next().charAt(0);

        // set the offset character and index
        setOffset(offsetChar);

        // instance object of the encoder and decoder
        CustomEncoderDecoder encoderDecoder = new CustomEncoderDecoder();

        System.out.println("Enter the string to encode: ");
        scanner.nextLine();
        String inputString = scanner.nextLine();

        String encodedResult = encoderDecoder.encode(inputString);
        System.out.println("Encoded String: " + encodedResult);

        String decodedResult = encoderDecoder.decode(encodedResult);
        System.out.println("Decoded String: " + decodedResult);
    }

    // method to set the offset character and index
    public static void setOffset(char offsetChar) {
        for (int i = 0; i < CHARACTER_TABLE.length; i++) {
            if (offsetChar == CHARACTER_TABLE[i]) {
                offsetIndex = i;
                offsetCharacter = CHARACTER_TABLE[i];
                return;
            }
        }
        System.out.println("Invalid Offset Character.");
        System.exit(1);
    }

    // to decode an encoded string given by user
    public String decode(String encodedText) {
        char[] encodedArray = encodedText.toCharArray();
        String decodedResult = "";
        boolean offsetApplied = false;

        // array to store the positions of the characters
        int[] position = new int[encodedText.length()];
        int found_Pos = 0;

        for (char encodedChar : encodedArray) {
            if (!offsetApplied && encodedChar == offsetCharacter) {
                offsetApplied = true;
                continue;
            }
            found_Pos = 0;

            // look for the character in the character table and iterate it
            for (int k = 0; k < CHARACTER_TABLE.length; k++) {
                if (encodedChar == CHARACTER_TABLE[k]) {
                    found_Pos = 1;
                    // append the deoced character to the result
                    decodedResult += CHARACTER_TABLE[k + offsetIndex];
                    break;
                }

                if (encodedChar == ' ') {
                    decodedResult += " ";
                    break;
                }
            }

            if (found_Pos == 0) {
                // character not found in the CHARACTER_TABLE, preserve as is
                decodedResult += encodedChar;
            }
        }

        return decodedResult;
    }

    public String encode(String plainText) {
        char[] charsInput = plainText.toCharArray();
        int countSpacing = 0; // to keep track of spaces

        for (char ch : charsInput) {
            if (ch == ' ') {
                countSpacing++;
            }
        }
        // to store the encoded result and add the offset char at the beginning of the
        // text
        StringBuilder encodedResult = new StringBuilder();
        encodedResult.append(offsetCharacter);

        // rotate the CHARACTER_TABLE based on the offset
        for (int j = 0; j < offsetIndex; j++) {
            // store the last char in CHARACTER_TABLE (TEMPORARILY)
            char temp = CHARACTER_TABLE[CHARACTER_TABLE.length - 1];
            // shift each of the character to one position to the right
            for (int i = CHARACTER_TABLE.length - 2; i >= 0; i--) {
                CHARACTER_TABLE[i + 1] = CHARACTER_TABLE[i];
            }

            // set the first char in CHARACTER_TABLE to the TEMPORARILY stored char
            CHARACTER_TABLE[0] = temp;
        }
        // array to store the positions of characters in the input text
        int[] position = new int[charsInput.length];
        int pos = 0; // initialize a counter to keep track of the positions
        int found_Pos = 0; // flag is used to track if a character was found

        // map method to look up the indices of the character in the MAIN_TABLE
        Map<Character, Integer> charToIndexMap = new HashMap<>();
        for (int i = 0; i < MAIN_TABLE.length; i++) {
            charToIndexMap.put(MAIN_TABLE[i], i);
        }
        // loop thorugh chars in the input text
        for (char inputChar : charsInput) {
            int index;
            if (inputChar == ' ') {
                // error handling: if the char is a space, set its index to 0
                index = 0;
            } else {
                // checks if the char is in MAIN_TABLE, if it is then it's index from the map
                // if not found in map, ue the char's ASCII value as it's index
                // get the index from the existing map, if not employ ASCII value
                index = charToIndexMap.getOrDefault(inputChar, (int) inputChar);
            }
            // store the calulcated index in the array and increase it once everytime it's
            // successfully stored
            position[pos] = index;
            pos++;
        }
        // iterate and build the encoded result
        // loop through the positions of characters in the input text
        for (int i = 0; i < position.length; i++) {
            char currentChar = ' '; // for preservation of spaces
            if (position[i] == 0) {
                currentChar = ' ';
            } else if (position[i] >= 0 && position[i] <= 43) {
                // if the character is in range from the table (0 -> 43)
                // to use CHARACTER_TABLE for encoding
                currentChar = CHARACTER_TABLE[position[i]];
            } else if ((position[i] >= 97 && position[i] <= 122) || (position[i] >= 58 && position[i] <= 126)) {
                // if the character are outside the table range but withing printable ASCII
                // char:
                // preserve ASCII char between 97 and 122 (a -> z)
                // preserve ASCII char between 58 and 126 (special chars e.g. / * )
                // preserve characters outside the CHARACTER_TABLE range
                // char is preserved as it is by converting the integer pos back to a char
                currentChar = (char) position[i];
            }
            encodedResult.append(currentChar);
        }

        // store the encoded result in encodedText
        encodedText += encodedResult;
        return encodedResult.toString();
    }
}
