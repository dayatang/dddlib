class Hello {
	public static String hello() {
		return "Hello";
	}
}

class  World {
	public static String world() {
		return "world";
	}
}

println(Hello.hello() + " " + World.world() + termination());

def termination() {
	return "!";
}