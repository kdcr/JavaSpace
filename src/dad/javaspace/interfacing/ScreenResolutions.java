package dad.javaspace.interfacing;

public enum ScreenResolutions {
	X800Y600("800x600"),
	X1024Y768("1024x768"),
	X1280Y1024("1280x1024"),
	X1366Y768("1366x768"),
	X1360Y768("1360x768"),
	X1920Y1080("1920x1080");
	
	private String label;
	
	ScreenResolutions(String label) {
		this.label = label;
	}
	
	public String toString() {
		return label;
	}	
}
