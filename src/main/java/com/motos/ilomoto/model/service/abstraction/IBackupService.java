package com.motos.ilomoto.model.service.abstraction;

import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.motos.ilomoto.model.dto.request.BackupRequest;

import java.util.List;

public interface IBackupService {
    APIResponse<List<String>> getAllBackups();
    APIResponse<Void> backup();
    APIResponse<Void> restore(BackupRequest backupRequest);
}
