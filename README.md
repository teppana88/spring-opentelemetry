# Table of Contents
- [Summary](#1-summary)
- [App Configuration](#2-app-configuration)
- [Otel Collector Configuration](#3-otel-collector-configuration)
    - [Receivers](#31-receivers)
    - [Processors](#32-processors)
    - [Exporters](#33-exporters)
    - [Extensions](#34-extensions)
    - [Service](#35-service)
- [Observability Tools Configuration](#4-observability-tools-configuration)
    - [Prometheus Configuration](#41-prometheus-configuration)
    - [Grafana Tempo Configuration](#42-grafana-tempo-configuration)
    - [Grafana Loki Configuration](#43-grafana-loki-configuration)
    - [Grafana Configuration](#44-grafana-configuration)

# 1. Summary
This project is providing a simple Spring Boot app with observability implementation.
The main focus is to provide a simple example of how to implement observability in a Spring Boot app.
The app uses Spring Boot 3.x version, Kotlin and JDK 21.

The Spring Boot app is using following components:
- OpenTelemetry Spring Boot Starter
- Spring Boot Actuator (including Micrometer)
- Micrometer tracing bridge otel

We use the [OpenTelemetry Spring Boot Starter](https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/) to provide tracing and metrics capabilities to the app.
This is equivalent to the OpenTelemetry Java agent but makes Spring Boot app setup simpler when OpenTelemetry is part of the project build.
```
dependencies {
    ...
	// These are opentelemetry dependencies
	// For Spring Boot native - OpenTelemetry core instrumentation for Spring Boot
	implementation(platform(SpringBootPlugin.BOM_COORDINATES))
	implementation(platform("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.10.0"))
	implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:2.10.0")

	// Enable metrics
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// enable tracing bridge so that traceID is propagated
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
    ...
}
```

The project contains also a Docker compose part where we have implementation of observability tools:
- Otel Collector
- Prometheus
- Grafana
- Grafana Tempo
- Grafana Loki

Make sure that you have Docker running and the you can spin up the observability tools by running the following command:
```
docker-compose up -d
```

Once you have the observability tools up and running you can start the Spring Boot app, you can access the following URLs:
```
http://localhost:8080/post/2
```
This will trigger a call to app and you will have metrics, traces and logs generated into Grafana.

# 2. App configuration
The observability configs are done in application.properties file. Check the following sets:
1. OpenTelemetry configuration
2. Sampling configuration
3. Provide list of metrics you want to enable
4. Provide tags for metrics

Update these based on your needs. Current setup provides a sample setup to get you started.

# 3. Otel Collector configuration
The Otel Collector is the central component to receive, process and export metrics, traces and logs.
The configuration is done in otel-collector.yaml file.
> Note: Otel Collector contrib is having [releases](https://github.com/open-telemetry/opentelemetry-collector-contrib) quite often and in some cases the configuration might change.
> Make sure to check the latest [Collector documentation](https://opentelemetry.io/docs/collector/) for the configuration if you use a different version than in the project.

## 3.1 Receivers
The Collector is configured to receive traces and metrics from the Spring Boot app via OTLP (OpenTelemetry Spring Boot Starter).

## 3.2 Processors
There two processors configured:
- batch => This is used to batch the traces and metrics before exporting them.
- memory_limiter => This is used to limit the memory usage of the Collector.

## 3.3 Exporters
There are three exporters configured:
- prometheus => This is used to export metrics to Prometheus.
- otlp/tempo => This is used to export traces to Grafana Tempo.
- loki => This is used to export logs to Grafana Loki.

> Note: config includes a sample config for [Splunk HEC metrics exporter](https://github.com/open-telemetry/opentelemetry-collector-contrib/tree/main/exporter/splunkhecexporter). This is not enabled.
> If you want to try this you need to set up Splunk server and provided needed config for Otel Collector + enable metrics exporter for splunk_hec/metrics.

## 3.4 Extensions
There are 3 extensions configured:
- health_check => This is used to provide health check for the Collector.
- zpages => This is used to provide zpages for the Collector.
- pprof => This is used to provide pprof for the Collector.

## 3.5 Service
- pipelines => This is used to define the pipelines for traces, metrics and logs.
- extensions => This is used to define the extensions for the Collector.
- telemetry => This is used to define the telemetry for the Collector itself (sends metrics data back to collector receiver)

# 4. Observability tools configuration
## 4.1 Prometheus configuration
Prometheus is configured to scrape metrics from the Collector.
The configuration is done in prometheus.yml file. Here you define e.g. scrape interval etc.

## 4.2. Grafana Tempo configuration
The configuration is done in tempo.yaml file and includes a basic set.

## 4.3. Grafana Loki configuration
This component does not additional configuration.

## 4.4 Grafana configuration
The configuration is done in grafana-datasources.yaml file and this basically includes 
configs to set up data sources (Prometheus, Tempo, Loki) for Grafana. Without this Grafana would not have data srouces defined when you spin up container and you would need to setup these manually.


