FROM ubuntu:14.04

RUN apt-get update && \
	sudo apt-get install -y software-properties-common && \
	sudo add-apt-repository ppa:webupd8team/java -y && \
	sudo add-apt-repository -y ppa:git-core/ppa && \
	sudo apt-get install -y curl && \
	curl -sL https://deb.nodesource.com/setup_6.x | sudo -E bash - && \
	apt-get update && \
	echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
	apt-get install -y nodejs && \
	sudo npm install webpack -g && \
	apt-get install -y oracle-java8-installer && \
	apt install -y maven && \
	apt-get clean

COPY . /app/karma
WORKDIR /app/karma/server

RUN mvn compile 
RUN mvn compile assembly:single

WORKDIR /
WORKDIR /app/karma/client 

RUN npm install
RUN webpack


RUN chmod +x /app/karma/server/target/server-0.0.1-jar-with-dependencies.jar 
CMD ["java" , "-jar", "/app/karma/server/target/server-0.0.1-jar-with-dependencies.jar"]
