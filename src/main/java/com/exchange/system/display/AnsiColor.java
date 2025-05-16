package com.exchange.system.display;

public enum AnsiColor {
    BLACK("\033[30;4m", "\033[40m"),
    RED("\033[31;4m", "\033[41m"),
    GREEN("\033[32;4m", "\033[42m"),
    YELLOW("\033[33;4m", "\033[43m"),
    BLUE("\033[34;4m", "\033[44m"),
    MAGENTA("\033[35;4m", "\033[45m"),
    CYAN("\033[36;4m", "\033[46m"),
    WHITE("\033[37;4m", "\033[47m"),
    BLACK_RED("\033[40;31;4m", null),
    RED_GREEN("\033[41;32;4m", null),
    GREEN_YELLOW("\033[42;33;4m", null),
    YELLOW_BLUE("\033[43;34;4m", null),
    BLUE_MAGENTA("\033[44;35;4m", null),
    MAGENTA_CYAN("\033[45;36;4m", null),
    CYAN_WHITE("\033[46;37;4m", null),
    WHITE_BG("\033[47;4m", null);

    private static final String RESET = "\033[0m";

    private final String foregroundCode;
    private final String backgroundCode;

    AnsiColor(String foregroundCode, String backgroundCode) {
        this.foregroundCode = foregroundCode;
        this.backgroundCode = backgroundCode;
    }

    public String format(String text) {
        if (text == null) {
            return null;
        }
        return foregroundCode + text + RESET;
    }

}