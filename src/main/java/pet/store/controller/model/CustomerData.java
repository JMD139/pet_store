package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pet.store.entity.Customer;

@Data
@NoArgsConstructor
public class CustomerData {

	Long customerId;
	String customerFirstName;
	String customerLastName;
	String customerEmail;

	@Setter
	Set<PetStoreData> petStores = new HashSet<>();

	public CustomerData(Customer customer) {
		this.customerId = customer.getCustomerId();
		this.customerFirstName = customer.getCustomerFirstName();
		this.customerLastName = customer.getCustomerLastName();
		this.customerEmail = customer.getCustomerEmail();
	}

	public CustomerData(Long customerId, String customerFirstName, String customerLastName, String customerEmail) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setCustomerFirstName(customerFirstName);
		customer.setCustomerLastName(customerLastName);
		customer.setCustomerEmail(customerEmail);
	}

}
