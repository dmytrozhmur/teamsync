package ua.nure.teamsync.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.teamsync.enums.SourceSystemEnum;
import ua.nure.teamsync.services.SyncService;

import java.util.Arrays;

@RestController("api/v1/")
public class SyncController {
    @Autowired
    private SyncService syncService;

    @PostMapping("sync")
    public ResponseEntity<Object> syncSystem(@RequestHeader("SourceSystem") String source) {
        try {
            syncService.service(source);
            return ResponseEntity.ok("Synced");
        } catch (IllegalAccessException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid source system for sync request");
        }
    }
}
