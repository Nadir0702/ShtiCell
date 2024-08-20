package logic.function.parser;

import component.api.CellType;
import logic.function.Function;
import logic.function.math.Abs;
import logic.function.math.Minus;
import logic.function.math.Plus;
import logic.function.string.Concat;
import logic.function.string.Sub;
import logic.function.system.Identity;
import logic.function.system.Ref;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public enum FunctionParser {
    IDENTITY{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for IDENTITY function." +
                        " Expected 1, but got " + arguments.size());
            }

            String value = arguments.getFirst().trim();
            if (isBoolean(value)) {
                return new Identity(Boolean.parseBoolean(value), CellType.BOOLEAN);
            } else if (isNumber(value)) {
                return new Identity(Double.parseDouble(value), CellType.NUMERIC);
            } else {
                return new Identity(value, CellType.STRING);
            }
        }

        private boolean isBoolean(String value) {
            return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
        }

        private boolean isNumber(String value) {
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    ABS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function." +
                        " Expected 1, but got " + arguments.size());
            }

            Function argument = parseFunction(arguments.getFirst().trim());

            if (!argument.getReturnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for PLUS function." +
                        " Expected NUMERIC, but got " + argument.getReturnType());
            }

            return new Abs(argument);
        }
    },
    PLUS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for PLUS function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst().trim());
            Function secondArgument = parseFunction(arguments.getLast().trim());

            if (!firstArgument.getReturnType().equals(CellType.NUMERIC) ||
                    !secondArgument.getReturnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for PLUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Plus(firstArgument, secondArgument);
        }
    },
    MINUS{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for MINUS function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst().trim());
            Function secondArgument = parseFunction(arguments.getLast().trim());

            if (!firstArgument.getReturnType().equals(CellType.NUMERIC) ||
                    !secondArgument.getReturnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for MINUS function." +
                        " Expected NUMERIC, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Minus(firstArgument, secondArgument);
        }
    },
    CONCAT{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 2) {
                throw new IllegalArgumentException("Invalid number of arguments for CONCAT function." +
                        " Expected 2, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst().trim());
            Function secondArgument = parseFunction(arguments.getLast().trim());

            if (!firstArgument.getReturnType().equals(CellType.STRING) ||
                    !secondArgument.getReturnType().equals(CellType.STRING)) {
                throw new IllegalArgumentException("Invalid argument types for CONCAT function." +
                        " Expected STRING, but got " + firstArgument.getReturnType() +
                        " and " + secondArgument.getReturnType());
            }

            return new Concat(firstArgument, secondArgument);
        }
    },
    SUB{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 3) {
                throw new IllegalArgumentException("Invalid number of arguments for SUB function." +
                        " Expected 3, but got " + arguments.size());
            }

            Function firstArgument = parseFunction(arguments.getFirst().trim());
            Function secondArgument = parseFunction(arguments.getLast().trim());
            Function thirdArgument = parseFunction(arguments.getLast().trim());

            if (!firstArgument.getReturnType().equals(CellType.NUMERIC) ||
                    !secondArgument.getReturnType().equals(CellType.NUMERIC) ||
                    !thirdArgument.getReturnType().equals(CellType.NUMERIC)) {
                throw new IllegalArgumentException("Invalid argument types for SUB function." +
                        " Expected STRING, NUMERIC and NUMERIC, but got " + firstArgument.getReturnType() +
                        ", " + secondArgument.getReturnType() + " and " + thirdArgument.getReturnType());
            }

            return new Sub(firstArgument, secondArgument, thirdArgument);
        }
    },
    REF{
        @Override
        public Function parse(List<String> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for REF function." +
                        " Expected 1, but got " + arguments.size());
            }

            Function argument = parseFunction(arguments.getFirst().trim());

            if (!argument.getReturnType().equals(CellType.STRING)) {
                throw new IllegalArgumentException("Invalid argument types for REF function." +
                        " Expected Cell ID, but got " + argument.getFunctionName());
            }

            return new Ref(argument);
        }
    };

    abstract public Function parse(List<String> arguments);

    public static Function parseFunction(String input){
        if(input.startsWith("{") && input.endsWith("}")){

            String functionContent = input.substring(1, input.length()-1);
            List<String> topLevelParts = parseMainParts(functionContent);

            String functionName = topLevelParts.getFirst().trim().toUpperCase();

            topLevelParts.removeFirst();
            return FunctionParser.valueOf(functionName).parse(topLevelParts);
        }

        return FunctionParser.IDENTITY.parse(List.of(input.trim()));
    }

    private static List<String> parseMainParts(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : input.toCharArray()) {
            if (c == '{') {
                stack.push(c);
            } else if (c == '}') {
                stack.pop();
            }

            if (c == ',' && stack.isEmpty()) {
                // If we are at a comma and the stack is empty, it's a separator for top-level parts
                parts.add(buffer.toString().trim());
                buffer.setLength(0); // Clear the buffer for the next part
            } else {
                buffer.append(c);
            }
        }

        // Add the last part
        if (!buffer.isEmpty()) {
            parts.add(buffer.toString().trim());
        }

        return parts;
    }
}
