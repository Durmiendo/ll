package org.durmiendo.ll.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardLibrary {
    private Map<String, TokenType> operators;
    private Map<String, FunctionMetadata> functions;

    public StandardLibrary() {
        operators = new HashMap<>();
        functions = new HashMap<>();

        // Добавляем доступные операторы
        operators.put("=", TokenType.operator);
        operators.put("+", TokenType.operator);
        operators.put("/", TokenType.operator);
        operators.put("*", TokenType.operator);
        operators.put("++", TokenType.operator);
        // Добавьте остальные операторы по аналогии
        // ...

        // Добавляем доступные функции
        addFunction("print", TokenType.keyword, null); // Функция print без кода
        addFunction("p", TokenType.keyword, null);
        // Добавьте другие функции с кодом по аналогии
        // ...
    }

    public void addFunction(String functionName, TokenType tokenType, String code) {
        functions.put(functionName, new FunctionMetadata(tokenType, code));
    }

    public boolean isOperator(String operator) {
        return operators.containsKey(operator);
    }

    public TokenType getOperatorType(String operator) {
        return operators.get(operator);
    }

    public boolean isFunction(String function) {
        return functions.containsKey(function);
    }

    public TokenType getFunctionType(String function) {
        if (functions.containsKey(function)) {
            return functions.get(function).getTokenType();
        }
        return TokenType.unknown; // Если функция не существует в стандартной библиотеке, возвращаем тип unknown
    }

    public String getFunctionCode(String function) {
        if (functions.containsKey(function)) {
            return functions.get(function).getCode();
        }
        return null; // Если функция не существует в стандартной библиотеке, возвращаем null
    }

    public List<String> getOperators() {
        return new ArrayList<>(operators.keySet());
    }
}

class FunctionMetadata {
    private TokenType tokenType;
    private String code;

    public FunctionMetadata(TokenType tokenType, String code) {
        this.tokenType = tokenType;
        this.code = code;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getCode() {
        return code;
    }
}