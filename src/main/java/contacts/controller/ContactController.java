package contacts.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import contacts.entity.Contact;
import contacts.entity.Product;
import contacts.redis.message.RedisMessageSend;
import contacts.respsitory.ContactRepository;
import contacts.respsitory.ProductRepository;

@Controller
@RequestMapping("/")
public class ContactController {
	
	@Autowired
	private ContactRepository contactRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private RedisMessageSend redisMessageSend;

	@RequestMapping(method=RequestMethod.GET)
	public String home(Map<String,Object> model) {
		// test redis message send and receive
		redisMessageSend.sendMessage("Hello,redis ! From Home !");
		
		List<Contact> contacts = contactRepo.findAll();
		model.put("contacts", contacts);
		return "home";
	}
	
	@RequestMapping(value="/product",method=RequestMethod.GET)
	public String product(Map<String,Object> model) {
		Long id = 1L;
		Product product = productRepo.getProductById(id);
		model.put("products", product);
		
		// test redis message send and receive
		redisMessageSend.sendMessage("Hello,redis ! From Product !");
		
		return "product";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String submit(Contact contact) {
		contactRepo.save(contact);
		return "redirect:/";
	}
}