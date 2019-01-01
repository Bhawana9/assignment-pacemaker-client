 package controllers;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Activity;
import models.DistanceLeaderBoard;

import models.Location;
import models.Message;

import models.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface PacemakerInterface {
  @GET("/users")
  Call<List<User>> getUsers();

  @DELETE("/users")
  Call<String> deleteUsers();

  @DELETE("/users/{id}")
  Call<User> deleteUser(@Path("id") String id);

  @GET("/users/{id}")
  Call<User> getUser(@Path("id") String id);

  @POST("/users")
  Call<User> registerUser(@Body User User);

  @GET("/users/{id}/activities")
  Call<List<Activity>> getActivities(@Path("id") String id);

  @POST("/users/{id}/activities")
  Call<Activity> addActivity(@Path("id") String id, @Body Activity activity);

  @GET("/users/{id}/activities/{activityId}")
  Call<Activity> getActivity(@Path("id") String id, @Path("activityId") String activityId);

  @POST("/users/{id}/activities/{activityId}/locations")
  Call<Location> addLocation(@Path("id") String id, @Path("activityId") String activityId,
      @Body Location location);
  
  @DELETE("/users/{id}/activities")
  Call<String> deleteActivities(@Path("id") String id);
  
  @GET("/users/{id}/activities/{activityid}/location")
  Call<List<Location>> getlocation(@Path("id") String id,@Path("activityid")String activityid);

  @GET("/users/{id}/friend/{email}")
  Call<String> followFriend(@Path("id")String id, @Path("email") String emailid);
  
@GET("/users/{id}/friendlist")
Call<List<User>> listFriends(@Path("id")String id);


@GET("/users/{id}/friends/{email}")
Call<String> unfollowFriend(@Path("id") String id,@Path("email") String email);

@GET("/users/{email}/friends/activities/{activityId}")
Call<List<Activity>> FriendgetActivities(@Path("email")String email, @Path("type")String type);


@POST("/users/{id}/message/{email}/{message}")
Call<Message> messageFriend(@Path("id") String id, @Path ("email")String email,@Path("message") String message);

@GET("/users/message/{email}")
Call<List<Message>> listMessages(@Path("email") String email);


@GET("/users/{id}/message/{email}")
Call<Message> messageallFriend(@Body Message message);

@POST("/users/{id}/friends/{activityId}/{distance}")
Call<String> distanceLeaderBoard(@Path("id")String id,@Path("activityId")String activityId,@Path("distance")Double distance);
}


public class PacemakerAPI {

  PacemakerInterface pacemakerInterface;

