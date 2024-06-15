package org.jbox;

import java.util.List;

// we are amplement this grammar
/*
    expression     → equality ;
    equality       → comparison ( ( "!=" | "==" ) comparison )* ;
    comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    term           → factor ( ( "-" | "+" ) factor )* ;
    factor         → unary ( ( "/" | "*" ) unary )* ;
    unary          → ( "!" | "-" ) unary
                   | primary ;
    primary        → NUMBER | STRING | "true" | "false" | "nil"
                   | "(" expression ")" ;
 */

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens){
        this.tokens=tokens;
    }

    private Expr expression(){
        return equality();
    }

    private Expr equality(){
        Expr expr= comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)){
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private boolean math(TokenType... types){
        for (TokenType type:types){
            if(check(type)){
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type){
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance(){
        if (!isAtEnd()) current++;
        return previous();
    }
}
