package krugdev.me.region;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MarketingCampaign {
	@Id
	@GeneratedValue
	private int id;
	private LocalDate startDate;		
	private LocalDate endDate;
	@ManyToOne(cascade = CascadeType.MERGE)
	private Region region;
	
	private String name;
	private double targetMultiplier;
	
	// required by JPA
	protected MarketingCampaign() {}
	
	public static class Builder{
		private LocalDate startDate;		
		private LocalDate endDate;
		private final Region region;
		
		private String name = "unknown";
		private double targetMultiplier = 1.0d;
		
		public Builder(LocalDate startDate, LocalDate endDate, Region region) {
			setCampaignDates(startDate, endDate);
			this.region = region;
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
	}	
	
	private MarketingCampaign(Builder builder) {
		this.startDate = builder.startDate;
		this.endDate = builder.endDate;
		this.region = builder.region;
		this.name = builder.name;
		this.targetMultiplier = builder.targetMultiplier;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public Region getRegion() {
		return region;
	}

	public String getName() {
		return name;
	}

	public double getTargetMultiplier() {
		return targetMultiplier;
	}
}
