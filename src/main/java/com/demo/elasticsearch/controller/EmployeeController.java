package com.demo.elasticsearch.controller;

import com.demo.elasticsearch.model.Employee;
import com.demo.elasticsearch.request.DeleteById;
import com.demo.elasticsearch.request.FindAllRequest;
import com.demo.elasticsearch.request.FindByIdRequest;
import com.demo.elasticsearch.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/save")
    public Mono<Employee> saveEmployee(@RequestBody Employee request){
        return employeeService.saveEmployee(request);
    }

    @PostMapping("/findById")
    public Mono<Employee> findEmployeeById(@RequestBody FindByIdRequest request){
        return employeeService.findEmployeeById(request.getId());
    }

    @PostMapping("/findAll")
    public Flux<Employee> findAllEmployee(@RequestBody FindAllRequest request){
        return employeeService.findAllEmployee(request.getField(),request.getValue());
    }

    @PostMapping("/delete")
    public Mono<String> deleteEmployeeById(@RequestBody DeleteById request){
        return employeeService.deleteEmployeeById(request.getId());
    }
}
