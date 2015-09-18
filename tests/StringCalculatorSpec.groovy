import spock.lang.*

@Title("String Calculator Kata")
@Narrative(
"""List of all requirements that are stated in the TDD Kata
based on http://osherove.com/tdd-kata-1/""")
class StringCalculatorSpec extends Specification {

    @Subject
    def calculator = new JavaStringCalculator()

    @See("Requirement 1")
    def "The result of an empty string is 0"() {
        expect:
        calculator.add("") == 0
    }

    @Unroll
    @See("Requirements 1 and 2")
    def "Adding #numbers returns #result"() {
        expect: "given numbers are added"
        calculator.add(numbers) == result

        where:
        numbers || result
        "5"     || 5
        "1,2"   || 3
        "1,5,20,15,10,3,9" || 63
    }

    @See("Requirement 3")
    def "new lines characters shall be valid besides of a comma"() {
        expect: "new line is treated as a separator of numbers as well"
        calculator.add("1,5\n20,15\n10,3\n9") == 63
    }

    @See("Requirement 4")
    def "the delimiter can be initialized by  //"() {
        expect: "new line is treated as a separator of numbers as well"
        calculator.add("//;\n1;2") == 3
    }

    @See("Requirement 5")
    def "using a negative number throws an exception"() {
        when: "calculator shall add a negative number"
        calculator.add("1,-1,5")
        then: "an exception is thrown"
        IllegalArgumentException e = thrown(IllegalArgumentException)
        e.message == "negatives not allowed"
    }

    @See("Requirement 6")
    def "Numbers bigger than 1000 should be ignored, so adding 2 + 1001  = 2"() {
        expect: "new line is treated as a separator of numbers as well"
        calculator.add("2,1001") == 2
    }

    @See("Requirement 7")
    def "Delimiters can be of any length with the following format: //[delimiter]\\n" () {
        expect:
        calculator.add("//[***]\n1***2***3") == 6
    }

    @See("Requirement 8")
    def "Allow multiple delimiters like this: //[delim1][delim2]\\n"() {
        expect:
        calculator.add("//[*][%]\n1*2%3") == 6
    }

    @See("Requirement 9")
    def "make sure you can also handle multiple delimiters with length longer than one char"() {
        expect:
        calculator.add("//[*%][%*]\n4%*5*%6") == 15
    }

    def "Invalid inputs should return a valid error message - fake error handler to illustrate mocking" () {
        given: "an error handler to handle the exception caused by wrong input"
        ErrorHandler handler = Mock(ErrorHandler)
        calculator.setErrorHandler(handler)
        when: "The wrong input is taken"
        calculator.add("//")
        then: "The handler should be invoked to handle the error"
        1 * handler.handleError(_ as IllegalStateException)
    }
}