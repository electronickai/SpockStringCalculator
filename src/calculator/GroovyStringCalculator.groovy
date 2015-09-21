package calculator

import common.ErrorHandler

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by schmidka on 18.09.2015.
 */
class GroovyStringCalculator {

        static final int MIN_THRESHOLD = 0
        static final int MAX_THRESHOLD = 1000

        static final String SEPARATOR_PREFIX = "//"
        static final String SEPARATOR_NUMBERS_PATTERN = "//(.*)\\n(.*)"
        static final String SEPARATOR_SPLIT = "\\]\\["
        static final String COMMA_OR_NEW_LINE = ",|\n"
        static final String FIND_ASTERISK = "\\*"
        static final String ESCAPE_ASTERISK = "\\\\*"

        static final String ILLEGAL_ARGUMENT_MESSAGE = "negatives not allowed"

        ErrorHandler errorHandler

        public int add(String numbers) {

            if (!numbers) {
                return 0
            }

            String[] numberParts = extractSingleNumbers(numbers)
            addAllNumbers(numberParts)
        }

        private String[] extractSingleNumbers(String command) {
            if (usesCustomSeparator(command)) {
                return retrieveNumbersWithCustomSeparator(command)
            }
            command.split(COMMA_OR_NEW_LINE)
        }

        private boolean usesCustomSeparator(String command) {
            command.startsWith(SEPARATOR_PREFIX)
        }

        private String[] retrieveNumbersWithCustomSeparator(String command) {
            Matcher m = matchPatternToSplitSeparatorAndNumbers(command)
            String separators
            String numbers
            try {
                separators = m.group(1)
                numbers = m.group(2)
            }
            catch (IllegalStateException e) {
                errorHandler.handleError(e)
                return []
            }

            ArrayList<String> separatorList = retrieveListOfSeparators(separators)
            String splitCriteria = assembleSplitCriteria(separatorList)
            numbers.split(splitCriteria)
        }

        private Matcher matchPatternToSplitSeparatorAndNumbers(String command) {
            Pattern p = Pattern.compile(SEPARATOR_NUMBERS_PATTERN)
            Matcher m = p.matcher(command)
            m.matches()
            m
        }

        private ArrayList<String> retrieveListOfSeparators(String separators) {
            String[] separatorParts = separators.split(SEPARATOR_SPLIT)

            ArrayList<String> separatorList = new ArrayList<>()
            separatorParts.each {

                if (it.length() > 1) {
                    it = it.replace("[", "")
                    it = it.replace("]", "")
                }
                it = it.replaceAll(FIND_ASTERISK, ESCAPE_ASTERISK) //"real" implementation would have to consider all meta characters...

                separatorList.add(it)
            }
            separatorList
        }

        private String assembleSplitCriteria(ArrayList<String> separatorList) {
            String splitCriteria = ""
            separatorList.each {
                splitCriteria += "$it|"
            }
            splitCriteria = splitCriteria.substring(0, splitCriteria.length() - 1)
            splitCriteria
        }

        private int addAllNumbers(String[] numberParts) {
            int result = 0

            numberParts.each {
                int numberToAdd = it.toInteger()
                if (numberToAdd < MIN_THRESHOLD) {
                    throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE);
                }
                if (numberToAdd < MAX_THRESHOLD) {
                    result += numberToAdd
                }
            }
            result
        }
    }