@echo off

echo highmed/bpe ...
docker build -t highmed/bpe ..\dsf-bpe\dsf-bpe-server-jetty\docker

echo highmed/bpe_proxy ...
docker build -t highmed/bpe_proxy ..\dsf-docker\bpe_proxy

echo highmed/fhir ...
docker build -t highmed/fhir ..\dsf-fhir\dsf-fhir-server-jetty\docker

echo highmed/fhir_proxy ...
docker build -t highmed/fhir_proxy ..\dsf-docker\fhir_proxy
