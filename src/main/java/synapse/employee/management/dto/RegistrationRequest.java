
package synapse.employee.management.dto;

import lombok.Data;

@Data
public class RegistrationRequest {


    private String userName;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String department;
    private String designation;
}
