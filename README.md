# Developer Notes

## Watch the REST API from Powershell

`while (1) { http get localhost:8080/api/alarms; sleep 5; cls;}`

## HTTPie

`http post localhost:8080/api/alarms name="web.benhunter.me" target="http://web.benhunter.me" webhook="REPLACE_ME"`

## Build and run on Windows with Powershell

Temporarily override $JAVA_HOME to use Java 17.
Run the .jar

```powershell
$env:JAVA_HOME = 'C:\Program Files\OpenJDK\jdk-17';
./gradlew build;
& ("$env:JAVA_HOME" + "bin\java") -jar "$((Get-Item .).FullName)\build\libs\solo-mvp-server-0.0.1-SNAPSHOT.jar";
```

## Build the Docker image with Spring Boot :bootBuildImage

Uses Buildpack in Docker, making a layered image for efficient builds and runs.

- https://spring.io/guides/gs/spring-boot-docker/
- https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1
- Image is tagged with the version specified in `build.gradle`
- (Note: we are not using the `Dockerfile` for the server that is in the same folder as the README.md)
 
`./gradlew bootBuildImage` - Basic command.

`$env:JAVA_HOME = 'C:\Program Files\OpenJDK\jdk-17';./gradlew bootBuildImage --imageName=benhunter/solo-mvp-server` - Set an environment variable in Powershell, then build for a specific image name.

`docker push benhunter/solo-mvp-server` - Push after building the image locally. If you aren't logged into Docker Hub, do `docker login` first.


## Docker Compose to run the full stack application

Uses `docker-compose.yml`.

`$env:ENV_DATABASE_PASS=""` - Set environment variables.

`docker-compose pull` - Get the latest images.

Start the services manually so the database has time to come up. The server does not have a delay before it connects to the database.

`docker-compose up database` - Make sure the database is ready to accept connections

`docker-compose up` - Bring everything up
