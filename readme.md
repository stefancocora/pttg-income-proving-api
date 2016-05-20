Income Proving API
=

Overview
-

This is the Income Proving API.

Technical Notes
-

The API is implemented using Spring Boot.  It is intended to allow the team to compare and contrast with
the API previously built using Spark Java.

Building
-

Build the project using:

    gradle build

This does not run the acceptance tests, which can be run using:

    gradle acceptanceTest
    
The default build does not include API documentation. To perform a build including API documentation, use:
    
    gradle buildWithApiDocs

API Documentation
-

API documentation can be generated using:

    gradle generateApiDocs
    
The documentation in HTML and PDF format will be produced under build/asciidoc

When the jar containing API documentation is started (java -jar <name-of-jar>) then the documentation will be served at:

    http://localhost:8081/docs/index.html
    
    (or http://localhost:8081/docs/index.pdf)

