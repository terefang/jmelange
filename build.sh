#!/usr/bin/env bash
bDATE=$(date '+%Y%m%d%H%M%S')
yDATE=$(date '+%Y')
mDATE=$(date '+%-m')
bDIR=$(dirname $0)
bDIR=$(cd $bDIR && pwd)

#OPTS="-DskipTests=true -DproxySet=true -DproxyHost=127.0.0.1 -DproxyPort=3128 -Dhttps.nonProxyHosts=127.0.0.1"
OPTS="-DskipTests=true"

while test ! -z "$1" ; do
  case "$1" in
    -setversion*)
          VALUE="${2}"
          (cd $bDIR && mvn build-helper:parse-version versions:set \
                    -DnewVersion="${VALUE}" ) || exit 1
          shift
          ;;
    -release*)
      (cd $bDIR && mvn -X build-helper:parse-version versions:set \
                -DnewVersion="\${parsedVersion.majorVersion}.\${parsedVersion.minorVersion}.\${parsedVersion.nextIncrementalVersion}" ) || exit 1
      ;;
    -drel*)
      (cd $bDIR && mvn -X build-helper:parse-version versions:set \
                -DnewVersion="${yDATE}.${mDATE}.\${parsedVersion.nextIncrementalVersion}" ) || exit 1
      ;;
    -clean*)
      (cd $bDIR && mvn clean $OPTS -U) || exit 1
      ;;
    -pack*)
      (cd $bDIR && mvn clean package $OPTS -U) || exit 1
      ;;
    -fix*)
      (cd $bDIR && mvn -N versions:update-child-modules) || exit 1
      ;;
    -install*)
      (cd $bDIR && mvn install $OPTS) || exit 1
      ;;
    -copy*)
      cp $bDIR/script-cli/target/script-cli.sh.jar ~/bin/script-cli.sh.jar
      cp $bDIR/pdfml-jmelange/target/pmltopdf.bin ~/bin/pmltopdf-cli.sh
      ;;
    -deps)
      (cd $bDIR && mvn org.apache.maven.plugins:maven-dependency-plugin:tree) || exit 1
      ;;
    *) echo "unknown option ..."
      ;;
  esac
  shift
done