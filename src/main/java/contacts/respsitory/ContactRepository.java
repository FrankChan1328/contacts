package contacts.respsitory;

import java.util.List;

import contacts.entity.Contact;

public interface ContactRepository {
	
	List<Contact> findAll();

	void save(Contact contact) ;
		
	String testCache();
}
