#!/bin/sh
MYSELF=`which "$0" 2>/dev/null`
[ $? -gt 0 -a -f "$0" ] && MYSELF="./$0"
MYDIR=$(cd $(dirname $MYSELF) && pwd)

for x in 21/ 17/ 11/ "/"; do
  if test ! -n "$JAVA_HOME"; then
      if test -d "$MYDIR/java${x}"; then
          export JAVA_HOME="$MYDIR/java${x}"
      elif test -d "$MYDIR/../java${x}"; then
          export JAVA_HOME="$MYDIR/../java${x}"
      fi
  fi
done

#export JAVA_HOME

java=java
if test -n "$JAVA_HOME"; then
    java="$JAVA_HOME/bin/java"
fi

LOPTS=" "

_JAVA_ARGS=" --add-exports=java.base/sun.nio.ch=ALL-UNNAMED "
_JAVA_ARGS="${_JAVA_ARGS} --add-opens=java.base/java.lang=ALL-UNNAMED "
_JAVA_ARGS="${_JAVA_ARGS} --add-opens=java.base/java.lang.reflect=ALL-UNNAMED "
_JAVA_ARGS="${_JAVA_ARGS} --add-opens=java.base/java.io=ALL-UNNAMED "
_JAVA_ARGS="${_JAVA_ARGS} --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED "

export LUAY_HOME="$MYDIR/lib/luay"
export LUAY_LIB="$(cd $MYDIR/.. && pwd)/lib/luay"

if [ -f $MYSELF.jar ]; then
exec "$java" $_JAVA_ARGS -jar $MYSELF.jar ${LOPTS} "$@"
else
exec "$java" $_JAVA_ARGS -jar $MYSELF ${LOPTS} "$@"
fi
exit 1
