package org.durmiendo.ll.parser;

public class Token {
    public TokenType type;
    public String[] c;

    public Token(TokenType type, String[] c) {
        this.type = type;
        this.c = c;
    }
}
