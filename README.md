# Content Moderation System

An app developed as a technical test with preconditions and specific requirements.

# Notes from me (Raul Breje)

I've spent ~9 hours working on the project. I started to go too broad on the solution at some point, but I managed
to narrow it down and reach a functional state at the end.

Of course, there are quite a few things to improve further on, but I think the current outcome is good enough
to show my expertise keeping in mind the time spent on it.

## Preconditions & Assumptions

- The app needs to perform well even for large input data files with millions of entries;
- The mocked services will have network latencies (random values between 50ms to 200ms);
- The mocked services are idempotent;
- The app will persist the partial results into a database;

## Content Moderation System Design

### Second Iteration (Current State)

The CSV files are stored locally in order to decouple the upload and execution processes. The user will receive a 
unique identifier to use later in order to check the status or retrieve the output if ready.

The uploaded file is stored locally by a dedicated service and the entry is added to the DB by another service.

The independent CSV processor is checking the DB for newly added files. When a new file is detected, the content of 
it is retrieved and parse in a multi-threaded approach. This makes the CSV processing part scalable despite the amount
of users which are uploading files to it.

![Second Iteration](./Design/cms-architecture-v3.png)

### First Iteration (outdated)

![First Iteration](./Design/cms-architecture-v2.png)

### Initial Design (outdated)

![Initial Design](./Design/overall-architecture-v1.png)

### API Endpoints

```
POST /content-moderation-system/api/v1/file
```

The user or third-party will use this endpoint to upload a CSV file and will get a unique identifier to check job status
or result later.

#### Request

'file=@path_to_your_file.csv'

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

```
user_id,total_messages,avg_score
56038,525,0.2463875
46332,2324,0.657238
61854,5242,0.3047051
```

### Database

The app will use a table to add metadata about the uploaded files. The CSV Processor will load the metadata and will 
initiate the processing for each entry.

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

> **Note:** The project requires Java 17.

```shell
./gradlew bootRun
```

The best would be to use a REST client as Insomnia or Postman to test the API endpoints.

To provide the endpoint with a file to process.

```shell
curl --request POST \
  --url http://localhost:8080/api/v1/file \
  --header 'Content-Type: multipart/form-data' \
  --form 'file=@/localpath/input1.csv'
```

To retrieve the outfile or check the status.

```shell
curl --request GET \
  --url http://localhost:8080/api/v1/file/ab8f2571-f107-4c4b-94ed-dcea2890360a
```

## Performance Tests

It required ~5 minutes to processes 1 file with ~10000 records.
It required ~5 minutes to processes 5 files with ~10000 records each.

# TODOs

- [x] Create initial design and implementation plan;
- [x] Create the base project;
- [x] Implement the mocked services related stuff;
- [x] Implement storage mechanism of the input files;
- [x] Create database to store each input file;
- [x] Implement the API endpoint to store the input files;
- [x] Implement the API endpoint to download the output files;
- [X] Implement the business logic with concurrence execution;
- [ ] Add SonarCloud analysis;

# Further features

- Retry mechanism for failed connections to third-party services;
- Compute hash of each user message to use as idempotency key for translation and scoring requests;