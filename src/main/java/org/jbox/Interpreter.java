package org.jbox;

public class Interpreter implements Expr.Visitor<Object>{

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left=evaluate(expr.left);
        Object right=evaluate(expr.right);

        switch (expr.operator.type){
            case GREATER:
                return (double)left > (double) right;
            case GREATER_EQUAL:
                return (double)left >= (double) right;
            case LESS:
                return (double)left < (double) right;
            case LESS_EQUAL:
                return (double)left <= (double) right;
            case BANG_EQUAL: return !isEqual(left,right);
            case EQUAL_EQUAL: return isEqual(left,right);
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if(left instanceof Double && right instanceof Double){
                    return (double) left + (double) right;
                }
                if(left instanceof String && right instanceof String){
                    return (String) left + (String) right;
                }
                //moi
                if((left instanceof String || left instanceof Double) && (right instanceof String || right instanceof Double)){
                    return (String) left + (String) right;
                }
                break;
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
        }
        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right=evaluate(expr.right);

        switch (expr.operator.type){
            case BANG:
                return !isTruthy(right);
            case MINUS:
                return - (double) right;
        }
        return null;
    }

    private Object evaluate(Expr expr){
        return expr.accept(this);
    }

    private boolean isTruthy(Object object){
        if(object == null) return false;
        if(object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEqual(Object left,Object right){
        if(left==null && right==null) return true;
        if(left==null) return false;

        return left.equals(right);
    }
}
