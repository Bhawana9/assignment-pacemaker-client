package controllers;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import asg.cliche.Command;
import asg.cliche.Param;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import models.Activity;
import models.DistanceLeaderBoard;
import models.Message;
import models.User;
import parsers.AsciiTableParser;
import parsers.Parser;

public class PacemakerConsoleService {

  private PacemakerAPI paceapi = new PacemakerAPI("http://localhost:7000");
  private Parser console = new AsciiTableParser();
  private User loggedInUser = null;
  private Message sentmessage=null;
  
 

  public PacemakerConsoleService() {}

  // Starter Commands

  @Command(description = "Register: Create an account for a new user")
  public void register(@Param(name = "first name") String firstName,
      @Param(name = "last name") String lastName, @Param(name = "email") String email,
      @Param(name = "password") String password) {
    console.renderUser(paceapi.createUser(firstName, lastName, email, password));
  }

  @Command(description = "List Users: List all users emails, first and last names")
  public void listUsers() {
    console.renderUsers(paceapi.getUsers());
  }

  @Command(description = "Login: Log in a registered user in to pacemaker")
  public void login(@Param(name = "email") String email,@Param(name = "password") String password) {
    Optional<User> user = Optional.fromNullable(paceapi.getUserByEmail(email));
    if (user.isPresent()) {
      if (user.get().password.equals(password)) {
        loggedInUser = user.get();
        console.println("Logged in " + loggedInUser.email);
        console.println("ok");
      } else {
        console.println("Error on login");
      }
    }
  }

  @Command(description = "Logout: Logout current user")
  public void logout() {
    console.println("Logging out " + loggedInUser.email);
    console.println("ok");
    loggedInUser = null;
  }

