package com.devasee.users.service;

import com.devasee.users.dto.CustomerDTO;
import com.devasee.users.entity.Customer;
import com.devasee.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CustomerDTO> getAllCustomers() {
        try {
            List<Customer> customers = customerRepository.findAll();
            return modelMapper.map(customers, new TypeToken<List<CustomerDTO>>() {}.getType());
        } catch (Exception e) {
            // Log the exception and return an empty list or throw custom
            System.err.println("Error fetching all customers: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public CustomerDTO getCustomerById(Long customerId) {
        try {
            Optional<Customer> customer = customerRepository.findById(customerId);
            if (customer.isPresent()) {
                return modelMapper.map(customer.get(), CustomerDTO.class);
            } else {
                System.err.println("Customer not found with ID: " + customerId);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching customer by ID: " + e.getMessage());
            return null;
        }
    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = modelMapper.map(customerDTO, Customer.class);
            customerRepository.save(customer);
            return customerDTO;
        } catch (Exception e) {
            System.err.println("Error saving customer: " + e.getMessage());
            return null;
        }
    }

    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        try {
            Customer customer = modelMapper.map(customerDTO, Customer.class);
            customerRepository.save(customer);
            return customerDTO;
        } catch (Exception e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteCustomerById(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
            return true;
        }
        return false;
    }
}
