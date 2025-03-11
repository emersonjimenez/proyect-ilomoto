package com.motos.ilomoto.controller.rest;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.request.BackupRequest;
import com.motos.ilomoto.model.service.abstraction.IBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/backup")
public class BackupRestController {
    private final IBackupService iBackupService;

    @GetMapping
    public APIResponse<List<String>> getAllBackups() {
        return iBackupService.getAllBackups();
    }

    @GetMapping("/backup")
    public APIResponse<Void> backup() {
        return iBackupService.backup();
    }

    @PostMapping("/restore")
    public APIResponse<Void> restore(@RequestBody BackupRequest backupRequest) {
        return iBackupService.restore(backupRequest);
    }
}
