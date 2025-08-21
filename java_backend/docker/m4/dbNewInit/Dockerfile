FROM ubuntu:20.04

WORKDIR /

COPY initServer.sh .
RUN tr -d '\r' < ./initServer.sh > ./tmp.sh && mv ./tmp.sh ./initServer.sh && chmod +x ./initServer.sh

RUN apt-get update && apt-get install -y software-properties-common wget curl
RUN curl https://packages.microsoft.com/keys/microsoft.asc | tee /etc/apt/trusted.gpg.d/microsoft.asc 
RUN add-apt-repository "$(wget -qO- https://packages.microsoft.com/config/ubuntu/20.04/prod.list)"
RUN apt-get update && apt-get install -y sqlcmd

CMD ./initServer.sh
