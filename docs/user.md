# User API Spec

## Register User
Endpoint : POST /api/users

Request Body :
```json
{
  "username" : "wildan",
  "password" : "wildan",
  "name" : "Wildan Fatahillah Akbar"
}
```

Response Body (Success) :
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "error" : "Username must no blank"
}
```

## Login User
Endpoint : POST /api/auth/login

Request Body :
```json
{
  "username" : "wildan",
  "password" : "wildan"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 2323
  }
}
```

Response Body (Failed) :
```json
{
  "error" : "Username or password wrong"
}
```

## Get User
Endpoint : GET /api/users/{username}
Endpoint : GET /api/users/current

Request Header :
- X-API-TOKEN : Token(Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "username" : "Wildan",
    "name" : "Wildan Fatahillah Akbar"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "error" : "Unauthorized"
}
```


## Update User
Endpoint : PATCH /api/auth/login

Request Header :
- X-API-TOKEN : Token(Mandatory)

Request Body :
```json
{
  "name" : "wildan",
  "password" : "wildan"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "username" : "wildan",
    "name" : "Wizzly Aw"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "error" : "Unauthorized"
}
```


## Logout User
Endpoint : DELETE /api/auth/logout

Request Header :
- X-API-TOKEN : Token(Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}

```