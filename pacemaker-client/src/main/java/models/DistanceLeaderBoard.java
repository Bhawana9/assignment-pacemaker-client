package models;

import java.io.Serializable;

public class DistanceLeaderBoard  implements Serializable
{
public String users;
public String friends;
public Double distance;

public DistanceLeaderBoard(String users,String friends,Double distance)
{
this.users=users;
this.friends=friends;
this.distance=distance;
}
}
