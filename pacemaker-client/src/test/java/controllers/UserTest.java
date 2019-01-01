package controllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import models.User;
import static models.Fixtures.users;
import static models.Fixtures.friends;
import static models.Fixtures.margelistFriends;

public class UserTest {

  PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
  ;
   
   @Before
  public void setup() {
    pacemaker.deleteUsers();
    
  }

  @After
  public void tearDown() {
  }
  
  @Test
  public void testCreateUser() {
    User user = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    assertEquals(user, homer);
    User user2 = pacemaker.getUserByEmail(homer.email);
    assertEquals(user2, homer);
  }

  @Test
  public void testCreateUsers() {
    users.forEach(
        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
    Collection<User> returnedUsers = pacemaker.getUsers();
    assertEquals(users.size(), returnedUsers.size());
  }
  @Test
  public void testDeleteUsers()
  {
	  users.forEach(
		        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	  Collection<User> returnedUsers = pacemaker.getUsers();
	    assertEquals(users.size(), returnedUsers.size());
	    User marge=pacemaker.getUserByEmail("marge@simpson.com");
	    pacemaker.deleteUser(marge.email);
	    assertEquals(users.size(),pacemaker.getUsers().size());
	    
  }
  
  @Test
  public void testDeleteUserById()
  {
	  users.forEach(
		        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	  Collection<User> returnedUsers = pacemaker.getUsers();
	    assertEquals(users.size(), returnedUsers.size());
	    User marge=pacemaker.getUser(homer.id);
	    pacemaker.deleteUser(marge.id);
	    assertEquals(users.size(),pacemaker.getUsers().size());
	    
  }
@Test
public void getUsers()
{
	 users.forEach(
		        user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	  Collection<User> returnedUsers = pacemaker.getUsers();
	    assertEquals(users.size(), returnedUsers.size());
	    assertEquals(users.size(),pacemaker.getUsers().size());
	    
}
@Test
public void getUserByEmail()
{
	User usertest = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
	User returnedUsers = pacemaker.getUserByEmail(usertest.email);
    assertEquals(homer.email, returnedUsers);
}

@Test
public void getUserById()
{
	User usertest = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
	
  User returnedUsers = pacemaker.getUser(usertest.id);
  assertEquals(homer.id, returnedUsers);
}
    
  @Test
  public void followFriend()
  {
	  pacemaker.deleteUser(homer.id);
	  
	   
	  friends.forEach(
			  friend->pacemaker.followFriend(friend.id, friend.email));
	   
	  String returnedUsers = pacemaker.followFriend(homer.id, homer.email);
	 
	  assertEquals(friends.get(0),returnedUsers.length());
	  
  }
  
  @Test
  public void unfollowFriend()
  {
	  friends.forEach(
		        friend -> pacemaker.followFriend(friend.id, friend.email));
	  Collection<User> returnedUsers = pacemaker.getUsers();
	  assertEquals(friends.size(), returnedUsers.size());
	  User marge=pacemaker.getUserByEmail("marge@simpson.com");
	   pacemaker.unfollowFriend(marge.id, marge.email);
	    assertEquals(marge,returnedUsers.size());
  }
  
  @Test
  public void getListFriend()
  {
	  friends.forEach(
		        friend -> pacemaker.followFriend(friend.id, friend.email));
	  	  
	  String friendslist1=pacemaker.followFriend(homer.id, homer.email);
	  Collection<User> friendslist2=pacemaker.listFriends(friendslist1);
	  assertEquals(friendslist1,friendslist2);
  }
  
}