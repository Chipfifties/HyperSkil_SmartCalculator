package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class InputParser {
    private static final String DIGIT_REGEX = "[-+]?\\d++";
    private static final String COMMAND_REGEX = "^/\\w+$";
    private static final String VARIABLE_REGEX = "^[A-Za-z]+?$";
    private static final String OPERATOR_REGEX = "[+\\-*/]";
    private static final Variables variable = new Variables();
    private static final List<String> postFix = new LinkedList<>();
    private static List<String> inFix = new LinkedList<>();
    private final String expression;

    InputParser(String expression) {
        this.expression = expression;
    }

    void run() {
        if (expression.contains("=")) {
            assignVariable();
        } else if (expression.matches(COMMAND_REGEX)) {
            System.out.println(new ErrorCode(5).getError());
        } else {
            if (expression.matches(VARIABLE_REGEX) && variable.contains(expression)) {
                System.out.println(variable.getVariable(expression));
            } else if (expression.matches(VARIABLE_REGEX) && !variable.contains(expression)) {
                System.out.println(new ErrorCode(3).getError());
            } else if (getInfix(expression)) {
                infixToPostfix();
                System.out.println(getResult());
            } else {
                System.out.println(new ErrorCode(4).getError());
            }
        }
    }

    private void assignVariable() {
        List<String> values = Arrays.stream(expression.split("="))
                .map(String::trim)
                .collect(Collectors.toList());
        if (values.get(0).matches(VARIABLE_REGEX) && values.get(1).matches(DIGIT_REGEX)) {
            variable.setVariable(values.get(0), new BigInteger(values.get(1)));
        } else if (values.get(0).matches(VARIABLE_REGEX) && values.get(1).matches(VARIABLE_REGEX)) {
            variable.setVariable(values.get(0), values.get(1));
        } else {
            if (!values.get(0).matches(VARIABLE_REGEX)) {
                System.out.println(new ErrorCode(1).getError());
            } else {
                System.out.println(new ErrorCode(2).getError());
            }
        }
    }

    private static String getResult() {
        Stack<BigInteger> stack = new Stack<>();

        for (String value : postFix) {
            if (value.matches(DIGIT_REGEX)) {
                stack.push(new BigInteger(value));
            } else if (value.matches(VARIABLE_REGEX)) {
                stack.push(variable.getVariable(value));
            } else if (value.matches(OPERATOR_REGEX)) {
                BigInteger operator1 = stack.pop();
                BigInteger operator2 = stack.pop();
                switch (value) {
                    case "+":
                        stack.push(operator2.add(operator1));
                        break;
                    case "-":
                        stack.push(operator2.subtract(operator1));
                        break;
                    case "*":
                        stack.push(operator2.multiply(operator1));
                        break;
                    case "/":
                        stack.push(operator2.divide(operator1));
                        break;
                }
            }
        }
        return stack.pop().toString();
    }

    private static boolean getInfix(String expression) {

        if (expression.contains("**") || expression.contains("//")) {
            return false;
        }

        Deque<String> brackets = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' && result.charAt(result.length() - 1) == '+') {
                continue;
            } else if (expression.charAt(i) == ' ') {
                continue;
            } else if (expression.charAt(i) == '-' && result.charAt(result.length() - 1) == '-') {
                result.replace(result.length() - 1, result.length(), "+");
            } else if (expression.charAt(i) == '-' && result.charAt(result.length() - 1) == '+') {
                result.replace(result.length() - 1, result.length(), "-");
            } else {
                result.append(expression.charAt(i));
            }

            if (i > 0 && expression.charAt(i) == ')' && expression.charAt(i - 1) == '(') {
                return false;
            }
            if (expression.charAt(i) == '(') {
                brackets.addFirst("(");
            } else if (expression.charAt(i) == ')' && !brackets.isEmpty()) {
                brackets.pollFirst();
            } else if (expression.charAt(i) == ')' && brackets.isEmpty()) {
                return false;
            }
        }
        result = new StringBuilder(result.toString()
                .replaceAll("\\+", " + ")
                .replaceAll("-", " - ")
                .replaceAll("\\(", " ( ")
                .replaceAll("\\)", " ) ")
                .replaceAll("\\*", " * ")
                .replaceAll("/", " / "));

        inFix = Arrays.stream(result.toString().split("\\s+")).collect(Collectors.toList());

        for (String value : inFix) {
            if (value.matches(VARIABLE_REGEX) && !variable.contains(value)) {
                return false;
            }
        }
        return true;
    }

    static int Prec(String ch) {
        switch (ch) {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }

    static void infixToPostfix() {
        Stack<String> stack = new Stack<>();

        for (String c : inFix) {
            if (c.matches(VARIABLE_REGEX) || c.matches(DIGIT_REGEX)) {
                postFix.add(c);
            } else if (c.equals("(")) {
                stack.push(c);
            } else if (c.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postFix.add(stack.pop());
                }
                if (!stack.isEmpty() && !stack.peek().equals("(")) {
                    System.out.println(new ErrorCode(4).getError());
                } else {
                    stack.pop();
                }
            } else {
                while (!stack.isEmpty() && Prec(c) <= Prec(stack.peek())) {
                    if (stack.peek().equals("(")) {
                        System.out.println(new ErrorCode(4).getError());
                    }
                    postFix.add(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                System.out.println(new ErrorCode(4).getError());
            }
            postFix.add(stack.pop());
        }
    }
}
