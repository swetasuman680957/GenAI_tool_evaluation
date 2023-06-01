package reg.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

        @Id
        @Column(name = "id",length = 45)
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;
        @Column(name = "userName",length = 255)
        private String userName;
        @Column(name = "email",length = 255)
        private String email;
        @Column(name = "password",length = 255)
        private String password;
        @Column(name = "confirmPassword",length = 255)
        private String confirmPassword;
    public Employee() {
    }
    public Employee(int id, String userName, String email, String password, String confirmPassword) {

        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }



    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
    //documentation for toString() method
    //https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#toString()

}

