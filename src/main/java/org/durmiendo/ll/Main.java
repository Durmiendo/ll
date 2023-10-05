package org.durmiendo.ll;


import org.durmiendo.ll.parser.Lexer;
import org.durmiendo.ll.parser.Token;
import org.durmiendo.ll.parser.TokenType;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        String input = "p(0, 0) {" +
                "function test(argg,gds, sdg) {" +
                "    return argg + 100;" +
                "}" +
                "num = test(734);" +
                "print(ds, sf); " +
                "print(jsfsf,sfddsf, sfs, sfd,fs);" +
                "print() ;" +
                "}";

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.print(token.type + ": ");

            if (token.c.length > 1) {
                System.out.print("[");
                for (int i = 0; i < token.c.length - 1; i++) {
                    System.out.print(token.c[i] + ", ");
                }
                System.out.print(token.c[token.c.length - 1]);
                System.out.print("]");
            } else {
                System.out.print(token.c[0]);
            }

            System.out.println();
        }
    }
}