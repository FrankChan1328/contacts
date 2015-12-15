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
	// 摘自网上，功能：做缓存
	
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
	// 摘自网上，功能：做缓存
	public String testCache() {
		System.out.println("无缓存");
		String test = "test";
		return test;
	}

}
