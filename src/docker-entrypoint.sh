#!/bin/bash -x
java -Xms1024m -Xmx2048m -Duser.timezone=Europe/Kiev -Dconfig.properties=/opt/srv/conf/config.properties -jar /opt/srv/pattern.project2-1.1.8.jar
