package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import pet.store.controller.model.CustomerData;
import pet.store.controller.model.EmployeeData;
import pet.store.controller.model.PetStoreData;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(@RequestBody PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));

	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());

	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		if (Objects.isNull(petStoreId)) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);

		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID = " + petStoreId + "was not found"));
	}

	@Transactional(readOnly = false)
	public EmployeeData saveEmployee(Long petStoreId, EmployeeData employeeData) {
		Long employeeId = employeeData.getEmployeeId();
		PetStore petStore = findPetStoreById(petStoreId);
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, employeeData);

		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		Employee databaseEmployee = employeeDao.save(employee);
		return new EmployeeData(databaseEmployee);

	}

	private void copyEmployeeFields(Employee employee, EmployeeData employeeData) {
		employee.setEmployeeId(employeeData.getEmployeeId());
		employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
		employee.setEmployeeLastName(employeeData.getEmployeeLastName());
		employee.setEmployeePhone(employeeData.getEmployeePhone());
		employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());

	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (Objects.isNull(employeeId)) {
			return new Employee();
		} else {
			return findEmployeeById(employeeId, petStoreId);
		}
	}

	private Employee findEmployeeById(Long employeeId, Long petStoreId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID = " + employeeId + "was not found"));
		if (employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("Employee with Id= )" + employeeId
					+ " does not have a relatioship with Pet Store with ID = " + petStoreId);

		}
		return employee;
	}

	@Transactional(readOnly = false)
	public CustomerData saveCustomer(Long petStoreId, CustomerData customerData) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = customerData.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		copyCustomerFields(customer, customerData);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		Customer databaseCustomer = customerDao.save(customer);
		CustomerData customerAsCustomerData = new CustomerData(databaseCustomer);
		return customerAsCustomerData;

	}

	private void copyCustomerFields(Customer customer, CustomerData customerData) {
		customer.setCustomerId(customerData.getCustomerId());
		customer.setCustomerFirstName(customerData.getCustomerFirstName());
		customer.setCustomerLastName(customerData.getCustomerLastName());
		customer.setCustomerEmail(customerData.getCustomerEmail());
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if (Objects.isNull(customerId)) {
			return new Customer();
		}
		return findCustomerById(petStoreId, customerId);
	}
	
	private Customer findCustomerById (Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID = " + customerId + "was not found"));
		boolean found = false;
		for (PetStore ps : customer.getPetStores()) {
			if (ps.getPetStoreId() == petStoreId) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("Customer with Id= )" + customerId
					+ " does not have a relationship with Pet Store with ID = " + petStoreId);
		}
		return customer;
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {

		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();
		for (PetStore petStore : petStores) {
			PetStoreData petStoreData = new PetStoreData(petStore);
			petStoreData.getEmployees().clear();
			petStoreData.getCustomers().clear();
			result.add(petStoreData);
		}
		return result;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
	}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);

	}

	public void deleteEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = findEmployeeById(employeeId, petStoreId);
		employeeDao.delete(employee);

	}

}
