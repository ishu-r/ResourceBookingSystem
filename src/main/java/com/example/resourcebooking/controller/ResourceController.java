package com.example.resourcebooking.controller;

import com.example.resourcebooking.entity.Resource;
import com.example.resourcebooking.service.ResourceService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    // GET all resources
    // USER and ADMIN can access
    @GetMapping
    public ResponseEntity<List<Resource>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    // GET resource by ID
    // USER and ADMIN can access
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(
            @PathVariable Long id) {

        return resourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE resource
    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Resource> createResource(
            @RequestBody Resource resource) {

        Resource savedResource =
                resourceService.saveResource(resource);

        return ResponseEntity.ok(savedResource);
    }

    // UPDATE resource
    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(
            @PathVariable Long id,
            @RequestBody Resource resource) {

        return resourceService.getResourceById(id)
                .map(existingResource -> {

                    existingResource.setName(resource.getName());
                    existingResource.setType(resource.getType());
                    existingResource.setDescription(
                            resource.getDescription());
                    existingResource.setAvailable(
                            resource.isAvailable());

                    Resource updatedResource =
                            resourceService.saveResource(
                                    existingResource);

                    return ResponseEntity.ok(updatedResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE resource
    // ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(
            @PathVariable Long id) {

        if (resourceService.getResourceById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        resourceService.deleteResource(id);

        return ResponseEntity.noContent().build();
    }
}