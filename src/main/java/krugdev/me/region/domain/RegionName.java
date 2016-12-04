package krugdev.me.region.domain;

public enum RegionName {
	SOUTH_EAST("South East"),
	LONDON("London"),
	SOUTH_WEST("South West"),
	MIDLANDS("Midlands"),
	NORTH_EAST("North East"),
	NORTH_WEST("North West");
	
	private String name;
	
	private RegionName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
	

}
