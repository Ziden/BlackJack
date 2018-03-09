package org.slomka.blackjack.menus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Construct a OptionMenu
 * 
 * @author Slomka
 */
public class OptionBuilder {

    // options that will run automatically
    private static List<Option> auorunOptions = new ArrayList<Option>();
    
    // possible options
    private List<Option> options = new ArrayList<Option>();

    // message before asking
    private String message = null;
    
    /**
     * Gets the next text option and pop the list.
     * @return the next option or null if there is none
     */
    public static Option nextAutorunOption() {
        if(auorunOptions.size() > 0) {
            return auorunOptions.remove(0);
        }
        return null;
    }
    
    /**
     * Sets a list of autorun options. The player will choose this options
     * automatically.
     * @param opt the options you want to autorun this round, in order
     */
    public static void setAutorunOptions(Option ... opt) {
        auorunOptions.clear();
        for(Option o : opt)
            auorunOptions.add(o);
    }
    
    public OptionBuilder message(String message) {
        this.message = message;
        return this;
    }
    
    public OptionBuilder option(Option opt) {
        this.options.add(opt);
        return this;
    }
    
    public OptionMenu build() {
        return new OptionMenu(options,message);
    }
    
    public OptionBuilder removeOption(Option o) {
        this.options.remove(o);
        return this;
    }
    
}
