# t2p-validation

Syntax Validation Endpoint

    Endpoint: /main/syntax/validate
    Request Type: POST

Request Details

    Body:
        The request should be sent as form-data.
        Include a key named "file".
        Upload a .bpmn file with this key.

To run Docker: 
docker build -t t2p-validation .
docker run -p 8080:8080 t2p-validation
