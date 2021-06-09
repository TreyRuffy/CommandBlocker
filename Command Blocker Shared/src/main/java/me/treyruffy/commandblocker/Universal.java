package me.treyruffy.commandblocker;

public class Universal {

	private static Universal instance = null;
	private MethodInterface mi;
	
	public static Universal get() {
		return instance == null ? instance = new Universal() : instance;
	}
	
	public void setup(MethodInterface mi) {
		this.mi = mi;
		
		mi.setupMetrics();
	}
	
	public MethodInterface getMethods() {
        return mi;
    }
	
}
