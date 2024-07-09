package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.MainValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/main/syntax")
public class MainValidationController {

    @Autowired
    private MainValidationService mainValidationService;

    @Operation(summary = "Validiert eine BPMN-Datei", description = "Validiert eine hochgeladene BPMN-Datei und gibt die Validierungsergebnisse zurück")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validierung erfolgreich",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"xmlValidation\": {\n" +
                                    "    \"validationStatus\": \"completed\",\n" +
                                    "    \"errors\": [],\n" +
                                    "    \"warnings\": []\n" +
                                    "  },\n" +
                                    "  \"gatewayValidation\": {\n" +
                                    "    \"validationStatus\": \"completed\",\n" +
                                    "    \"errors\": [],\n" +
                                    "    \"warnings\": []\n" +
                                    "  },\n" +
                                    "  \"eventValidation\": {\n" +
                                    "    \"validationStatus\": \"completed\",\n" +
                                    "    \"errors\": [],\n" +
                                    "    \"warnings\": []\n" +
                                    "  },\n" +
                                    "  \"flowValidation\": {\n" +
                                    "    \"validationStatus\": \"completed\",\n" +
                                    "    \"errors\": [],\n" +
                                    "    \"warnings\": []\n" +
                                    "  },\n" +
                                    "  \"taskValidation\": {\n" +
                                    "    \"validationStatus\": \"completed\",\n" +
                                    "    \"errors\": [],\n" +
                                    "    \"warnings\": []\n" +
                                    "  }\n" +
                                    "}"
                            ))),
            @ApiResponse(responseCode = "400", description = "Fehlerhafte Anfrage"),
            @ApiResponse(responseCode = "500", description = "Interner Serverfehler")
    })
    @PostMapping(value = "/validate", consumes = "multipart/form-data")
    public Map<String, Object> validateAll(
            @Parameter(description = "Die hochzuladende BPMN-Datei", required = true,
                    content = @Content(mediaType = "application/octet-stream",
                            schema = @Schema(type = "string", format = "binary")))
            @RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("uploaded-", ".bpmn");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        }

        Map<String, Object> result = mainValidationService.validateAll(tempFile.getAbsolutePath());
        tempFile.delete();

        return result;
    }
}
