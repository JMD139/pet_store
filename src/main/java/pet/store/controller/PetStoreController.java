package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.CustomerData;
import pet.store.controller.model.EmployeeData;
import pet.store.controller.model.PetStoreData;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store with id: {}", petStoreId);
		return petStoreService.savePetStore(petStoreData);
	}

	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public EmployeeData addEmployeeToStore(@PathVariable Long petStoreId, @RequestBody EmployeeData employeeData) {
		log.info("Adding employee with id: {} to store with id: {}", petStoreId, employeeData);
		return petStoreService.saveEmployee(petStoreId, employeeData);
	}

	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CustomerData addCustomerToStore(@PathVariable Long petStoreId, @RequestBody CustomerData customerData) {
		log.info("Adding customer to store with id: {}", petStoreId);
		return petStoreService.saveCustomer(petStoreId, customerData);
	}

	@GetMapping
	public List<PetStoreData> retrieveAllPetStores() {
		log.info("Retrieving all pet stores");
		return petStoreService.retrieveAllPetStores();

	}

	@GetMapping("/{petStoreId}")
	public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving pet store by id: {}", petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}

	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("attempting to delete pet store with id: {}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		return Map.of("message", "Pet Store with id: " + petStoreId + " was deleted");
	}
	 @DeleteMapping("/{petStoreId}/employee/{employeeId}")
	public Map<String, String> deleteEmployeeById(@PathVariable Long petStoreId, @PathVariable Long employeeId) {
		log.info("attempting to delete employee with id: {} from pet store with id: {}", employeeId, petStoreId);
		petStoreService.deleteEmployeeById(petStoreId, employeeId);
		return Map.of("message",
				"Employee with id: " + employeeId + " was deleted from pet store with id: " + petStoreId);
	}

}
