package sesch0357.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sesch0357.crud.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
