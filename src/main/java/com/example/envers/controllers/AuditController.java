package com.example.envers.controllers;

import com.example.envers.services.SubscriptionAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscriptions/audit")
@RequiredArgsConstructor
public class AuditController {


    private final SubscriptionAuditService auditService;

    @GetMapping("/{id}")
    public List getSubscriptionAuditHistory(@PathVariable Long id) {
        return auditService.getAuditHistory(id);
    }
}
