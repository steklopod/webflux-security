# Spring Webflux Security with 🚀 `Kotlin` Coroutines
## [![Minimal CI/CD](https://github.com/steklopod/webflux-security/actions/workflows/test.yml/badge.svg)](https://github.com/steklopod/webflux-security/actions/workflows/test.yml)

Example of how to make a custom security configuration based in JWT with Kotlin coroutines.

## 1. Stack

* Kotlin (Java `17`)
* Spring Boot (Webflux, Security)
* Gradle
* Reactive `MongoDB` (through docker)
* Vue.js (very simple example)

## 2. Project Structure

The project consists in two maven modules:

* `backend` (Spring app)
* `frontend` (Vue.js app)

And in two more folders:

* `.github` (CI/CD)
* `mongo` (MongoDB scripts to initialize the database. They )

## 3. Security

Spring Security Webflux (like his brother the Servlet version) it's all about filters, these filters are composed one after the other 
forming a chain. Every [ServerWebExchange](https://github.com/spring-projects/spring-framework/blob/master/spring-web/src/main/java/org/springframework/web/server/ServerWebExchange.java) (an exchange 
it's commonly known as an object that holds request and response, this concept exists in other places like undertow web server) has 
to go through this chain. Spring Security Webflux allow us to configure the chain ([SecurityWebFilterChain](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/SecurityWebFilterChain.java)) as we need and even 
give us the possibility to have more than one per path. 
The filter chain for Spring Security Webflux has the following order:

     +---------------------------+
     |                           |
     | HttpHeaderWriterWebFilter | (1) default
     |                           |
     +-----------+---------------+
                 |
                 |
                 |
     +-----------v------------+
     |                        |
     | HttpsRedirectWebFilter | (2) configurable
     |                        |
     +-----------+------------+
                 |
                 |
                 |
         +-------v-------+
         |               |
         | CorsWebFilter | (3) configurable
         |               |
         +-------+-------+
                 |
                 |
                 |
         +-------v-------+
         |               |
         | CsrfWebFilter | (4) default
         |               |
         +-------+-------+
                 |
                 |
                 |
    +------------v------------+
    |                         |
    | ReactorContextWebFilter | (5) default
    |                         |
    +------------+------------+
                 |
                 |
                 |
    +------------v------------+
    |                         |
    | AuthenticationWebFilter | (6) default
    |                         |
    +------------+------------+
                 |
                 |
                 |
    +------------v------------------------------+
    |                                           |
    | SecurityContextServerWebExchangeWebFilter | (7) default
    |                                           |
    +------------+------------------------------+
                 |
                 |
                 |
       +---------v-------------------+
       |                             |
       | ServerRequestCacheWebFilter | (8) default
       |                             |
       +---------+-------------------+
                 |
                 |
                 |
       +---------v-------+
       |                 |
       | LogoutWebFilter | (9) configurable
       |                 |
       +---------+-------+
                 |
                 |
                 |
    +------------v------------------+
    |                               |
    | ExceptionTranslationWebFilter | (10) default
    |                               |
    +------------+------------------+
                 |
                 |
                 |
    +------------v------------+
    |                         |
    |  AuthorizationWebFilter | (11) default
    |                         |
    +-------------------------+
    
    - Figure 1. Unless otherwise specified, filters with 'default' word are added by Spring Security -

## 4. Authentication

1. We want to authenticate users through a POST to `/login` endpoint, our matcher looks at the request and see if this pattern match. We can use the factory method `pathMatchers()` that [ServerWebExchangeMatchers](/backend/src/main/kotlin/com/popokis/backend_server/application/WebConfig.kt#L71) provides 
to create our custom matcher. 
2. Our converter gets from the body a JSON with `username` and `password` attributes and creates an unauthenticated Authentication object with them. Done by [JWTConverter](/backend/src/main/kotlin/com/popokis/backend_server/application/security/authentication/JWTConverter.kt). 
3. [AbstractUserDetailsReactiveAuthenticationManager](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AbstractUserDetailsReactiveAuthenticationManager.java#L98) gets the principal (username) and the credentials (password) from the Authentication object created in step 2 and: 
    1. [AbstractUserDetailsReactiveAuthenticationManager](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AbstractUserDetailsReactiveAuthenticationManager.java#L100) looks into the database if the user exist with [CustomerReactiveUserDetailsService](/backend/src/main/kotlin/com/popokis/backend_server/application/security/authentication/CustomerReactiveUserDetailsService.kt), if exists go to step 3.ii, otherwise throw BadCredentialsException and executes ServerAuthenticationFailureHandler (step 5). 
    2. [AbstractUserDetailsReactiveAuthenticationManager](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/authentication/AbstractUserDetailsReactiveAuthenticationManager.java#L103) checks if passwords match, if so authentication success, if not throw BadCredentialsException and executes ServerAuthenticationFailureHandler (step 5).
4. On authentication success:
    1. Our project is just an HTTP API and by default should be stateless, then we don't want to create a session so skip it. Done by [NoOpServerSecurityContextRepository](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/context/NoOpServerSecurityContextRepository.java).
    2. Execute our [JWTServerAuthenticationSuccessHandler](/backend/src/main/kotlin/com/popokis/backend_server/application/security/authentication/JWTServerAuthenticationSuccessHandler.kt) that generates an access and a refresh token and put them in the header of the response.
5. On authentication error:
    1. Return unauthorized error. Done by [JWTServerAuthenticationFailureHandler](/backend/src/main/kotlin/com/popokis/backend_server/application/JWTServerAuthenticationFailureHandler.kt).

## 5. Authorization


If you look at the code, you can see that we are not using the Authentication object for nothing, this is because we are not creating any session in the 
server, looking at the [AuthorizationWebFilter](https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/server/authorization/AuthorizationWebFilter.java#L46) 
we can see that is using the security context to get the Authentication from there but we disabled sessions so there is no Authentication object, we have to authorize from 
the request that is inside the AuthorizationContext.

To authorize admins, we check if the role that comes from the token in the request has the same role of the required, `ROLE_ADMIN` in this case.

To authorize the rest, we are checking the validity of the JWT that comes in every request. The validity for us is:

1. If the token is expired. 
2. If token was given by us (is signed with our signature).

These checks are provided by our JWT library. 

# 🌞 Build project

1. Build `frontend`

```shell
cd frontend
npm i
npm build
```

2. Build & Test `backend` (from root folder)

```shell
./gradlew build
./gradlew copyFront
```

3. Run
```shell
./gradlew bootRun
```

### Try it

[http://localhost:3000](http://localhost:3000)

Go to `Customers` menu section and try to login with:

`user@example.com` : `userPassword`
