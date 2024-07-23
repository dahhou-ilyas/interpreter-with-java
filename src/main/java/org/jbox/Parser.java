package org.jbox;

import java.rmi.server.ExportException;
import java.util.List;

// la logic en générale est de apartire de la liste des tokent qui vienne depuit le scannet on fait des analyse sur ces tocken (parsing)
// et on test si il respect le grammaire qu'on a définit

// pour faire ces travaille on fait une méthode qui s'appelle (un analyseur descendant )


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

import static org.jbox.TokenType.*;

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

    private Expr comparison(){
        Expr expr = term() ;
        while (match(GREATER, GREATER_EQUAL, LESS_EQUAL, LESS)) {
            Token operatore =previous() ;
            Expr right = term() ;
            expr = new Expr.Binary(expr,operatore,right) ;
        }
        return expr ;
    }

    private Expr term(){
        Expr expr = factor();
        while (match(MINUS,PLUS)){
            Token operatore =previous();
            Expr right=factor();
            expr=new Expr.Binary(expr,operatore,right);
        }
        return expr;
    }

    private Expr factor(){
        Expr expr= unary();
        while (match(SLASH,STAR)){
            Token operatore = previous();
            Expr right=unary();
            expr =new Expr.Binary(expr,operatore,right);
        }
        return expr;
    }

    private Expr unary(){
        if (match(MINUS,BANG)){
            Token operatore=previous();
            Expr right= unary();
            return new Expr.Unary(operatore,right);
        }
        return primary();
    }

    private Expr primary(){
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
    }
    private boolean match(TokenType... types){
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

    private boolean isAtEnd(){
        return peek().type==EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current-1);
    }
}
