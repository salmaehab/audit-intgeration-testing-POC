package com.example.envers.services;
import com.example.envers.models.Subscription;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
@Service
public class SubscriptionAuditService {

    @PersistenceContext
    private EntityManager entityManager;

    public List getAuditHistory(Long employeeId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        return auditReader.createQuery()
                .forRevisionsOfEntity(Subscription.class, false, true)
                .add(AuditEntity.id().eq(employeeId))
                .getResultList();
    }
}
