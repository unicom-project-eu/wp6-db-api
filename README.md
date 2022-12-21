### LAUNCH APP
Install maven dependencies and create jar file
* ```mvn install```
* ```mvn package```

Run postgres

* for develop ```docker-compose -f docker-compose-dev.yml up -d postgres```
* for production ```docker-compose up -d```

Run application for develop
* ```mvn springboot:run``` or ```java -jar target/unicom-backend.jar``` <br>

You can try the api from [localhost:8080/fhir/swagger-ui/](localhost:8080/fhir/swagger-ui/)<br>
**NOTE**: make sure to include the last "/"


### DATA IMPORT
#### JSON FILE GENERATION
To import data you need a json file that validates against the json schema at src/main/resources/public.schema.import.v<...>
or http://localhost:8080/schemas/import/v<...>/index.html (for example http://localhost:8080/schemas/import/v0.1.0/index.html)

To generate the json you need a csv file formatted according to https://docs.google.com/document/d/1xlXlbF5wgZeUkFfVULoq4aa5coTO91QIEJnw9ucvxpU/edit?usp=sharing

Just install the requirements in preprocess-data/beglium_subset, copy the csv file in preprocess-data/belgium_subset and launch
```
cd /preprocess-data/belgium_subset
python3 preprocess.py -c > myjson.json
```
This will save the json file to myjson.json. If the csv has error the script will print them in stderr.
If you want to save the errors in a file just launch
```
python3 preprocess.py > myjson.json 2> conflicts.json
```
This will save the json file to myjson.json and the errors in conflicts.json

#### DEVELOPMENT DATA IMPORT
Launch
```java -jar target/unicom-backend.jar -i path/to/myjson.json```

#### PRODUCTION DATA IMPORT
Launch
```data-import.sh```

##### ISSUES RELATED TO DATA IMPORT
For production data import, the path to json file is hard-coded. We should improve this by using command line parameter instead
For json file generation, we should improve the folder structure and avoid to repeat the csvutils library
