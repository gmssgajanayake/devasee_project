package com.devasee.product.services;

import com.devasee.product.dto.PrintDTO;
import com.devasee.product.entity.Printing;
import com.devasee.product.repo.PrintRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PrintServices {

    @Autowired
    private PrintRepo printRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<PrintDTO> getPrintType() {
        return modelMapper.map(printRepo.findAll(), new TypeToken<List<PrintDTO>>(){}.getType());
//        return  Collections.emptyList();
    }

    public PrintDTO  getPrintById(int printId) {
        return modelMapper.map(printRepo.findById(printId), PrintDTO.class);
    }

    public PrintDTO savePrint(PrintDTO printDTO) {
       printRepo.save(modelMapper.map(printDTO, Printing.class));
        return printDTO;
  }



    public PrintDTO updatePrint(PrintDTO printDTO) {
        printRepo.save(modelMapper.map(printDTO, Printing.class));
        return printDTO;
    }

    public boolean deletePrint(PrintDTO printDTO) {
        printRepo.delete(modelMapper.map(printDTO, Printing.class));
        return true;
    }
}
