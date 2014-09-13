package net.jcip.examples;

/**
 * ResourceFactory
 * <p/>
 * Lazy initialization holder class idiom
 * 
 * @author Brian Goetz and Tim Peierls
 */
public class ResourceFactory {
	private static class ResourceHolder {
		public static Resource resource = new Resource();
	}

	public static Resource getResource() {
		return ResourceFactory.ResourceHolder.resource;
	}

	static class Resource {
	}
}
