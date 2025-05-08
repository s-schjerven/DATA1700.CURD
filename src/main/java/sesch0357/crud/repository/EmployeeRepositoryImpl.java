package sesch0357.crud.repository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sesch0357.crud.model.Employee;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class EmployeeRepositoryImpl implements EmployeeRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Employee save(Employee emp){
            // create employee
            String sql = "insert into employee (first_name, last_name, phone_number) values (?, ?, ?)";
            jdbcTemplate.update(sql,emp.getFirstName(),emp.getLastName(),emp.getPhoneNumber());
            return emp;
    }

    @Override
    public List<Employee> findAll(){
        String sql = "select * from employee";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Employee(
            rs.getLong("id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("phone_number")
        ));
    }

    @Override
    public Optional<Employee> findById(long id){
        String sql = "select * from employee where id = ?";
        try {
            Employee emp = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> new Employee(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("phone_number")
            ));
            return Optional.of(emp);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id){
        String sql = "delete from employee where id = ?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public int update(Long id, String firstName, String lastName, String phoneNumber) {
        String sql = "UPDATE employee SET first_name = ?, last_name = ?, phone_number = ? WHERE id = ?";
        return jdbcTemplate.update(sql, firstName, lastName, phoneNumber, id);
    }

    @Override
    public boolean existsById(Long id){
        String sql = "select count(*) from employee where id = ?";
        Integer count = jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count != null && count > 0;
    }
}
