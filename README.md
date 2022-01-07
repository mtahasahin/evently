<div id="top"></div>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->




<!-- PROJECT LOGO -->
<br />
<div align="center">

<h3 align="center">Evently</h3>

  <p align="center">
    Evently is a hobby project I've been developing to improve my web developer skills and learn new technologies. It is built with Spring Boot and Nextjs. It is heavily inspired by <a href="https://kommunity.com">Kommunity</a>.
    <br />
    <br />
    <a href="https://evently-demo.vercel.app">View Demo</a>
  </p>
</div>



<hr/>
<div align="center">
<h3 align="center">🚧🚧🚧WORK IN PROGRESS🚧🚧🚧</h3>
Project is not yet completed. You might get 404 for some pages.
</div>
<hr/>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

![Evently Screen Shot][product-screenshot]

Evently is an event platform to organize events and communicate with your members within one place.

<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Hibernate](https://hibernate.org/)
* [Liquibase](https://www.liquibase.org/)
* [PostgreSQL](https://www.postgresql.org/)
* [TestContainers](https://www.testcontainers.org/)
* [Elasticsearch](https://www.elastic.co/)
* [Next.js](https://nextjs.org/)
* [Tailwind](https://tailwindcss.com/)
* [Amazon S3](https://aws.amazon.com/s3/)



<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### 1st way - Using docker-compose
The easiest way to get the project up and running is to use docker-compose.

#### Prerequisites
- Docker and docker-compose

#### Installation

1. Clone the repo `git clone https://github.com/mtahasahin/evently.git`
2. cd into the project directory `cd evently`
3. Run `docker-compose up`
4. Browse `http://localhost:3000` and enjoy!

Note: If you want to use AWS S3 and/or Google/Facebook Login, follow the instructions in the optional sections below.

<p align="right">(<a href="#top">back to top</a>)</p>

### 2nd way - Without docker-compose
If you don't want to use docker-compose you need to manually install the dependencies and have a postgres
and elasticsearch instance running.

#### Prerequisites
- JDK 11
- Postgres 14.1
- Elasticsearch 7.10
- Npm 8.x

#### Installation

1. Clone the repo `git clone https://github.com/mtahasahin/evently.git`
2. Open `application.yml` file at `evently/backend/src/main/resources/` and fill in the following values:
```yaml
spring:
  datasource:
    url: #jdbc url of your postgres database
    username: #username of your postgres database
    password: #password of your postgres database

jpa:
  properties:
    hibernate:
      search:
        backend:
          uris: #url of your elasticseach instance (including http://)

app:
  elasticsearch:
    host: #url of your elasticseach instance (without http://)
```
3. Run `./mvnw spring-boot:run` at `evently/backend` directory
4. Go to `evently/frontend` directory and run `npm install`
5. Run `npm run build` and then `npm run start`
6. Browse `http://localhost:3000` and enjoy!

<p align="right">(<a href="#top">back to top</a>)</p>


### Optional - Store images in AWS S3 bucket
By default files are stored in the local file system. 
If you want to store images in an AWS S3 bucket, you need to follow the instructions below.

1. Create an S3 bucket with public read access.
2. Open `application.yml` file at `evently/backend/src/main/resources/` and fill in the following values:
```yaml
cloud:
  aws:
    region:
      static: #s3 region
    stack:
      auto: false
    credentials:
      access-key: #aws access key
      secret-key: #aws secret key

app:
  bucket:
    name: #s3 bucket name
```
- If you are using Docker-compose, open `docker-compose.yml` file and uncomment the following line:
```yaml
  #- "SPRING_PROFILES_ACTIVE=s3" #uncomment this to use s3 bucket for storage
```
- If you are using maven, start the project with `./mvnw spring-boot:run -Dspring-boot.run.profiles=s3` command.


### Optional - Google/Facebook Login
If you want to use Google/Facebook Login, you need to follow the instructions below.
1. Create a Google/Facebook app and get the client id and client secret.
2. Open `application.yml` file at `evently/backend/src/main/resources/` and fill in the following values:
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            clientId:  # your google client id
            clientSecret:  # your google client secret
            redirectUri: "{baseUrl}/oauth2/callback/{registrationId}"
            scope:
              - email
              - profile
          facebook:
            clientId:  # your facebook client id
            clientSecret:  # your facebook client secret
            redirectUri: "{baseUrl}/oauth2/callback/{registrationId}" # Note that facebook now mandates the use of https redirect URIs, so make sure your app supports https in production
            scope:
              - email
              - public_profile
```
<!-- ROADMAP -->
## Roadmap

### Features

- [X] Social Logins
    - [X] Google
    - [X] Facebook
- [X] Explore events page
- [X] Event attendees page
- [ ] Forgot password page

### Other
- [X] Add installation instructions to Readme
- [X] Increase unit test coverage
- [X] Add Integration Tests with Testcontainers
- [ ] Refactor frontend

<p align="right">(<a href="#top">back to top</a>)</p>




<!-- CONTACT -->
## Contact

Mustafa Taha Şahin - mustafatahasahin@gmail.com

Project Link: [https://github.com/mtahasahin/evently](https://github.com/mtahasahin/evently)

<p align="right">(<a href="#top">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[product-screenshot]: images/screenshot.png
