package reg.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reg.dto.EmployeeDTO;
import reg.dto.LoginDTO;
import reg.entity.Employee;
import reg.repo.EmployeeRepo;
import reg.service.EmployeeService;

@ContextConfiguration(classes = {EmployeeController.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeRepo employeeRepo;

    @MockBean
    private EmployeeService employeeService;

    /**
     * Method under test: {@link EmployeeController#login(LoginDTO)}
     */
    @Test
    void testLogin() throws Exception {
        Employee employee = new Employee();
        employee.setConfirmPassword("iloveyou");
        employee.setEmail("jane.doe@example.org");
        employee.setId(1);
        employee.setPassword("iloveyou");
        employee.setUserName("janedoe");
        when(employeeRepo.findByEmail(Mockito.<String>any())).thenReturn(employee);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setConfirmPassword("iloveyou");
        loginDTO.setEmail("jane.doe@example.org");
        loginDTO.setPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(loginDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/employee/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Incorrect password"));
    }

    /**
     * Method under test: {@link EmployeeController#registerEmployee(EmployeeDTO)}
     */
    @Test
    void testRegisterEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setConfirmPassword("iloveyou");
        employeeDTO.setEmail("jane.doe@example.org");
        employeeDTO.setId(1);
        employeeDTO.setPassword("iloveyou");
        employeeDTO.setUserName("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(employeeDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/employee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Password does not meet the criteria"));
    }

    /**
     * Method under test: {@link EmployeeController#registerEmployee(EmployeeDTO)}
     */
    @Test
    void testRegisterEmployee2() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setConfirmPassword("?");
        employeeDTO.setEmail("jane.doe@example.org");
        employeeDTO.setId(1);
        employeeDTO.setPassword("iloveyou");
        employeeDTO.setUserName("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(employeeDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/employee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Password and confirm password do not match"));
    }
    void testRegisterEmployeeFailedEmail() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmail("iloveyou");
        employeeDTO.setEmail("jane.doe@example.com");
        employeeDTO.setId(1);
        employeeDTO.setPassword("iloveyou");
        employeeDTO.setUserName("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(employeeDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/employee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Error: Only Deloitte email addresses are allowed"));
    }
}

