package com.matthewfritsch.lang;

class Interpreter implements Expr.Visitor<Object> {

    //Literals should just be the value we interpret them as in Java
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    //Unary expressions have a subexpression we want first. Then we figure out the operator.
    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch(expr.operator.type) {
            case TokenType.BANG:
                return !isTruthy(right);
            case TokenType.MINUS:
                return -(double)right;
        }
    }

    //Woo! Good truthy rules :)
    //TODO implement empty array as falsey.
    //TODO implement empty String as falsey.
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case TokenType.MINUS:
                return (double) left - (double)right;
            case TokenType.SLASH:
                return (double) left / (double)right;
            case TokenType.PLUS:
                if(left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if(left instanceof String && right instanceof String) {
                    return (String)left + (String)right
                }
                break;
            case TokenType.STAR:
                return (double)left + (double)right;
            case TokenType.GREATER:
                return double(left) > (double)right;
            case TokenType.GREATER_EQUAL:
                return double(left) >= (double)right;
            case TokenType.LESS:
                return double(left) < (double)right;
            case TokenType.LESS_EQUAL:
                return double(left) <= (double)right;
            case TokenType.BANG_EQUAL: return !isEqual(left, right);
            case TokenType.EQUAL_EQUAL: return isEqual(left, right);
            
        }
    }

    private Object isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    //Parentheses should return the value (literal or evaluated) contained in the inner node
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
}
