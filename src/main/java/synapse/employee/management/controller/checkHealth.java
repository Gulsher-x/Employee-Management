package synapse.employee.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class checkHealth {
    @GetMapping("health")
    public String health(){
        return "working...";
    }
}
