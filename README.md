

## About The Project

### Built With


* [Scala 3](https://scala-lang.org/)
* [http4s](https://http4s.org/)
* [Circe](https://circe.github.io/circe/)
* [Firebase](https://firebase.google.com/)

## Getting Started

### Prerequisites

1. Create a Firebase project
2. Go to Project Settings -> Service Accounts
3. Generate a new private key and store the json file
4. Initiate Firestore database with 2 collections: series and movies

### Run backend

Set environmental variables:
* FIREBASE_SECRET_FILE_NAME (where the Firebase priv key is stored)
* FIREBASE_PROJECT_NAME (name of the project in Firebase)

Run this backend

  ```sh
  sbt compile
  sbt run
  ```

### Example requests

prefix with localhost:8080

* GET /series
* GET /movies

* POST /movies, with body:
```json
{
"name": "Encanto",
"year": "2021"
}
```

* POST /series, with body:
```json
{
  "name": "Sherlock",
  "seasons": [
    {"nr": 1, "releaseYear" : 2010},
    {"nr": 2, "releaseYear" : 2012},
    {"nr": 3, "releaseYear" : 2014},
    {"nr": 4, "releaseYear" : 2017}
  ]
}
```