package org.slomka.blackjack.menus;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slomka.blackjack.Out;
import org.slomka.blackjack.exceptions.InvalidOptionException;

/**
 *
 * @author Slomka
 */
public class OptionMenu {

    private List<Option> options = new ArrayList<Option>();
    private String message = null;

    public OptionMenu(List<Option> options, String message) {
        this.message = message;
        this.options = options;
    }

    public void removeOption(Option o) {
        options.remove(o);
    }

    /**
     * Prints the option's menu message and the options
     */
    public void printOptionMenu() {
        if (message != null) {
            Out.print(message);
        }
        for (int x = 0; x < options.size(); x++) {
            Out.print("[" + (x+1) + "] - " + options.get(x).name());
        }

    }

    /**
     * Show the menu to the player and ask the player for a response
     *
     * @return a MenuResponse with the chosen Option
     */
    public MenuResponse ask() {
        printOptionMenu();
        // if we have a test option, we force this test option
        Option testOption = OptionBuilder.nextAutorunOption();
        if (testOption != null) {
            Out.print("Autorun option found.");
            for (int x = 0; x < options.size(); x++) {
                if (options.get(x) == testOption) {
                    Out.print("You were coerced to choose " + testOption.name());
                    return new MenuResponse(x, testOption);
                }
            }
            Out.print("Could not find option "+testOption);
            throw new InvalidOptionException(testOption, this);
        }

        try {
            int inputNumber = readNumber();
            Option selected = null;
            if (options.size() > 0) {
                if (options.size() > inputNumber-1) {
                    selected = options.get(inputNumber-1);
                    Out.print("You selected " + selected.name());
                }
            }
            return new MenuResponse(inputNumber, selected);
        } catch (IOException ex) {
            Out.print("An error has ocurred while reading the inputstream.");
            ex.printStackTrace();
            throw new RuntimeException("Error while reading player's input number", ex);
        }
    }

    /**
     * Reads a integer from keyboard input.
     *
     * @return a int read
     * @throws IOException if inputstream is not found
     */
    public int readNumber() throws IOException {
        String inputString = "";
        int inputNumber = -1;
        boolean read = true;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        while (read) {
            // else we read the input to get the user option
            inputString = bufferRead.readLine();
            if (inputString.matches("[0-9]+")) {
                inputNumber = Integer.valueOf(inputString);
                if (options.size() > 0) {
                    if (inputNumber-1 >= 0 && inputNumber-1 < options.size()) {
                        read = false;
                    } else {
                        Out.print("Enter a valid option.");
                    }
                } else {
                    read = false;
                }
            } else {
                Out.print("Please enter a valid numeric value.");
            }
        }
        return inputNumber;
    }

}
