package com.devasee.users.service;

import com.devasee.users.dto.CreateCustomerDTO;
import com.devasee.users.dto.DeleteCustomerDTO;
import com.devasee.users.dto.RetrieveCustomerDTO;
import com.devasee.users.entity.Customer;
import com.devasee.users.exceptions.CustomerAlreadyExistsException;
import com.devasee.users.exceptions.CustomerNotFoundException;
import com.devasee.users.exceptions.ServiceUnavailableException;
import com.devasee.users.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public List<RetrieveCustomerDTO> getAllCustomers() {
        try {
            List<Customer> customers = customerRepository.findAll();
            return modelMapper.map(customers, new TypeToken<List<RetrieveCustomerDTO>>(){}.getType());
        } catch (Exception e) {
            throw new ServiceUnavailableException("Failed to fetch customers. Please try again later.");
        }
    }

    public RetrieveCustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        return modelMapper.map(customer, RetrieveCustomerDTO.class);
    }

    public CreateCustomerDTO saveCustomer(CreateCustomerDTO dto) {
        if(customerRepository.existsByEmail(dto.getEmail())) {
            throw new CustomerAlreadyExistsException("Customer with email '" + dto.getEmail() + "' already exists");
        }
        Customer customer = modelMapper.map(dto, Customer.class);
        customerRepository.save(customer);
        return dto;
    }

    public RetrieveCustomerDTO updateCustomer(RetrieveCustomerDTO dto) {
        Customer existing = customerRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + dto.getId()));
        Customer updated = modelMapper.map(dto, Customer.class);
        updated.setId(existing.getId());
        Customer saved = customerRepository.save(updated);
        return modelMapper.map(saved, RetrieveCustomerDTO.class);
    }

    public DeleteCustomerDTO deleteCustomer(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(existing);
        return modelMapper.map(existing, DeleteCustomerDTO.class);
    }
}
