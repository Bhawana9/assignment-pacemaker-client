package controllers;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static models.Fixtures.route1;
import static models.Fixtures.route2;
import models.Activity;
import models.Location;
import models.User;

public class LocationTest 
{
	PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
	  Activity activity = new Activity("walk", "shop", 2.5);

	  @Before
	  public void setup() {
	    pacemaker.deleteUsers();
	    pacemaker.deleteActivities(homer.id);
	    
	  }
	  @After
	  public void tearDown() {
	  }

	  @Test
	  public void testLocationCreate() {
		  Location location = new Location(23.3,33.3);
		  pacemaker.addLocation(homer.id, activity.id, location.latitude, location.longitude);
		    assertEquals(0.01,23.3,route1.get(0).latitude);
	    assertEquals(0.01,33.3,route2.get(1).longitude);
	      
	  }
	  @Test
	  public void testgetLocation() 
	  {
		    Location location = new Location(23.3,33.3);
		 pacemaker.addLocation(homer.id, activity.id, location.latitude, location.longitude);
		    List<Location> returnedActivity2 = pacemaker.getLocations(homer.id, activity.id);
		    assertEquals(location.id, returnedActivity2);
		  }
	

	  }
 

