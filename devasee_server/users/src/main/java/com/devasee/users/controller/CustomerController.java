package com.devasee.users.controller;

import com.devasee.users.dto.CustomerDTO;
import com.devasee.users.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/users/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/allCustomer")
    public ResponseEntity<List<CustomerDTO>> getAllCustomer() {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long customerId) {
        try {
            CustomerDTO customer = customerService.getCustomerById(customerId);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Customer not found with ID: " + customerId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving customer: " + e.getMessage());
        }
    }

    @PostMapping("/addCustomer")
    public ResponseEntity<?> saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO saved = customerService.saveCustomer(customerDTO);
            if (saved != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save customer.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving customer: " + e.getMessage());
        }
    }

    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO updated = customerService.updateCustomer(customerDTO);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update customer.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating customer: " + e.getMessage());
        }
    }


    @DeleteMapping("/deleteCustomer/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
        try {
            boolean deleted = customerService.deleteCustomerById(customerId);
            if (deleted) {
                return ResponseEntity.ok("Customer deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting customer: " + e.getMessage());
        }
    }
}
