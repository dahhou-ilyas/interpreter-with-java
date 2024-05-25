package org.example;

import java.util.ArrayList;
import java.util.List;
import static org.example.TokenType.*;
public class Scanner {

    private int start = 0;
    private int current = 0;
    private int line = 1;

    private final String source;
    private final List<Token> tokens =new ArrayList<>();

    Scanner(String source){
        this.source=source;
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }


    List<Token> scanTokens(){
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken(){
        char c= advance();

        switch (c){
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
        }
    }

}
