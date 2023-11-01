package com.matthewfritsch.lang;

class Interpreter implements Expr.Visitor<Object> {

    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if(text.endsWith(".0")) {
                text = text.substring(0, text.length()-2);
            }
            return text;
        }
        return object.toString();
    }
    
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
                checkNumberOperand(expr.operator, right);
                return -(double)right;
        }
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operator must be a number.");
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
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double)right;
            case TokenType.SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double) left / (double)right;
            case TokenType.PLUS:
                if(left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if(left instanceof String && right instanceof String) {
                    return (String)left + (String)right
                }
                
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
            case TokenType.STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left + (double)right;
            case TokenType.GREATER:
                checkNumberOperands(expr.operator, left, right);
                return double(left) > (double)right;
            case TokenType.GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return double(left) >= (double)right;
            case TokenType.LESS:
                checkNumberOperands(expr.operator, left, right);
                return double(left) < (double)right;
            case TokenType.LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return double(left) <= (double)right;
            case TokenType.BANG_EQUAL: return !isEqual(left, right);
            case TokenType.EQUAL_EQUAL: return isEqual(left, right);
            
        }
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
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
