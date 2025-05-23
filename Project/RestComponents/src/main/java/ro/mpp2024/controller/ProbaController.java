package ro.mpp2024.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mpp2024.domain.Proba;
import ro.mpp2024.IProbaRepo;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5175")
@RestController
@RequestMapping("/api/probe")
public class ProbaController {

    private final IProbaRepo probaRepository;

    @Autowired
    public ProbaController(IProbaRepo probaRepository) {
        this.probaRepository = probaRepository;
    }

    @GetMapping
    public Iterable<Proba> getAllProbe() {
        System.out.println("Return toate probele...");
        return probaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Proba> addProba(@RequestBody Proba proba) {
        Optional<Proba> result = probaRepository.save(proba);
        if (result.isEmpty()) {
            return new ResponseEntity<>(proba, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProba(@RequestBody Proba proba, @PathVariable Long id) {
        if (!id.equals(proba.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<Proba> result = probaRepository.update(proba);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Update failed", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProbaById(@PathVariable Long id) {
        Optional<Proba> found = probaRepository.findOne(id);
        return found.<ResponseEntity<?>>map(proba -> new ResponseEntity<>(proba, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProba(@PathVariable Long id) {
        try {
            Optional<Proba> deleted = probaRepository.delete(id);
            if (deleted.isEmpty()) {
                return new ResponseEntity<>("Proba deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Proba not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
