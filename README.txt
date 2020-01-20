I'm using Tomcat version 8.5

Edit WebContent/META-INF/context.xml to your own database parameters.

Extract the following into $CATALINA_BASE/lib (I should really have used Maven or something to make this easier...):
mysql-connector-java-5.1.40-bin.jar (https://dev.mysql.com/downloads/connector/j/)
Jersey JAX-RS 2.0 RI bundle (https://jersey.java.net/download.html)
jackson-annotations-2.8.0.jar
jackson-core-2.8.1.jar 
jackson-databind-2.8.5.jar
jackson-jaxrs-base-2.8.5.jar
jackson-jaxrs-json-provider-2.8.5.jar
jackson-module-jaxb-annotations-2.8.5.jar (all from https://mvnrepository.com/artifact/com.fasterxml.jackson)
jjwt-0.6.0.jar (from https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt/0.6.0) 

Hopefully that is all of them.