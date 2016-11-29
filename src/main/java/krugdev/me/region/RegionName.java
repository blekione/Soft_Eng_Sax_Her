package krugdev.me.region;

public enum RegionName {
	SOUTH_EAST("South East", 1),
	LONDON("London", 2),
	SOUTH_WEST("South West", 3),
	MIDLANDS("Midlands", 4),
	NORTH_EAST("North East", 5),
	NORTH_WEST("North West", 6);
	
	private String name;
	private int value;
	
	private RegionName(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	
	

}
