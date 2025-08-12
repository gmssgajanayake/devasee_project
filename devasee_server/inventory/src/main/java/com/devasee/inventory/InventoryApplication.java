package com.devasee.inventory;

import com.devasee.inventory.dto.CreateInventoryDTO;
import com.devasee.inventory.entity.Inventory;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		// Skip mapping 'id' when mapping CreateInventoryDTO -> Inventory
		modelMapper.typeMap(CreateInventoryDTO.class, Inventory.class)
				.addMappings(mapper
						-> mapper.skip(Inventory::setId));
		return  modelMapper;
	}
}
