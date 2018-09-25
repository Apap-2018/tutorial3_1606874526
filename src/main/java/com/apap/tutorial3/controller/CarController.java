package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.CarModel;
import com.apap.tutorial3.service.CarService;

@Controller
public class CarController {
	@Autowired
	private CarService mobilService;
	
	@RequestMapping("/car/add")
	public String add(@RequestParam(value = "id", required = true) String id, 
			@RequestParam(value = "brand", required = true) String brand,
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "price", required = true) Long price,
			@RequestParam(value = "amount", required = true) Integer amount) {
		CarModel car = new CarModel(id, brand, type, price, amount);
		mobilService.addCar(car);
		return "add";
	}
	
	@RequestMapping("/car/view")
	public String view(@RequestParam("id") String id, Model model) {
		CarModel archive = mobilService.getCarDetail(id);
		
		model.addAttribute("car", archive);
		return "view-car";
	}
	
	@RequestMapping("/car/viewall")
	public String viewall (Model model) {
		List <CarModel> archive = mobilService.getCarList();
		
		model.addAttribute("listCar", archive);
		return "viewall-car";
	}
	
	@RequestMapping(value= {"/car/view","car/view/{id}"})
	public String viewById(@PathVariable Optional <String> id, Model model) {
		
		if (id.isPresent()) {
			List <CarModel> archive = mobilService.getCarList();

			for(int i= 0; i< archive.size(); i++) {
				if(archive.get(i).getId().equals(id.get())) {
					model.addAttribute("car", archive.get(i));
				}
			}
			return "viewby-id";

			
		}
		else {
			model.addAttribute("errorMsgType", "Id kosong");
			return "errorMsg";
		}
		
	}
	
	@RequestMapping(value= {"/car/update/{id}/amount/{amount}"})
	public String changeAmount(@PathVariable Optional<String> id, @PathVariable Integer amount, Model model) {
		if (id.isPresent()) {
			CarModel carModel = mobilService.getCarDetail(id.get());			
			if (carModel!=null) {
				carModel.setAmount(amount);
				model.addAttribute("car", carModel);
				return "amountChanged";
			}
			else {
				model.addAttribute("errorMsgType", "id tidak ada");
			}
			
		}
		else {
			model.addAttribute("errorMsgType", "Id kosong");
			
		}
		return "errorMsg";
	}
	
	@RequestMapping(value = {"/car/delete/{id}"})
	public String deleteById(@PathVariable Optional<String> id,  Model model) {
		if(id.isPresent()) {
			CarModel carModel = mobilService.getCarDetail(id.get());
			List <CarModel> archive = mobilService.getCarList();
			
			if(carModel != null) {
				for(int i=0; i < archive.size(); i++) {
					if(archive.get(i).getId().equals(id.get())) {
						archive.remove(i);
						model.addAttribute("delete", "Berhasil dihapus");	
					}
					else {
						model.addAttribute("delete", "id tidak ada");
					}	
				}
			}
			else {
				model.addAttribute("delete", "id tidak ada");
			}
		}
		else {
			model.addAttribute("delete", "id tidak ada");
		}
		return "delete";
		
	}
}
