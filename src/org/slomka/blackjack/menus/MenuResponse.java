package org.slomka.blackjack.menus;

/**
 * Response from a OptionMenu call
 * 
 * @author Slomka
 */
public class MenuResponse {

    private int responseNumber;
    private Option responseOption;

    public MenuResponse(int number, Option response) {
        this.responseNumber = number;
        this.responseOption = response;
    }
    
    public int getResponseNumber() {
        return responseNumber;
    }

    public void setResponseNumber(int responseNumber) {
        this.responseNumber = responseNumber;
    }

    public Option getResponseOption() {
        return responseOption;
    }

    public void setResponseOption(Option responseOption) {
        this.responseOption = responseOption;
    }
    
    
    
}
