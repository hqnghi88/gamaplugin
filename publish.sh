#!/bin/bash
cd genstar.p2updatesite &&
mvn -U clean install -P p2Repo --settings ../settings.xml && 
cd -