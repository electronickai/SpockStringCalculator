import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by schmidka on 16.09.2015.
 */
public class JavaStringCalculator {

    private static final int MIN_THRESHOLD = 0;
    private static final int MAX_THRESHOLD = 1000;
    private static final String SEPARATOR_NUMBERS_PATTERN = "//(.*)\\n(.*)";
    private static final String COMMA_OR_NEW_LINE = ",|\n";

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
        return command.startsWith("//");
    }

    private String[] retrieveNumbersWithCustomSeparator(String command) {
        Pattern p = Pattern.compile(SEPARATOR_NUMBERS_PATTERN);
        Matcher m = p.matcher(command);
        m.matches();
        String separator = m.group(1);
        String numbers = m.group(2);

        //TODO KSC 18.09.15: Find a better solution than just replacing the "*"
        if (separator.startsWith("[") && separator.endsWith("]")) {
            separator = separator.substring(1, separator.length() - 1);
            separator = separator.replaceAll("\\*", ",");
            numbers = numbers.replaceAll("\\*", ",");
        }
        return numbers.split(separator);
    }

    private int addAllNumbers(String[] numberParts) {
        int result = 0;
        for (String number : numberParts) {
            int numberToAdd = Integer.parseInt(number);
            if (numberToAdd < MIN_THRESHOLD) {
                throw new IllegalArgumentException("negatives not allowed");
            }
            if (numberToAdd > MAX_THRESHOLD) {
                continue;
            }
            result += numberToAdd;
        }
        return result;
    }
}
