FROM openjdk:17-alpine

RUN apk --no-cache add curl

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

RUN chown app:app /app

USER app

EXPOSE 8080

COPY --chown=app:app target/billing-discounts-api-*.jar /app/app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/app/app.jar"]