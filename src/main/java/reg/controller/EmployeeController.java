package reg.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reg.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reg.dto.LoginDTO;
import reg.entity.Employee;
import reg.repo.EmployeeRepo;
import reg.service.EmployeeService;


@RestController
@CrossOrigin
@RequestMapping("api/v1/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

        @PostMapping(path = "/register")
        //write a method to save employee details
        public String registerEmployee(@RequestBody EmployeeDTO employeeDTO) {
            if (!employeeDTO.getPassword().equals(employeeDTO.getConfirmPassword())) {
                return "Error: Password and confirm password do not match";
            }

            // Check password criteria
            String password = employeeDTO.getPassword();
            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$")) {
                 return "Error: Password does not meet the criteria";
            }

            // Check if email is a valid Deloitte email address
            if (!employeeDTO.getEmail().endsWith("deloitte.com")) {
                return "Error: Only Deloitte email addresses are allowed";
            }

            // Check if email already exists in the database
            if (employeeRepo.findByEmail(employeeDTO.getEmail()) != null) {
                return "Error: Email already exists";

            }

            // Hash the password
            String hashedPassword = passwordEncoder.encode(password);
            employeeDTO.setPassword(hashedPassword);

            try {
                // Save the user in the database
                employeeRepo.save(new reg.entity.Employee(
                        employeeDTO.getId(),
                        employeeDTO.getUserName(),
                        employeeDTO.getEmail(),
                        employeeDTO.getPassword(),
                        employeeDTO.getConfirmPassword()
                ));

                return "Registration successful";
            } catch (Exception e) {
                return "Error: Failed to register user";
            }
        }

        //explain the method saveEmployee
        @PostMapping(path = "/login")
        public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
            // Find the user by email
            Employee employee = employeeRepo.findByEmail(loginDTO.getEmail());

            // Check if the user exists
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Invalid email");
            }

            // Compare the hashed passwords
            if (!passwordEncoder.matches(loginDTO.getPassword(), employee.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Incorrect password");
            }

            // Generate JWT token
            String token = Jwts.builder()
                    .setSubject(employee.getEmail())
                    .signWith(SignatureAlgorithm.HS256, "2D4A614E645267556B58703273357638792F423F4428472B4B6250655368566D") // Replace with your own secret key
                    .compact();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login successful. JWT token: " + token);
        }

    }

