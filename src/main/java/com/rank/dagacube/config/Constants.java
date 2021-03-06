package com.rank.dagacube.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String DEPOSIT = "Deposit";
    public static final String WAGE = "Wage";
    public static final String VIEW_BALANCE = "View Balance";
    public static final String PROMOTION = "paper";
    public static final Long NEXT_PROMOTION = 4L;

    private Constants() {}
}
