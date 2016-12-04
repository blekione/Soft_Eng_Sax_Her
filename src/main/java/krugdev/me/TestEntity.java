package krugdev.me;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(
		name = "TestEntity.findEntityByName",
		query= "Select s from TestEntity s where s.name = :name")
public class TestEntity{
	@Id
	int id;
	String name;
	
	protected TestEntity() {}
	
	public TestEntity(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
