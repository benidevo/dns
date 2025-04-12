run:
	sh run.sh

test:
	codecrafters test

format:
    mvn spotless:apply

compile:
	mvn clean compile

package:
	mvn package

install:
	mvn install

compile:
	mvn clean compile
