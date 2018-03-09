#!/bin/bash
if type -p java; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    _java="$JAVA_HOME/bin/java"
else
    echo "Java is not installed on your system. Please install Java to run the game."
fi
if [[ "$_java" ]]; then
   JAVA_VER=$(java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*"/\1\2/p;')
   [ "$JAVA_VER" -ge 18 ] && java -jar BlackJack.jar || echo "You need java 8 to play this game."
fi
