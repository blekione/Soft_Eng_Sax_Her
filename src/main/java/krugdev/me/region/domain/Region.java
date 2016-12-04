package krugdev.me.region.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@NamedQuery(
		name = "Region.findRegionByName",
		query= "Select r from Region r where r.name = :name")
public class Region {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany(mappedBy = "region", fetch = FetchType.EAGER)
	private Set<RegionSite> regionSites;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "RegionCampaigns")
	@Fetch(FetchMode.SELECT)
	private List<MarketingCampaign> campaigns;
	
	// required by JPA
	protected Region() {}
	
	private Region(String name) {
		this.name = name;
		regionSites = new HashSet<>();
		campaigns = new ArrayList<>();
	}
	
	public static Region instanceOf(String name) {
		return new Region(name);
	}

	public void addMarketingCampaign(MarketingCampaign campaign) {
		campaigns.add(campaign);
	}

	public List<MarketingCampaign> getMarketingCampaigns() {
		return campaigns;
	}
	
	public String getName() {
		return name;
	}

	public void addSite(RegionSite regionSite) {
		if (regionSites.contains(regionSite)) {
			throw new IllegalArgumentException("RegionSite already exist in Region");
		}
		regionSites.add(regionSite);
	}
	
	public Set<RegionSite> getSites() {
		return regionSites;
	}

	public void removeSite(RegionSite regionSite) {
		regionSites.remove(regionSite);
	}
	
	public void replaceSite(RegionSite oldSite, RegionSite newSite) {
		addSite(newSite);	
		removeSite(oldSite);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {return true;}
		if (obj == null) {return false;}
		if (getClass() != obj.getClass()) {return false;}
		
		Region other = (Region) obj;
		if (id != other.id) {return false;}
		if (name == null && other.name != null) {
				return false;
		} else if (!name.equals(other.name)) {return false;}
		return true;
	}
}
