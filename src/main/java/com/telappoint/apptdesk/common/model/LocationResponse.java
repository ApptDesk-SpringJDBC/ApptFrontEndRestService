package com.telappoint.apptdesk.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.apptdesk.model.Location;

@JsonSerialize(include = Inclusion.NON_NULL)
public class LocationResponse extends BaseResponse {
	private List<Location> locations;

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
