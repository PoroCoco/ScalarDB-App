# How to Launch 

## Start cassandra

Using cassandra binaries, launch cassandra in front mode

    cd your_cassandra_folder

    bin/cassandra -f

## CLI-Mode 

    cd src/backend

    ./gradlew cli

## Web interface

### Start backend

    cd src/backend

    ./gradlew server


### Start frontend

    cd src/frontend

    npm i && npm run serve


## To update the database schema

First go into the backend folder then delete the current schema, and upload the updated one

    cd src/backend

    java -jar scalardb-schema-loader-<VERSION>.jar --config scalardb.properties --schema-file kuruma.json --coordinator -D

    java -jar scalardb-schema-loader-<VERSION>.jar --config scalardb.properties --schema-file kuruma.json --coordinator


