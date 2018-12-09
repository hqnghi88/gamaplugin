#!/bin/bash

cd genstar.p2updatesite &&
mvn -U clean install -P p2Repo --settings ../settings.xml && 
cd - && bash ./pushToBintray.sh hqnghi88 $BINTRAY gama_genstar genstar.plugin.bundle-all.feature 1.0.0 genstar.p2updatesite\target\