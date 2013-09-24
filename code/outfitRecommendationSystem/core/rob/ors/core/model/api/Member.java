package rob.ors.core.model.api;
import java.util.Map;
import java.util.Set;


public interface Member extends ElementAge {

	public abstract Integer getId();

	public abstract void setId(Integer id);

	public abstract String getName();

	public abstract void setName(String name);
	
	public Set<rob.ors.core.model.api.ConcreteOutfit> getMemberOutfits();

	public abstract void addLikedOutfit(rob.ors.core.model.api.ConcreteOutfit outfit);
	
	public Set<rob.ors.core.model.api.ConcreteOutfit> getLikedOutfits();
	
	public abstract void addFollowing(Member member);
	public boolean getComplete();
	public void setComplete(boolean complete);
	public abstract Set<Member> getFollowing();
	
	public abstract Set<Member> getFollowers();
	

	public int getNumOutfitViews();
	public void setNumOutfitViews(int num);

	public int getNumOutfitLikes();
	public void setNumOutfitLikes(int num);

	public int getNumTrophies();
	public void setNumTrophies(int num);


	
	public int getNumFollowers();
	public void setNumFollowers(int numFollowers);

}