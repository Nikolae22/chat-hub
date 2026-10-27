package com.backend.service;

import com.backend.domain.AuditEvent;
import com.backend.respository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditEventRepository auditEventRepository;
    private final ObjectMapper objectMapper;

    public void record(UUID actorId, String action, String resourceType,
                       String resourceId, Map<String, ?> metadata){
        auditEventRepository.save(
                new AuditEvent(actorId,action,resourceType,resourceId,toJson(metadata))
        );
    }

    private String toJson(Map<String ,?> metadata){
        try {
            return objectMapper.writeValueAsString(metadata);
        }catch (JacksonException exception){
            return  "{}";
        }
    }

}
