package rob.ors.core.model.impl;


// Generated Jul 13, 2012 11:58:30 AM by Hibernate Tools 3.4.0.CR1

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rob.ors.core.model.api.AbstractOutfit;
import rob.ors.core.model.api.ConcreteOutfit;
import rob.ors.core.model.api.Member;

/**
 * Member generated by hbm2java
 */
public class MemberI implements java.io.Serializable, Member {

	private Integer id;
	private String name;
	private Set<rob.ors.core.model.api.ConcreteOutfit> memberOutfits = new HashSet(0);
	private Set<rob.ors.core.model.api.ConcreteOutfit> likedOutfits = new HashSet(0);
	private Set<Member> following = new HashSet<Member>(0);
	private Set<Member> followers = new HashSet<Member>(0);
	private Date date;
	private int numOutfitViews;
	private int numOutfitLikes;
	private int numTrophies;
	private int numFollowers;
	private boolean complete;
	protected MemberI() {}

	public MemberI(Integer id, String name)
	{
		this.id = id;
		this.name = name;	
		this.date = new Date();
	}

	
	@Override
	public Integer getId()
	{
		return this.id;
	}

	
	@Override
	public void setId(Integer id)
	{
		if(id == null) throw new IllegalArgumentException("Member id is null");
		this.id = id;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public void setName(String name)
	{
		if(name == null) throw new IllegalArgumentException("Member name is null");
		this.name = name;
	}	
	
	@Override
	public Set<rob.ors.core.model.api.ConcreteOutfit> getMemberOutfits()
	{
		return memberOutfits;
	}

	private void setMemberOutfits(Set<rob.ors.core.model.api.ConcreteOutfit> memberOutfits)
	{
		this.memberOutfits = memberOutfits;
	}

	@Override
	public Set<rob.ors.core.model.api.ConcreteOutfit> getLikedOutfits()
	{
		return likedOutfits;
	}

	private void setLikedOutfits(Set<rob.ors.core.model.api.ConcreteOutfit> likedOutfits)
	{
		this.likedOutfits = likedOutfits;
	}
	
	
	@Override
	public void addLikedOutfit(rob.ors.core.model.api.ConcreteOutfit outfit)
	{
		if(outfit == null) throw new IllegalArgumentException("Set is null");
		outfit.getMembersLiked().add(this);
		this.likedOutfits.add(outfit);		
	}
	
	@Override
	public Set<Member> getFollowing()
	{
		return this.following;
	}


	private void setFollowing(Set<Member> following)
	{
		this.following=following;		
	}


	@Override
	public void addFollowing(Member member)
	{
		if(member==null) throw new IllegalArgumentException("Member is null");
		this.following.add(member);
		member.getFollowers().add(this);
	}
	
	@Override
	public Set<Member> getFollowers()
	{
		return this.followers;
	}

	private void setFollowers(Set<Member> followers)
	{
		this.followers = followers;
		
	}

	
	@Override
	public Date getDate()
	{
		return date;
	}

	@Override
	public void setDate(Date newDate)
	{
		this.date = newDate;		
	}


	
	
	public boolean getComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isOlder(int days)
	{
		Calendar cal = new GregorianCalendar();
		cal.setTime((Date) date.clone());
		cal.add(Calendar.DAY_OF_YEAR, days);
		if(cal.compareTo(new GregorianCalendar())<0) return true;
		return false;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberI other = (MemberI) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public int getNumOutfitViews() {
		return numOutfitViews;
	}

	public int getNumOutfitLikes() {
		return numOutfitLikes;
	}

	public int getNumTrophies() {
		return numTrophies;
	}

	public void setNumOutfitViews(int numOutfitViews) {
		this.numOutfitViews = numOutfitViews;
	}

	public void setNumOutfitLikes(int numOutfitLikes) {
		this.numOutfitLikes = numOutfitLikes;
	}

	public void setNumTrophies(int numTrophies) {
		this.numTrophies = numTrophies;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}
	
	
}
