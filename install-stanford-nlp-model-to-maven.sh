#!/bin/bash

echo
echo ---
echo Please ensure that Maven/mvn is on command path
echo ---
echo 

curl -O http://nlp.stanford.edu/software/stanford-corenlp-full-2014-01-04.zip
unzip stanford-corenlp-full-2014-01-04.zip
cd stanford-corenlp-full-2014-01-04
mvn install:install-file -Dfile=./stanford-corenlp-3.3.1-models.jar -DgroupId=edu.stanford.nlp -DartifactId=stanford-corenlp-models -Dversion=3.3.1 -Dpackaging=ja
cd ..
rm -rf stanford-corenlp-full-2014-01-04
rm stanford-corenlp-full-2014-01-04.zip
