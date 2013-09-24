package rob.ors.core.model.api;

import java.util.Map;

public interface Garment {

	public abstract Integer getId();

	public abstract void setId(Integer id);	

	public abstract Category getCategory();

	public abstract Integer getAge();

	public abstract void setAge(Integer age);

	public abstract Integer getViews();

	public abstract void setViews(Integer views);

	public abstract Integer getSaves();

	public abstract void setSaves(Integer saves);

	public abstract Map<Integer, Integer> getAttributes();
	


}