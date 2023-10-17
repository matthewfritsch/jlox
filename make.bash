export PROJ_DIR=$(pwd)
export PROJ_PKG_EXT=com/matthewfritsch
export PROJ_PKG_DIR=$PROJ_DIR/$PROJ_PKG_EXT

alias clean="rm $PROJ_PKG_DIR/*/*.class"
alias cbuild="clean && build"
alias fullbuild="clean; build util && build tool && java $PROJ_PKG_EXT/tool/GenerateAst $PROJ_PKG_EXT/lang && build lang"

function exists {
  found=1
  for f in $(ls $PROJ_PKG_DIR); do
    if [ $found == 1 ] && [ "$f" == "$A1" ]; then
      found=0
    fi
  done
  return $found
}

function build {
  A1=$1
  if [ "$A1" == "" ]; then
    A1="lang"
  fi

  exists $A1
  found=$?

  if [ $found == 0 ]; then
    javac $PROJ_PKG_DIR/$A1/*.java
  else
    echo "$A1 is not in the directory."
  fi

  return $found
}
