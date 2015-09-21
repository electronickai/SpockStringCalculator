package calculator;

import common.ErrorHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by schmidka on 16.09.2015.
 */
public class JavaStringCalculator {

    private static final int MIN_THRESHOLD = 0;
    private static final int MAX_THRESHOLD = 1000;

    private static final String SEPARATOR_PREFIX = "//";
    private static final String SEPARATOR_NUMBERS_PATTERN = "//(.*)\\n(.*)";
    private static final String SEPARATOR_SPLIT = "\\]\\[";
    private static final String COMMA_OR_NEW_LINE = ",|\n";
    private static final String FIND_ASTERISK = "\\*";
    private static final String ESCAPE_ASTERISK = "\\\\*";

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "negatives not allowed";

    ErrorHandler errorHandler;

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public int add(String numbers) {

        if (numbers.isEmpty()) {
            return 0;
        }

        String[] numberParts = extractSingleNumbers(numbers);
        return addAllNumbers(numberParts);
    }

    private String[] extractSingleNumbers(String command) {
        if (usesCustomSeparator(command)) {
            return retrieveNumbersWithCustomSeparator(command);
        }
        return command.split(COMMA_OR_NEW_LINE);
    }

    private boolean usesCustomSeparator(String command) {
        return command.startsWith(SEPARATOR_PREFIX);
    }

    private String[] retrieveNumbersWithCustomSeparator(String command) {
        Matcher m = matchPatternToSplitSeparatorAndNumbers(command);
        String separators;
        String numbers;
        try {
            separators = m.group(1);
            numbers = m.group(2);
        }
        catch (IllegalStateException e) {
            errorHandler.handleError(e);
            return new String[] {};
        }

        ArrayList<String> separatorList = retrieveListOfSeparators(separators);
        String splitCriteria = assembleSplitCriteria(separatorList);
        return numbers.split(splitCriteria);
    }

    private Matcher matchPatternToSplitSeparatorAndNumbers(String command) {
        Pattern p = Pattern.compile(SEPARATOR_NUMBERS_PATTERN);
        Matcher m = p.matcher(command);
        m.matches();
        return m;
    }

    private ArrayList<String> retrieveListOfSeparators(String separators) {
        String[] separatorParts = separators.split(SEPARATOR_SPLIT);

        ArrayList<String> separatorList = new ArrayList<>();
        for (String separatorPart : separatorParts) {

            if (separatorPart.length() > 1) {
                separatorPart = separatorPart.replace("[", "");
                separatorPart = separatorPart.replace("]", "");
            }
            separatorPart = separatorPart.replaceAll(FIND_ASTERISK, ESCAPE_ASTERISK); //"real" implementation would have to consider all meta characters...

            separatorList.add(separatorPart);
        }
        return separatorList;
    }

    private String assembleSplitCriteria(ArrayList<String> separatorList) {
        String splitCriteria = "";
        for (String separator : separatorList) {
            splitCriteria = splitCriteria + separator + "|";
        }
        splitCriteria = splitCriteria.substring(0, splitCriteria.length() - 1);
        return splitCriteria;
    }

    private int addAllNumbers(String[] numberParts) {
        int result = 0;

        for (String number : numberParts) {
            int numberToAdd = Integer.parseInt(number);
            if (numberToAdd < MIN_THRESHOLD) {
                throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
            }
            if (numberToAdd > MAX_THRESHOLD) {
                continue;
            }
            result += numberToAdd;
        }
        return result;
    }
}
