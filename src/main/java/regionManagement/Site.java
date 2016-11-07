package regionManagement;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "site")
public class Site {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	@ElementCollection
	private List<Integer> visitorsIds = new ArrayList<>();
	@ManyToOne
	private Region region;

	// default constructor required by JPA
	public Site() {
	}
	
	public Site(Region region) {
		this.region = region; 	
	}

	public Region getRegion() {
		// TODO Auto-generated method stub
		return region;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addVisitor(Integer id) {
		visitorsIds.add(id);
	}

	public Integer getId() {
		return this.id;
	}

}
