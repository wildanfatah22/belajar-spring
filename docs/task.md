# Task API Spec

## Create Task
Endpoint : POST /api/tasks

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
//  "username": "wildan",
  "title": "New Task",
  "description": "Task description",
  "status": true,
  "startDate": "2024-10-13",
  "endDate": "2024-10-14"
}
```

Response Body (Success) :
```json
{
  "data": {
    "taskId": 1,
    "username": "wildan",
    "title": "New Task",
    "description": "Task description",
    "status": true,
    "createdAt": "2024-10-12",
    "updateAt": "2024-10-12",
    "startDate": "2024-10-13",
    "endDate": "2024-10-14"
  }
}
```

Response Body (Failed) :
```json
{
  "error" : "Start date invalid, description invalid"
}
```

## Update Task
Endpoint : PUT /api/tasks/{taskId}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Request Body :
```json
{
  "username": "wildan",
  "title": "Updated Task",
  "description": "Updated description",
  "status": false,
  "startDate": "2024-10-15",
  "endDate": "2024-10-16"
}

```

Response Body (Success) :
```json
{
  "data": {
    "taskId": 1,
    "username": "wildan",
    "title": "Updated Task",
    "description": "Updated description",
    "status": false,
    "createdAt": "2024-10-12",
    "updateAt": "2024-10-15",
    "startDate": "2024-10-15",
    "endDate": "2024-10-16"
    }
}
```

Response Body (Failed) :
```json
{
  "error" : "Start date invalid, description invalid"
}
```

## Get Task
Endpoint : GET /api/tasks

Request Header :
- X-API-TOKEN : Token (Mandatory)


Response Body (Success) :
```json
{
  "data": {
    "taskId": 1,
    "username": "wildan",
    "title": "Updated Task",
    "description": "Updated description",
    "status": false,
    "createdAt": "2024-10-12",
    "updateAt": "2024-10-15",
    "startDate": "2024-10-15",
    "endDate": "2024-10-16"
    }
}
```

Response Body (Failed, 404) :
```json
{
  "error" : "Task not found"
}
```

## Search Task
Endpoint : POST /api/tasks

Query Param :
- title : String, title task, using like query
- start date : Date, startDate, using like query
- end date : Date, endDate
- status : Boolean, status
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :
- X-API-TOKEN : Token(Mandatory)

Response Body (Success) :
```json
{
  "data" : [
    {
      "taskId": 1,
      "username": "wildan",
      "title": "Updated Task",
      "description": "Updated description",
      "status": false,
      "createdAt": "2024-10-12",
      "updateAt": "2024-10-15",
      "startDate": "2024-10-15",
      "endDate": "2024-10-16"
    },
    {
      "taskId": 1,
      "username": "wildan",
      "title": "Updated Task",
      "description": "Updated description",
      "status": false,
      "createdAt": "2024-10-12",
      "updateAt": "2024-10-15",
      "startDate": "2024-10-15",
      "endDate": "2024-10-16"
    }
  ],
  "paging" : {
    "currentPage" : 0,
    "totalPage" : 10,
    "size" : 10
  }
}
```


Response Body (Failed) :
```json
{
  "error" : "Unauthorized"
}
```

## Delete Task
Endpoint : DELETE /api/tasks/{taskId}

Request Header :
- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"  
}
```

Response Body (Failed) :
```json
{
  "error" : "Something wrong"
}
```