# Libraries used in the program:
* Spring Boot - Used for Rest API, Dependency Injections.
* Google Guava - For storing transactions in a Multimap. 
#####NOTE: Spring also provide multimap but it has a multimap which stores a linkedlist against a key.
#####Here, we do not need any link between the transactions.
#####Google Guava provides multimap having arraylist as value of any key. So, is better for current requirement.

# Assumptions:
* Sum and Average stored in Statistics are rounded to two decimals.

# Design:
* All the transaction are stored in MultiMap with transaction timestamp as key.
* This map will only contain the transactions based upon the provided expiry time. At the moment, it is 60 secs.
* Once stored, a signal is sent to calculate the statistics of transactions.
* The thread calculating the statistics takes all the values from transaction multimap and calculates.
* Removal of keys from the transaction multimap is achieved via a delay queue having the transaction timestamps with expiry of 60 seconds.
* Whenever any key is removed, a signal is sent to calculate the statistics.

# Steps to build and start the program:
* Browse to the project's folder.
* mvn -U clean package
* java -jar target/backend-challenge-286-1.0.0-SNAPSHOT.jar
