package krugdev.me.siteService.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@NamedQueries({
@NamedQuery(
	name = "Site.findSiteByName",
	query= "Select s from Site s where s.name = :name"),
@NamedQuery(
		name = "Site_findSiteByChargingStructure",
		query= "Select s from Site s where s.chargingStructure = :chargingStructure")
})
public class Site  {
	
	@Id
	@GeneratedValue
	private int siteId;
	
	private String name;
	
	@OneToMany(mappedBy="visitedSite", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	private Collection<Visitor> visitors = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="STRUCTURE_ID")
	private ChargingStructure chargingStructure;	

	// required by the JPA
	protected Site() {}
	
	private Site(String name) {
		this.name = name;
	}

	public static Site instanceOf(String name) {
		return new Site(name);
	}
	
	public void addVisitor(Visitor visitor) {
		visitors.add(visitor);
	}
	
	public int getVisitorsCount() {
		return visitors.size();
	}
	
	public void setCharginStructure(ChargingStructure chargingStructure) {
		this.chargingStructure = chargingStructure;
	}

	public ChargingStructure getChargingStructure() {
		return chargingStructure;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {return true;}
		if (obj == null) {return false;}
		if (getClass() != obj.getClass()) {return false;}
		
		Site other = (Site) obj;
		if (name == null && other.name != null) {
			return false;
		} else if (!name.equals(other.name)) {return false;}
		if (siteId != other.siteId)	{return false;}
		return true;
	}
}
