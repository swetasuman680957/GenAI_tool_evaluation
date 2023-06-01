package reg.service;

import reg.dto.EmployeeDTO;
import reg.dto.LoginDTO;
import reg.response.LoginResponse;

public interface EmployeeService {
    String addEmployee(EmployeeDTO employeeDTO);

    LoginResponse loginEmployee(LoginDTO loginDTO);
}
