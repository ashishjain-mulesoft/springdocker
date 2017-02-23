FROM ashishjain07/springdocker:latest
MAINTAINER ashish jain "ashish.jain@weightwatchers.com"
VOLUME /tmp
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
RUN -p 8080:8080 -t ashishjain07/springdocker:latest