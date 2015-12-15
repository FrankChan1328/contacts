package contacts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import contacts.entity.Contact;

public interface ContactMapper {

	@Results({ @Result(property = "id", column = "id"),
			@Result(property = "firstName", column = "firstName"),
			@Result(property = "lastName", column = "lastName"),
			@Result(property = "phoneNumber", column = "phoneNumber"),
			@Result(property = "emailAddress", column = "emailAddress") })
	@Select("SELECT id, firstName,lastName,phoneNumber,emailAddress FROM contacts")
	List<Contact> selectAllContacts();

	@Update("UPDATE contacts SET firstName=#{firstName}, lastName=#{lastName}, phoneNumber=#{phoneNumber}, emailAddress=#{emailAddress}")
	void updateContact(Contact contact);

	@Insert("INSERT into contacts(firstName,lastName,phoneNumber,emailAddress) VALUES(#{firstName}, #{lastName}, #{phoneNumber}, #{emailAddress})")
	void insertContact(Contact contact);

	@Delete("DELETE FROM contact WHERE id =#{id}")
	void deleteContact(Long id);
}
