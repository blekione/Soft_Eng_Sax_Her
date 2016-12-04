package krugdev.me.region.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class MarketingCampaign {
	@Id
	@GeneratedValue
	private int id;
	private LocalDate startDate;		
	private LocalDate endDate;
	
	private String name;
	private double targetMultiplier;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "CampaignSites")
	@Fetch(FetchMode.SELECT)
	private Set<RegionSite> regionSites;
	
	// required by JPA
	protected MarketingCampaign() {}
	
	public static class Builder{
		private LocalDate startDate;		
		private LocalDate endDate;
		
		private String name = "unknown";
		private double targetMultiplier = 1.0d;
		private Set<RegionSite> regionSites = new HashSet<>();
		
		public Builder(LocalDate startDate, LocalDate endDate) {
			setCampaignDates(startDate, endDate);
		}
		
		private void setCampaignDates(LocalDate campaignStartDate, LocalDate campaignEndDate) {
			if (campaignStartDate.compareTo(LocalDate.now()) < 0) {
				throw new IllegalArgumentException("Date [" + campaignStartDate.toString() + "] is invalid as it is the past");
			} else if (campaignEndDate.compareTo(campaignStartDate) < 0) {
				throw new IllegalArgumentException("Campaign end date [" + campaignEndDate.toString()
						+ "] is before campaign start date [" + campaignStartDate.toString() + "].");
			}
			setCampaignStartDate(campaignStartDate);
			setCampaignEndDate(campaignEndDate);
		}
		
		private void setCampaignStartDate(LocalDate date) {
			this.startDate = date;
		}

		private void setCampaignEndDate(LocalDate date) {
			this.endDate = date;
		}
		
		public MarketingCampaign build() {
			return new MarketingCampaign(this);
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder targetMultiplier(double targetMultiplier) {
			this.targetMultiplier = targetMultiplier;
			return this;
		}
		
		public Builder regionSites(Set<RegionSite> regionSites) {
			this.regionSites = regionSites;
			return this;
		}
	}	
	
	private MarketingCampaign(Builder builder) {
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.name = builder.name;
		this.targetMultiplier = builder.targetMultiplier;
		this.regionSites = builder.regionSites;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public String getName() {
		return name;
	}

	public double getTargetMultiplier() {
		return targetMultiplier;
	}
	
	public Set<RegionSite> getRegionSites() {
		return regionSites;
	}

	@Override
	public String toString() {
		return "MarketingCampaign [startDate=" + startDate.toString() + ", endDate=" + endDate.toString() + ", name=" + name
				+ ", targetMultiplier=" + targetMultiplier + ", regionSitesSize=" + regionSites.size() + "]";
	}
}
