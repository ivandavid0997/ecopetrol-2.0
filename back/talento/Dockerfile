FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY build/libs/ms-talento-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /opt/apm && \
    wget -q -O /opt/apm/elastic-apm-agent.jar \
https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.55.6/elastic-apm-agent-1.55.6.jar

EXPOSE 8080
ENTRYPOINT ["java", \
  "-javaagent:/opt/apm/elastic-apm-agent.jar", \
  "-Delastic.apm.service_name=ms-talento", \
  "-Delastic.apm.server_url=http://48.214.147.7:8200", \
  "-Delastic.apm.environment=prod", \
  "-Delastic.apm.application_packages=com.nttdata.ecopetrol.talento", \
  "-jar", "app.jar"]