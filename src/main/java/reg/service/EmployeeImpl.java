package reg.service;

import reg.dto.EmployeeDTO;
import reg.dto.LoginDTO;
import reg.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reg.repo.EmployeeRepo;
import reg.response.LoginResponse;

import java.util.Optional;

@Service
public class EmployeeImpl implements EmployeeService{
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(
        employeeDTO.getId(),
        employeeDTO.getUserName(),
        employeeDTO.getEmail(),
        this.passwordEncoder.encode(employeeDTO.getPassword()),
        employeeDTO.getConfirmPassword());
        employeeRepo.save(employee);


        return employee.getUserName();
    }

    @Override
    public LoginResponse loginEmployee(LoginDTO loginDTO) {

        Employee employee1 = employeeRepo.findByEmail(loginDTO.getEmail());
        if (employee1 != null) {
            String password = loginDTO.getPassword();
            String encodedPassword = employee1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                Optional<Employee> employee = employeeRepo.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (employee.isPresent()) {
                    return new LoginResponse("Login Success", true);
                } else {
                    return new LoginResponse("Login Failed", false);
                }
            } else {

                return new LoginResponse("password Not Match", false);
            }
        }else {
            return new LoginResponse("Email not exits", false);
        }


    }

}

    //documentation for addEmployee
    //this method is used to add employee details


