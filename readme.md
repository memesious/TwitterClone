
# Twitter Clone

This is a Twitter clone project built using Groovy. It aims to replicate some of the core functionalities and features of the popular social media platform Twitter.

## Table of Contents

* Features
* Installation



## Features
User registration
Edit profile
Delete profile
Subscribe to other users

Make, edit, delete posts
Like/unlike posts
View feed (own posts + posts of people in subscriptions)

Comment posts
View comments

## Installation
1. Clone the repository
`   git clone https://github.com/memesious/TwitterClone.git
`
2. Navigate to the project directory:
   `cd TwitterClone `
3. Run all the additional software:
   (If you have x64 processor, open [compose.yaml](compose.yaml) file, uncomment x64 images of
keycloak and mongodb and comment the arm images)
`docker-compose up -d
`
4. Start the application using your IDE

## Usage
Send POST request to the `localhost:8081/users/register` url with body like that:

`{
"firstName": "John",
"lastName": "Doe",
"username": "johndoe",
"email": "johndoe@example.com",
"age": 30,
"password": "Secretpassword"
}`
This will register your user

To get the token for other requests you can open http://localhost:8080/auth/realms/TwitterClone/account/#/
in your browser, log in with your credentials, open browser's network tab, reload the page and view the response for token request

