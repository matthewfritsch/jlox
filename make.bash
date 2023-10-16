export PROJ_DIR=$(pwd)

alias clean="rm $PROJ_DIR/com/matthewfritsch/*/*.class"
alias build="javac $PROJ_DIR/com/matthewfritsch/lang/Lox.java"
alias cbuild="clean && build"
