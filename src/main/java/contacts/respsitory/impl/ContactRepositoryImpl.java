package contacts.respsitory.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import contacts.entity.Contact;
import contacts.mapper.ContactMapper;
import contacts.respsitory.ContactRepository;

@Repository
public class ContactRepositoryImpl implements ContactRepository{
	
	@Autowired
	private ContactMapper contactMapper;

	@Override
	// ժ�����ϣ����ܣ�������
	
	public List<Contact> findAll() {
		List<Contact> list = contactMapper.selectAllContacts();
		return list;
	}
	
	@Transactional
	@Override
	public void save(Contact contact) {
		contactMapper.insertContact(contact);
	}

	@Override
	@Cacheable(value = "usercache",keyGenerator = "wiselyKeyGenerator")
	// ժ�����ϣ����ܣ�������
	public String testCache() {
		System.out.println("�޻���");
		String test = "test";
		return test;
	}

}
