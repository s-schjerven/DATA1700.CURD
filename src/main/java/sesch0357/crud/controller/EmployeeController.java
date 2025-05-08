package sesch0357.crud.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sesch0357.crud.model.Employee;
import sesch0357.crud.repository.EmployeeRepository;
import sesch0357.crud.dto.EmployeeRequest;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/post")
    public Employee create(@Valid @RequestBody EmployeeRequest request) {
        Employee emp = new Employee(null, request.firstName(), request.lastName(), request.phoneNumber());
        return repository.save(emp);
    }

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        Optional<Employee> existing = repository.findById(id);
        if (existing.isPresent()) {
            Employee emp = existing.get();
            existing.get().setFirstName(request.firstName());
            existing.get().setLastName(request.lastName());
            existing.get().setPhoneNumber(request.phoneNumber());
            return ResponseEntity.ok(repository.save(emp));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}