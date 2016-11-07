package regionManagement;

public enum RegionName {

	SOUTH_EAST ("South-East"),
	LONDON ("London"),
	SOUTH_WEST ("South-West"),
	MIDLANDS ("Midlands"),
	NORTH_EAST ("North-East"),
	NORTH_WEST ("North-West");
	
	private final String name;
	
	private RegionName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
