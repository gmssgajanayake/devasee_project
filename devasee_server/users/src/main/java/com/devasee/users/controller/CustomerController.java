package com.devasee.users.controller;

import com.devasee.users.dto.CreateCustomerDTO;
import com.devasee.users.dto.DeleteCustomerDTO;
import com.devasee.users.dto.RetrieveCustomerDTO;
import com.devasee.users.response.CustomResponse;
import com.devasee.users.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/v1/users/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/public/all")
    public CustomResponse<List<RetrieveCustomerDTO>> getAllCustomers() {
        List<RetrieveCustomerDTO> customers = customerService.getAllCustomers();
        return new CustomResponse<>(true, "Customers retrieved successfully", customers);
    }

    @GetMapping("/public/{id}")
    public CustomResponse<RetrieveCustomerDTO> getCustomerById(@PathVariable Long id) {
        RetrieveCustomerDTO customer = customerService.getCustomerById(id);
        return new CustomResponse<>(true, "Customer retrieved successfully", customer);
    }

    @PostMapping("/add")
    public CustomResponse<CreateCustomerDTO> addCustomer(@RequestBody CreateCustomerDTO dto) {
        CreateCustomerDTO saved = customerService.saveCustomer(dto);
        return new CustomResponse<>(true, "Customer created successfully", saved);
    }

    @PutMapping("/update")
    public CustomResponse<RetrieveCustomerDTO> updateCustomer(@RequestBody RetrieveCustomerDTO dto) {
        RetrieveCustomerDTO updated = customerService.updateCustomer(dto);
        return new CustomResponse<>(true, "Customer updated successfully", updated);
    }

    @DeleteMapping("/delete/{id}")
    public CustomResponse<DeleteCustomerDTO> deleteCustomer(@PathVariable Long id) {
        DeleteCustomerDTO deleted = customerService.deleteCustomer(id);
        return new CustomResponse<>(true, "Customer deleted successfully", deleted);
    }
}
