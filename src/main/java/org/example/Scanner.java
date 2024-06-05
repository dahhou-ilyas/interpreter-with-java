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
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if(match('/')){
                    // on a ajouté !isAtEnd pour evité le out of range exception !
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if (match('*')) {

                    boolean isEndOfComment=false;

                    while (!isEndOfComment && !isAtEnd()){

                        char cc=advance();

                        if(cc=='\n'){
                            line++;
                        }
                        if(cc=='*' && peek()=='/'){
                            isEndOfComment=true;
                            advance();
                        }
                    }
                    if(isAtEnd() && !isEndOfComment){
                        JBox.error(line, "Unterminated comment.");
                        return;
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if(isDigit(c)){
                    number();
                }else {
                    JBox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private boolean isDigit(char c){
        if(c>='0' && c<='9'){
            return true;
        }
        return false;
    }

    private void number(){
        while (isDigit(peek())) advance();

        if (peek()=='.' && isDigit(peekNext())){
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(NUMBER,Double.parseDouble(source.substring(start,current)));
    }

    private void string(){

        while (peek()!='"' && !isAtEnd()){
            if(peek() == '\n') line++; //interdire les multiligne est plus complexe que l'autorisé hhhhhhh
            System.out.println(source.charAt(current));
            advance();
        }
        if (isAtEnd()) {
            JBox.error(line, "Unterminated string.");
            return;
        }
        advance(); // return " end of string et incrimenter current
        String value = source.substring(start+1 , current-1);
        addToken(STRING, value);
    }

    private char advance(){
        return source.charAt(current++);
    }

    private char peek(){
        if(isAtEnd()) return '\0';

        return source.charAt(current);
    }
    private boolean match(char expected){
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }


    private void addToken(TokenType tokenType){
        addToken(tokenType,null);
    }

    private void addToken(TokenType tokenType,Object literal){
        String text=source.substring(start,current);
        tokens.add(new Token(tokenType,text,literal,line));
    }

}
