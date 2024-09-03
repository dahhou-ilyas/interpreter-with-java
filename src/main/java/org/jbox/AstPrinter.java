package org.jbox;

public class AstPrinter implements Expr.Visitor {
    public static void main(String[] args) {
        Expr expression=new Expr.Binary(
                new Expr.Unary(new Token(TokenType.PLUS,"+",null,1),new Expr.Literal(1111)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Literal(45.67))
        );
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        return null;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme,expr.left,expr.right);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group",expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        if(expr.value == null) return null;
        return expr.value.toString();
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme,expr.right);
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return null;
    }

    public String print(Expr expr){
        return (String) expr.accept(this);
    }

    public String parenthesize(String name,Expr... exprs){
        StringBuilder builder =new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr:exprs){
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    //In reverse Polish notation (RPN)
    //create a méthode printer with RPN méthode
}
