package org.slomka.blackjack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.slomka.blackjack.Out;

/**
 *
 * @author Slomka
 *
 */
public class Config {

    private Properties prop = new Properties();

    private static final String MIN_BET_KEY = "minbet";
    private static final String MAX_BET_KEY = "maxbet";
    private static final String START_CASH_KEY = "startcash";
    private static final String FIVECC_KEY = "fivecc";
    private static final String SWEDISH_KEY = "swedish";
    private static final String LATESURRENDER_KEY = "latesurrender";

    public Config() {

        OutputStream output = null;
        try {
            File config = new File("config.properties");

            if (!config.exists()) {
                Out.print("Creating configuration file");
                output = new FileOutputStream("config.properties");
                Out.print("Configuration created in " + config.getAbsolutePath());
                setDefaults();
                prop.store(output, null);
            }
            prop.load(new FileInputStream(config));

            try {
                Integer.valueOf(prop.getProperty(MIN_BET_KEY));
                Integer.valueOf(prop.getProperty(MAX_BET_KEY));
                Integer.valueOf(prop.getProperty(START_CASH_KEY));
                Boolean.valueOf(prop.getProperty(FIVECC_KEY));
                Boolean.valueOf(prop.getProperty(SWEDISH_KEY));
                Boolean.valueOf(prop.getProperty(LATESURRENDER_KEY));
            } catch (NumberFormatException e) {
                setDefaults();
                Out.print("Configuration was malformed. Using Default configuration instead.");
            }

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setDefaults() {
        prop.setProperty(MIN_BET_KEY, "10");
        prop.setProperty(MAX_BET_KEY, "200");
        prop.setProperty(START_CASH_KEY, "500");
        prop.setProperty(FIVECC_KEY, "true");
        prop.setProperty(SWEDISH_KEY, "false");
        prop.setProperty(LATESURRENDER_KEY, "false");
    }

    public int getMinBet() {
        return Integer.valueOf(prop.getProperty("minbet"));
    }

    public int getMaxBet() {
        return Integer.valueOf(prop.getProperty("maxbet"));
    }

    public int getStartCash() {
        return Integer.valueOf(prop.getProperty("startcash"));
    }

    public boolean isFiveCharlieEnabled() {
        return Boolean.valueOf(prop.getProperty(FIVECC_KEY));
    }

    public boolean isSwedishPub() {
        return Boolean.valueOf(prop.getProperty(SWEDISH_KEY));
    }

    public void setFiveCharlieEnabled(boolean e) {
        prop.setProperty(FIVECC_KEY, String.valueOf(e));
    }

    public void setSwedishPub(boolean e) {
        prop.setProperty(SWEDISH_KEY, String.valueOf(e));
    }

    public boolean isLateSurrenderEnabled() {
        return Boolean.valueOf(prop.getProperty(LATESURRENDER_KEY));
    }

    public void setLateSurrenderEnabled(boolean e) {
        prop.setProperty(LATESURRENDER_KEY, String.valueOf(e));
    }

}
