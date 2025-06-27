# t2p-validation

This repository is for the validation of the created t2p-BPMN models.
After cloning this repository, it's essential to [set up git hooks](https://github.com/woped/woped-git-hooks/blob/main/README.md#activating-git-hooks-after-cloning-a-repository) to ensure project standards.

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

To access the swagger OpenApi Documentation:

    Endpoint: /swagger-ui/index.html
    


test
