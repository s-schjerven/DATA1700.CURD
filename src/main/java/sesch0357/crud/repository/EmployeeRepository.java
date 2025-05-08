package sesch0357.crud.repository;

import sesch0357.crud.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    Employee save(Employee employee);
    List<Employee> findAll();
    void deleteById(Long id);
    int update(Long id, String firstName, String lastName, String phoneNumber);
    boolean existsById(Long id);
    Optional<Employee> findById(long id);
}
