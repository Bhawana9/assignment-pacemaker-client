package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Activity;
import models.Message;
import models.User;

public class MessageTest 
{   
	PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
	  User homer = new User("homer", "simpson", "homer@simpson.com", "secret");
	  @Before
	  public void setup() {
	    pacemaker.deleteUsers();
	    homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
	  }

	  @After
	  public void tearDown() {}
	  
	  @Test
	  public void testMessageFriend() 
	  {
		  Message message=new Message("bart@simpson.com","lisa@simpson.com","HelloCheck");
		  Message sentMessage=pacemaker.messageFriend( message.reciever,message);
		  assertEquals(message.sender,sentMessage.sender);
		  assertEquals(message.reciever,sentMessage.reciever);
		  assertEquals(message.message,sentMessage.message,"HelloCheck");
		  assertNotNull(sentMessage.message);
	  }
	  @Test
	  public void testGetMessage() {
		  Message message=new Message("bart@simpson.com","lisa@simpson.com","HelloCheck");
	    Message sentMessage = pacemaker.messageFriend(message.reciever, message);
	    Collection<Message> sentMessage1=pacemaker.listMessage(message.message);
	    assertNotEquals(sentMessage,sentMessage1);
	    
}
}