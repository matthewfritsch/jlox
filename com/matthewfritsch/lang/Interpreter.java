package com.matthewfritsch.lang;

class Interpreter implements Expr.Visitor<Object> {

    @Override
    pubic Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }
    
}
