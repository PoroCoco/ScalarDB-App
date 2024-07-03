# How to Launch 

## Start cassandra

Using cassandra binaries, launch cassandra in front mode

    cd your_cassandra_folder

    bin/cassandra -f


## Start backend

    cd src/backend

    ./gradlew run


## Start frontend

    cd src/frontend

    npm i && npm run serve


## To update the database schema

First go into the backend folder then delete the current schema, and upload the updated one

    cd src/backend

    java -jar scalardb-schema-loader-<VERSION>.jar --config scalardb.properties --schema-file kuruma.json --coordinator -D

    java -jar scalardb-schema-loader-<VERSION>.jar --config scalardb.properties --schema-file kuruma.json --coordinator


