package org.slomka.blackjack.exceptions;

import org.slomka.blackjack.menus.Option;
import org.slomka.blackjack.menus.OptionMenu;

/**
 *
 * @author Slomka
 */
public class InvalidOptionException extends RuntimeException {

    private Option option;
    private OptionMenu menu;

    public InvalidOptionException(Option o, OptionMenu om) {
        this.option = o;
        this.menu = om;
    }
    
    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public OptionMenu getMenu() {
        return menu;
    }

    public void setMenu(OptionMenu menu) {
        this.menu = menu;
    }
    
    
}
