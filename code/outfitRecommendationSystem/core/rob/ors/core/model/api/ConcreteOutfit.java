package rob.ors.core.model.api;

public interface ConcreteOutfit extends Outfit, ElementAge {

	public abstract void addGarment(ConcreteGarment garment);
	
	public abstract java.util.Set<ConcreteGarment> getGarments();
	
	public void removeGarment(ConcreteGarment garment);
	
	public abstract Member getMember();
	
	public void setMember(Member member);
	
	public abstract void addMemberLiked(Member member);
	
	public abstract java.util.Set<Member> getMembersLiked();
	
	public abstract void setAbstractoutfit(AbstractOutfit outfit);
	
	public abstract AbstractOutfit getAbstractOutfit();
}