  @Command(description = "Add activity: create and add an activity for the logged in user")
  public void addActivity(@Param(name = "type") String type,
      @Param(name = "location") String location, @Param(name = "distance") double distance) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivity(paceapi.createActivity(user.get().id, type, location, distance));
    }
  }

  @Command(description = "List Activities: List all activities for logged in user")
  public void listActivities() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivities(paceapi.getActivities(user.get().id));
    }

  }

  // Baseline Commands

  @Command(description = "Add location: Append location to an activity")
  public void addLocation(@Param(name = "activity-id") String id,
      @Param(name = "longitude") double longitude, @Param(name = "latitude") double latitude) {
    Optional<Activity> activity = Optional.fromNullable(paceapi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      paceapi.addLocation(loggedInUser.getId(), activity.get().id, latitude, longitude);
      console.println("ok");
    } else {
      console.println("not found");
    }
  }

  @Command(description = "ActivityReport: List all activities for logged in user, sorted alphabetically by type")
  public void activityReport() {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      console.renderActivities(paceapi.listActivities(user.get().id, "type"));
    }
  }

  @Command(description = "Activity Report: List all activities for logged in user by type. Sorted longest to shortest distance")
  public void activityReport(@Param(name = "byType: type") String type) {
    Optional<User> user = Optional.fromNullable(loggedInUser);
    if (user.isPresent()) {
      List<Activity> reportActivities = new ArrayList<>();
      Collection<Activity> usersActivities = paceapi.getActivities(user.get().id);
      usersActivities.forEach(a -> {
        if (a.type.equals(type))
          reportActivities.add(a);
      });
      reportActivities.sort((a1, a2) -> {
        if (a1.distance >= a2.distance)
          return -1;
        else
          return 1;
      });
      console.renderActivities(reportActivities);
    }
  }

  @Command(description = "List all locations for a specific activity")
  public void listActivityLocations(@Param(name = "activity-id") String id) {
 
    Optional<Activity> activity = Optional.fromNullable(paceapi.getActivity(loggedInUser.getId(), id));
    if (activity.isPresent()) {
      // console.renderLocations(activity.get().route);
    }
  }
  
  @Command(description = "Follow Friend: Follow a specific friend")
  public void follow(@Param(name = "email") String email) 
  {
	  Optional<User> user = Optional.fromNullable(loggedInUser);
	  
	  if (user.isPresent()) 
		  if(user.get().email.equals(email))
	  {
			  console.println("You can't follow yourself");
	  }
		  else
		  {
			  console.println(paceapi.followFriend(user.get().id, email));
			  console.println("Sucessfully followed");
		  }
	}
  @Command(description = "List Friends: List all of the friends of the logged in user")
  public void listFriends() 
  {
Optional<User> user = Optional.fromNullable(loggedInUser);

	  if(user.isPresent())
	 {
		  console.println(paceapi.listFriends(user.get().id).toString());
	 }
	  
	 }
  
  @Command(description = "Friend Activity Report: List all activities of specific friend, sorted alphabetically by type)")
  public void friendActivityReport(@Param(name = "email") String email) 
  {
	  Optional<String>user=Optional.fromNullable(loggedInUser.getEmail());
	  	 
	    if (user.isPresent()) 
	    {
	      console.renderActivities(paceapi.friendActivityReport(email,"type"));
	    }
	  }  
 
  

  // Good Commands

  @Command(description = "Unfollow Friends: Stop following a friend")
  public void unfollowFriend(@Param(name = "email") String email) 
  {
	  Optional<User>user=Optional.fromNullable(loggedInUser);
		 
	    if (user.isPresent()) 
	    {
	    	paceapi.unfollowFriend(user.get().id, email);
	    }
  }

  @Command(description = "Message Friend: send a message to a friend")
  public void messageFriend(@Param(name = "email") String email,@Param(name = "message") String message)
  {
	  Optional<Message>getmessage=Optional.fromNullable(sentmessage);
	  console.renderMessage(paceapi.messageFriend(getmessage.get().reciever, getmessage.get()));
	  
  }

  @Command(description = "List Messages: List all messages for the logged in user")
  public void listMessages() 
  {
	  Optional<User> user = Optional.fromNullable(loggedInUser);

	  if(user.isPresent())
	 {
		  console.renderMessages(paceapi.listMessage(user.get().email));
	 }

	  
  }
	  

  @Command(description = "Distance Leader Board: list summary distances of all friends, sorted longest to shortest")
  public void distanceLeaderBoard(@Param(name="distance")Double distance) 
  {
	  Optional<User> user = Optional.fromNullable(loggedInUser);
	  List<User>friend=null;
	 Activity activity=new Activity();
	    if (user.isPresent()) 
	    {
	    	List<Activity> summaryActivities = new ArrayList<>();
	    	String friendsActivities=paceapi.distanceLeaderBoard(user.get().id, user.get().email, activity.distance);
	    	//Map<Double,Activity>dis2friends=friend.stream().collect(Collectors.toMap(Activity::getId, distance);
	    	
	    	
	    	
	    	summaryActivities.sort((a1, a2) -> {
	            if (a1.distance >= a2.distance)
	              return -1;
	            else
	              return 1;
	          });
	          console.renderdistanceActivities(friendsActivities);
	        }
	      }
	    
  
	  
   // Excellent Commands

  @Command(description = "Distance Leader Board: distance leader board refined by type")
  public void distanceLeaderBoardByType(@Param(name = "byType: type") String type) 
  {
	  Optional<User> user = Optional.fromNullable(loggedInUser);
	  Activity activity=new Activity();
	    if (user.isPresent()) {
	      List<Activity> summaryActivities = new ArrayList<>();
	      String usersActivities = paceapi.distanceLeaderBoard(user.get().id, user.get().email, activity.distance);
//	      //usersActivities.forEach(a -> {
//	        if (a.type.equals(type))
//	          summaryActivities.add(a);
//	      });
	    }
  }

  @Command(description = "Message All Friends: send a message to all friends")
  public void messageAllFriends(@Param(name = "message") String message) 
  {
	  
	  Optional<Message>getmessage=Optional.fromNullable(sentmessage);
	  console.renderMessage(paceapi.messageallFriend(getmessage.get()));
	  
  }

  @Command(description = "Location Leader Board: list sorted summary distances of all friends in named location")
  public void locationLeaderBoard(@Param(name = "location") String message) {}

  // Outstanding Commands

  // Todo
}
