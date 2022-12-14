package br.com.rodrigopostai.rmm.controller;

import br.com.rodrigopostai.rmm.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

public abstract class BaseController<T extends BaseEntity, ID> {

    private final JpaRepository<T,ID> repository;

    protected BaseController(JpaRepository<T,ID> repository) {
        this.repository = repository;
    }

    @GetMapping("{id}")
    private ResponseEntity<T> findById(@PathVariable("id") ID id) {
        Optional<T> entityFound = repository.findById(id);
        if (entityFound.isPresent()) {
            return ResponseEntity.ok(entityFound.get());
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<T> save(@RequestBody T entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @PutMapping
    public ResponseEntity<T> update(@RequestBody T entity) {
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") ID id) {
        repository.deleteById(id);
    }

    protected JpaRepository<T, ID> getRepository() {
        return this.repository;
    }
}
