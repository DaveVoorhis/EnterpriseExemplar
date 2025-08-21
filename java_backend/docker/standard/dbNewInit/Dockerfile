FROM mcr.microsoft.com/mssql-tools

WORKDIR /

COPY initServer.sh .
RUN tr -d '\r' < ./initServer.sh > ./tmp.sh && mv ./tmp.sh ./initServer.sh && chmod +x ./initServer.sh

CMD ./initServer.sh
