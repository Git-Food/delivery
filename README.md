# Git-Food API

## Overview
This repository includes the relevant code base used to deploy the back-end API on Heroku.

**Deployed API:** https://git-food.herokuapp.com/

To find the complimentary UI code base, its deployment instructions, and deployment location, please visit the following repository:

https://github.com/Git-Food/delivery-ui

## Relevant Project Documentation & Presentation

**Presentation:** Video linked [here](www.youtube.com)

All relevant Project documentation can be viewed in our sharepoint [Project Documentation Folder](https://northeastern.sharepoint.com/:f:/s/Fit/ElOVh2COQF1HiinugLhexDsBWroZ5DJ4_e9nemJrSCzguw?e=gizhMW). The Project Documentation Folder includes the following documents:
* Regular Meeting Notes
* Design Document
* UML Activity Diagram
* UML Class Diagram
* User Stories
* UI Wireframe

## Deployment Instructions:
1) [Clone repository](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
2) Decide whether to deploy to Heroku via [Heroku CLI & Git](https://devcenter.heroku.com/articles/git) or via [Github Integration](https://devcenter.heroku.com/articles/github-integration)
3) [Create an Atlas (MongoDB) account](https://docs.atlas.mongodb.com/getting-started) and setup and connect to your cluster. Make note of your username, password, and MongoDB URL
4) If you plan to run the application on your `localhost:5000`, it is best advised to set up a [local environmental variable](https://flaviocopes.com/shell-environment-variables/) so that you can connect to the database easily. The environmental variable should match the URL provided by Atlas:
    ```
    MONGODB_URI=<MongoDB URL>
    ```

    This variable needs to also be configured within Heroku to ensure the application connects to the Database correctly when depoyed. These `Config Vars` may be defined either using the Heroku CLI or directly on the Heroku website under settings.

5) Follow the necessary Heroku instructions mentioned in (2) to sucessfully deploy the application.

---

## Requirements

A recent Gradle (>= 6.1.1 but < 7.0.0) and JDK 8.

## Building

`./gradlew build`

## Testing

`./gradlew test`

## Testing JaCoCo

`./gradlew jacocoTestReport`

## Running

`./gradlew run`

The server will start on port 5000 by default.

## Deploying to Heroku

Configure as normal and use `git push heroku main`.

`./gradlew build deployHeroku` works to deploy without pushes...sometimes.

## Spotless?

Spotless automatically formats code. If it detects errors, run `./gradlew spotlessApply`
to automatically fix them. `./gradlew spotlessCheck` can be used to directly invoke
Spotless.
