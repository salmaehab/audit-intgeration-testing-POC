package com.example.envers.controllers;

import com.example.envers.models.Subscription;
import com.example.envers.repos.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {


    private final SubscriptionRepository repository;

    @PostMapping
    public Subscription createSubscription(@RequestBody Subscription subscription) {
        return repository.save(subscription);
    }

    @GetMapping
    public List<Subscription> getAllSubscriptions() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Optional<Subscription> subscription = repository.findById(id);
        return subscription.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a Subscription
    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @RequestBody Subscription updatedSubscription) {
        return repository.findById(id)
                .map(existingSubscription -> {
                    existingSubscription.setName(updatedSubscription.getName());
                    existingSubscription.setDescription(updatedSubscription.getDescription());

                    return ResponseEntity.ok(repository.save(existingSubscription));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a Subscription
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubscription(@PathVariable Long id) {
        return repository.findById(id)
                .map(subscription -> {
                    repository.delete(subscription);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