  public PacemakerAPI(String url) {
    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson)).build();

    pacemakerInterface = retrofit.create(PacemakerInterface.class);
  }

  public Collection<User> getUsers() {
    Collection<User> users = null;
    try {
      Call<List<User>> call = pacemakerInterface.getUsers();
      Response<List<User>> response = call.execute();
      users = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public User createUser(String firstName, String lastName, String email, String password) {
    User returnedUser = null;
    try {
      Call<User> call =
          pacemakerInterface.registerUser(new User(firstName, lastName, email, password));
      Response<User> response = call.execute();
      returnedUser = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedUser;
  }

  public Activity createActivity(String id, String type, String location, double distance) {
    Activity returnedActivity = null;
    try {
      Call<Activity> call =
          pacemakerInterface.addActivity(id, new Activity(type, location, distance));
      Response<Activity> response = call.execute();
      returnedActivity = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return returnedActivity;
  }

  public Collection<Activity> getActivities(String id) {
    Collection<Activity> activities = null;
    try {
      Call<List<Activity>> call = pacemakerInterface.getActivities(id);
      Response<List<Activity>> response = call.execute();
      activities = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activities;
  }
  
  public List<Activity> listActivities(String userId, String sortBy) {
    List<Activity>  activities=null;
    try
    {
    	Call<List<Activity>>call=pacemakerInterface.FriendgetActivities(userId, sortBy);
    	Response<List<Activity>>response=call.execute();
    	activities=response.body();
    }
    catch(Exception e)
    {
    	System.out.println(e.getMessage());
    }
    	
	return activities;
    
  }

  public Activity getActivity(String userId, String activityId) {
    Activity activity = null;
    try {
      Call<Activity> call = pacemakerInterface.getActivity(userId, activityId);
      Response<Activity> response = call.execute();
      activity = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return activity;
  }

  public void addLocation(String id, String activityId, double latitude, double longitude) {
    try {
      Call<Location> call =
          pacemakerInterface.addLocation(id, activityId, new Location(latitude, longitude));
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User getUserByEmail(String email) {
    Collection<User> users = getUsers();
    User foundUser = null;
    for (User user : users) {
      if (user.email.equals(email)) {
        foundUser = user;
      }
    }
    return foundUser;
  }

  public User getUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.getUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }
  
  public void deleteUsers() {
    try {
      Call<String> call = pacemakerInterface.deleteUsers();
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public User deleteUser(String id) {
    User user = null;
    try {
      Call<User> call = pacemakerInterface.deleteUser(id);
      Response<User> response = call.execute();
      user = response.body();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return user;
  }
  
  public void deleteActivities(String id) {
    try {
      Call<String> call = pacemakerInterface.deleteActivities(id);
      call.execute();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  
  public String followFriend(String id,String email)
  { 
	  String follow=null;
	  try
	  {
	  Call<String>call=pacemakerInterface.followFriend(id, email);
	  Response<String> response = call.execute();
      follow = response.body();
	  }
	  catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
    return follow;
  }  
  
  public Collection<User>listFriends(String userid) {
	  Collection<User>listfriend=null;
	  try
	  {
		 
		Call<List<User>>call=pacemakerInterface.listFriends(userid);
		Response<List<User>> response = call.execute();
	     listfriend = response.body();
	  }
	  catch(Exception e)
	  {
		  System.out.println(e.getMessage());
	  }
	return listfriend;
	
	    
	  }
  
  public List<Activity> friendActivityReport(String email,String type) {
	  List<Activity> activity = null;
	    try {
	      Call<List<Activity>> call = pacemakerInterface.FriendgetActivities(email, type);
	      Response<List<Activity>> response = call.execute();
	      activity = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return activity;
	  }
  

  public String unfollowFriend(String userId,String email) {
	    String friend = null;
	    try {
	      Call<String> call = pacemakerInterface.unfollowFriend(userId, email);
	      Response<String> response = call.execute();
	      friend = response.body();
	    } catch (Exception e) {
	      System.out.println(e.getMessage());
	    }
	    return friend;
	  }
  
  public Message messageFriend(String email,Message msg)
  {
	 Message message=null;
	 try {
		 Call<Message>call=pacemakerInterface.messageFriend(msg.sender, msg.reciever,msg.message);
		 Response<Message> response = call.execute();
	      message = response.body();
		 }
		 catch (IOException e) 
		 {
			e.printStackTrace();
		}
	return msg;
	
	}
  public Collection<Message> listMessage(String email)
  {

	Collection<Message>getmessage=null;
	try
	{
	  Call<List<Message>>call=pacemakerInterface.listMessages(email);
	  Response<List<Message>>response=call.execute();
	  getmessage=response.body();
	  
	}
	catch (Exception e) 
	 {
		
		System.out.println(e.getMessage());
	}
	return getmessage;
	 

  }

  public Message messageallFriend(Message msg)
  {
	 Message message=null;
	 try {
		 Call<Message>call=pacemakerInterface.messageallFriend(message);
		  call.execute();
	      
		 }
		 catch (IOException e) 
		 {
			e.printStackTrace();
		}
	return message;
	
	}

public List<Location> getLocations(String id, String activityid) 
{
	List<Location>location=null;
	try
	{
	  Call<List<Location>>call=pacemakerInterface.getlocation(id, activityid);
	  Response<List<Location>>response=call.execute();
	  location=response.body();
	  
	}
	catch (Exception e) 
	 {
		
		System.out.println(e.getMessage());
	}
	return location;
	 

}

public String distanceLeaderBoard(String users,String friends,Double distance)
{
	String summary=null;
	try
	{
	  Call<String>call=pacemakerInterface.distanceLeaderBoard(users,friends,distance);
	  Response<String>response=call.execute();
	  summary=response.body();
	  
	}
	catch (Exception e) 
	 { 
		
		System.out.println(e.getMessage());
	}
	return summary;
	 

}

}
