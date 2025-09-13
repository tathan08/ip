package jinjja.parser;

/**
 * Enum representing the different types of commands that the application can handle.
 */
public enum CommandType {
    BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, TENTATIVE, CONFIRM, DELETE, FIND, UNKNOWN;

    /**
     * Converts a string input to the corresponding CommandType enum.
     *
     * @param input The string input to convert.
     * @return The corresponding CommandType, or UNKNOWN if no match is found.
     */
    public static CommandType fromString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return UNKNOWN;
        }

        try {
            return CommandType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
