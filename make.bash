export PROJ_DIR=$(pwd)

alias clean="rm $PROJ_DIR/com/matthewfritsch/*/*.class"
alias cbuild="clean && build"

function build {
  A1=$1
  if [ "$A1" == "" ]; then
    A1="lang"
  fi

  found=0
  for f in $(ls $PROJ_DIR/com/matthewfritsch); do
    if [ $found == 0 ] && [ "$f" == "$A1" ]; then
      found=1
    fi
  done

  if [ $found == 1 ]; then
    javac $PROJ_DIR/com/matthewfritsch/$A1/*.java
  else
    echo "$A1 is not in the directory."
  fi
}
