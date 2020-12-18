# Git-Food API

## Overview

This repository includes the relevant code base used to deploy the back-end API on Heroku.

**Deployed API:** https://git-food.herokuapp.com/

To find the complimentary UI code base, its deployment instructions, and deployment location, please visit the following repository:

https://github.com/Git-Food/delivery-ui

## Relevant Project Documentation & Presentation

**Presentation:** Video linked [here](www.youtube.com)

All relevant Project documentation can be viewed in our sharepoint [Project Documentation Folder](https://northeastern.sharepoint.com/:f:/s/Fit/ElOVh2COQF1HiinugLhexDsBWroZ5DJ4_e9nemJrSCzguw?e=4v71vy). The Project Documentation Folder includes the following documents:

- Regular Meeting Notes
- Design Document
- UML Activity Diagram
- UML Class Diagram
- User Stories
- UI Wireframe

## Deployment Instructions:

1. [Clone repository](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository)
2. Decide whether to deploy to Heroku via [Heroku CLI & Git](https://devcenter.heroku.com/articles/git) or via [Github Integration](https://devcenter.heroku.com/articles/github-integration)
3. [Create an Atlas (MongoDB) account](https://docs.atlas.mongodb.com/getting-started) and setup and connect to your cluster. Make note of your username, password, and MongoDB URL
4. If you plan to run the application on your `localhost:5000`, it is best advised to set up a [local environmental variable](https://flaviocopes.com/shell-environment-variables/) so that you can connect to the database easily. The environmental variable should match the URL provided by Atlas:

   ```
   MONGODB_URI=<MongoDB URL>
   ```

   This variable needs to also be configured within Heroku to ensure the application connects to the Database correctly when depoyed. These `Config Vars` may be defined either using the Heroku CLI or directly on the Heroku website under settings.

5. Follow the necessary Heroku instructions mentioned in (2) to sucessfully deploy the application.

---

## APIs used for Git-Food Application

### Base URL: https://git-food.herokuapp.com

### Menu API

| Description | fetch menu for a particular restaurant based on menu id. |
| ----------- | -------------------------------------------------------- |
| Method      | get                                                      |
| Url         | "/menu/:id"                                              |

### Orders API

| Description | fetch orders for a specified user id. |
| ----------- | ------------------------------------- |
| Method      | get                                   |
| Url         | “/orderhistory”                       |
| Params      | { userId }                            |

### Restaurants API

| Description | fetch all restaurants available. |
| ----------- | -------------------------------- |
| Method      | get                              |
| Url         | “/restaurant”                    |

### Shopping cart by user API

| Description | fetch a shopping cart associated to a user id. |
| ----------- | ---------------------------------------------- |
| Method      | get                                            |
| Url         | “/shoppingcartbyuser”                          |
| Params      | { userid }                                     |

### Add order item to shopping cart API

| Description | put and order item into the shopping cart.  |
| ----------- | ------------------------------------------- |
| Method      | put                                         |
| Url         | “/addorderitem”                             |
| Params      | { userid, menuitem, specialnote, quantity } |

### Increment order item quantity on shopping cart API

| Description | increment quantity of a particular order item already in the shopping cart. |
| ----------- | --------------------------------------------------------------------------- |
| Method      | put                                                                         |
| Url         | “/incrementorderitem”                                                       |
| Params      | { userid, orderitem }                                                       |

### Decrement order item quantity on shopping cart API

| Description | decrement quantity of a particular order item already in the shopping cart. |
| ----------- | --------------------------------------------------------------------------- |
| Method      | put                                                                         |
| Url         | “/decrementorderitem”                                                       |
| Params      | { userid, orderitem }                                                       |

### Clear shopping cart API

| Description | Remove all items from a shopping cart. |
| ----------- | -------------------------------------- |
| Method      | put                                    |
| Url         | “/clearshoppingcart”                   |
| Params      | { userid }                             |

### Checkout API

| Description | Executes checkout process. Clear shopping cart and create and Order. |
| ----------- | -------------------------------------------------------------------- |
| Method      | post                                                                 |
| Url         | “/checkout”                                                          |
| Params      | { userid }                                                           |

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
