package org.durmiendo.ll.parser;

import java.util.*;

public class Lexer {
    private String input;
    private int currentIndex;
    private StandardLibrary standardLibrary;
    public List<Token> functions;
    private static final char[] SPECIAL_CHARS = {'(', ')', '{', '}', ',', ';', ':'};
    private String[] SPECIAL_WORD = {
            "function",
            "while",
            "for",
            "return",
            "if",
            "jump"
    };


    public Lexer(String input) {
        this.input = input;
        this.currentIndex = 0;
        this.standardLibrary = new StandardLibrary();
        functions = new ArrayList<>();
    }


    public List<Token> tokens = new ArrayList<>();
    public List<Token> tokenize() {


        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);

            if (Character.isWhitespace(currentChar)) {
                currentIndex++;
            } else if (isOperator(currentChar)) {
                tokens.add(processOperator());
            }else if (Character.isLetter(currentChar)) {
                tokens.add(processIdentifier());
            } else if (Character.isDigit(currentChar)) {
                tokens.add(processNumber());
            } else if (currentChar == '\'' || currentChar == '\"') {
                tokens.add(processString());
            } else if (isSpecialChar(currentChar)) {
                tokens.add(new Token(getTokenType(currentChar), new String[] {String.valueOf(currentChar)}));
                currentIndex++;
            } else {
                currentIndex++;
            }
        }

        return tokens;
    }

    private Token processOperator() {
        StringBuilder identifier = new StringBuilder();
        while (isOperator(input.charAt(currentIndex)) && currentIndex < input.length()) {
            identifier.append(input.charAt(currentIndex));
            currentIndex++;
        }

        return new Token(TokenType.operator, new String[]{identifier.toString()});

    }


    private boolean isOperator(char c) {
        for (String op : standardLibrary.getOperators()) {
            if (op.startsWith(Character.toString(c))) return true;
        }
        return false;
    }

    private Token processIdentifier() {
        StringBuilder identifier = new StringBuilder();

        while (currentIndex < input.length() && (Character.isLetterOrDigit(input.charAt(currentIndex)) || input.charAt(currentIndex) == '_' || isOperator(input.charAt(currentIndex)))) {
            identifier.append(input.charAt(currentIndex));

            currentIndex++;

        }

        String identifierStr = identifier.toString();

        if (standardLibrary.isFunction(identifierStr)) {
            return processFunction(identifierStr);
        } else if ((tokens.size() > 1) && (tokens.get(tokens.size() - 1).c[0].equals(SPECIAL_WORD[0]))) {
            Token r =  processFunction(identifierStr);
            r.type = TokenType.function;
            functions.add(r);
            tokens.remove(tokens.size() - 1);
            return r;
        } else if (standardLibrary.isOperator(identifierStr)) {
            return new Token(standardLibrary.getOperatorType(identifierStr), new String[] {identifierStr});
        } else if (Arrays.stream(SPECIAL_WORD).toList().contains(identifierStr)) {
            return new Token(TokenType.keyword, new String[] {identifierStr});
        } else if (isCall(identifierStr)) {
            return processCall(identifierStr);
        } else {
            return new Token(TokenType.name, new String[] {identifierStr});
        }
    }

    public boolean isCall(String i) {
        for (Token token : functions) {
            if (Objects.equals(token.c[0], i)) return true;
        }
        return false;
    }

    private Token processCall(String call) {
        List<String> c = new ArrayList<>();
        c.add(call);
        c.addAll(Arrays.stream(processArguments()).toList());
        return new Token(TokenType.call, c.toArray(new String[0]));
    }

    private Token processFunction(String functionName) {
        String[] arguments = processArguments();
        if (arguments.length > 0) {
            List<String> f = new ArrayList<>();
            f.add(functionName);
            f.addAll(List.of(arguments));
            return new Token(TokenType.call, f.toArray(new String[0]));
        } else {
            return new Token(TokenType.call, new String[]{functionName});
        }
    }

    private String[] processArguments() {
        List<String> arguments = new ArrayList<>();

        if (input.charAt(currentIndex) == '(') {
            currentIndex++; // Skip opening parenthesis

            while (currentIndex < input.length() && input.charAt(currentIndex) != ')') {
                StringBuilder argument = new StringBuilder();

                while (currentIndex < input.length() && input.charAt(currentIndex) != ',' && input.charAt(currentIndex) != ')') {
                    argument.append(input.charAt(currentIndex));
                    currentIndex++;
                }

                arguments.add(argument.toString().trim());

                if (currentIndex < input.length() && input.charAt(currentIndex) == ',') {
                    currentIndex++; // Skip comma
                }
            }

            if (currentIndex < input.length() && input.charAt(currentIndex) == ')') {
                currentIndex++; // Skip closing parenthesis
            }
        }

        return arguments.toArray(new String[0]);
    }

    private Token processString() { StringBuilder string = new StringBuilder();
        boolean escape = false;
        char quoteChar = input.charAt(currentIndex);
        currentIndex++; // Skip initial quote

        while (currentIndex < input.length() && (input.charAt(currentIndex) != quoteChar || escape)) {
            if (input.charAt(currentIndex) == '\\' && !escape) {
                escape = true;
            } else {
                string.append(input.charAt(currentIndex));
                escape = false;
            }

            currentIndex++;
        }

        if (currentIndex < input.length() && input.charAt(currentIndex) == quoteChar) {
            currentIndex++; // Skip closing quote
        }

        return new Token(TokenType.str, new String[] {string.toString()});
    }

    private Token processNumber() {
        StringBuilder number = new StringBuilder();

        while (currentIndex < input.length() && Character.isDigit(input.charAt(currentIndex))) {
            number.append(input.charAt(currentIndex));
            currentIndex++;
        }

        return new Token(TokenType.num, new String[] {number.toString()});
    }

    private TokenType getTokenType(char character) {
        switch (character) {
            case '(':
                return TokenType.opening;
            case ')':
                return TokenType.ending;
            case '{':
                return TokenType.opening;
            case '}':
                return TokenType.ending;
            case ',':
                return TokenType.special;
            case ';':
                return TokenType.special;
            case ':':
                return TokenType.special;
            default:
                return TokenType.unknown;
        }
    }

    private boolean isSpecialChar(char character) {
        for (char specialChar : SPECIAL_CHARS) {
            if (character == specialChar) {
                return true;
            }
        }
        return false;
    }
}