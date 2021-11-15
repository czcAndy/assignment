# assignment

Steps to run the project:
1. Import the project in Intellij
2. Run the Mockoon Web server and set the port to 1337. Alternatively, you can change the port from the 'client' side, from the application.properties file.
3. There is a Cloud Mongo DB to which the application connects automatically. This is done to avoid complexity (not the best idea)
4. Navigate to http://localhost:8080/playground to run the graphQl queries.

At this moment the MongoDB database is empty.

Notes:
At the moment of this commit there are 2 major performance issues that were not yet tackled:
1. N+1 problem in Graphql
2. The Rest calls for fetching the services in the context of the 3rd challenge (Return all vehicles where the specified service has the specified status) could 
have been called from inside a Flux container to improve performance. 