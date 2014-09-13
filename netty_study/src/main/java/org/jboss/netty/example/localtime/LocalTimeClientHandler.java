package org.jboss.netty.example.localtime;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.example.localtime.LocalTimeProtocol.Continent;
import org.jboss.netty.example.localtime.LocalTimeProtocol.Location;
import org.jboss.netty.example.localtime.LocalTimeProtocol.Locations;

public class LocalTimeClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger.getLogger(LocalTimeClient.class
			.getSimpleName());

	public List<String> getLocalTimes(Collection<String> cities) {
		Locations.Builder builder = Locations.newBuilder();

		for (String c : cities) {
			String[] components = c.split("/");
			builder.addLocation(Location
					.newBuilder()
					.setContinent(
							Continent.valueOf(components[0].toUpperCase()))
					.setCity(components[1]).build());
		}
		
		return null;
	}

}
