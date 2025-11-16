## config service
```sh
# study service
finish to do list in application.yml
```
## config database follow application.yml
```sh
config db_name = ktpm_study
```

## run system 
```sh
# run study service
# java 21 LTS, mvn 
cd study 
mvn spring-boot:run

# run by docker
cd study

# build
build -t study-backend:v1 .

# todo fill suitable info & run 
docker run -p 8081:8081 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/database_name -e SPRING_DATASOURCE_USERNAME=your_user_name -e SPRING_DATASOURCE_PASSWORD=your_pass_word study-backend:v1 
```
## api document
```sh
# install postman & import port man collection by json
ktpm_study.postman_collection.json
```