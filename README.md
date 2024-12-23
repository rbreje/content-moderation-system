# Content Moderation System

An app developed as a technical test with preconditions and specific requirements.

## Preconditions & Assumptions

- The app needs to perform well even for large input data files with millions of entries;
- The mocked services will have network latencies (random values between 50ms to 200ms);
- The mocked services are idempotent;
- The app will persist the partial results into a database;

## Content Moderation System Design

### Initial Design

![Initial Design](./Design/overall-architecture-v1.png)

### First Iteration

![First Iteration](./Design/cms-architecture-v2.png)

### Second Review

![Second Iteration](./Design/cms-architecture-v3.png)

### API Endpoints

```
POST /content-moderation-system/api/v1/file
```

The user or third-party will use this endpoint to upload a CSV file and will get a unique identifier to check job status
or result later.

#### Request

TBA

#### Response

```json
{
  "id": "2d14c3ca-d8ca-446a-8357-11d1623f0a82"
}
```

---

```
GET  /content-moderation-system/api/v1/file/{id}
```

The user will use the id to retrieve the job result as CSV. However, the job might take more time in case of huge input
files. Hence, the endpoint will return an IN_PROGRESS status until the job is done.

#### Response (IN_PROGRESS)

```json
{
  "id": "2d14c3ca-d8ca-446a-8357-11d1623f0a82",
  "status": "IN_PROGRESS"
}
```

#### Response (READY)

TBA

### Database

The app will store the partial results into a database in order to be able to create the required statistics of the
outfile file.

## Translation Service (mocked)

The service will have a fake latency to simulate a real-world scenario with a random value for each request.

### API Endpoints

```
GET /translation-service/api/v1/translation
```

#### Request

```json
{
  "originalMessage": "Este es un texto para ser puntuado"
}
```

#### Response

```json
{
  "originalMessage": "Este es un texto para ser puntuado",
  "translatedMessage": "This is a text to be scored"
}
```

## Scoring Service (mocked)

The service will have a fake latency to simulate a real-world scenario with a random value for each request.

### API Endpoints

```
GET /scoring-service/api/v1/score 
```

#### Request

```json
{
  "message": "This is a text to be scored"
}
```

#### Response

```json
{
  "message": "This is a text to be scored",
  "score": 0.8
}
```

# How to Build & Run It

TBA

# TODOs

- [x] Create initial design and implementation plan;
- [x] Create the base project;
- [x] Implement the mocked services related stuff;
- [x] Implement storage mechanism of the input files;
- [x] Create database to store each input file;
- [x] Implement the API endpoint to store the input files;
- [x] Implement the API endpoint to download the output files;
- [ ] Implement the business logic with concurrence execution;
- [ ] Design & implement the DB schema;
- [ ] Add SonarCloud analysis;

# Further features

- Retry mechanism for failed connections to third-party services;