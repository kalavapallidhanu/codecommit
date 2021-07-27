package com.impactsure.artnook.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impactsure.artnook.dto.CustomerData;
import com.impactsure.artnook.repository.ExcelRepository;
import com.impactsure.artnook.utils.ExcelUtils;
@Service
public class ExcelService {
	@Autowired
	ExcelRepository excelRepository;
	

	 public ByteArrayInputStream loadFile() {
	    	List<CustomerData> customers = (List<CustomerData>) excelRepository.getEmployeeAllDetails();
	    	
	    	try {
	    		ByteArrayInputStream in = ExcelUtils.customersToExcel(customers);
	    		return in;
			} catch (IOException e) {
				e.printStackTrace();
			}
	
	        return null;
	    }
}
