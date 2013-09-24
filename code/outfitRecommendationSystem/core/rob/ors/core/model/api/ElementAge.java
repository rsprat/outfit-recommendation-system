package rob.ors.core.model.api;

import java.util.Date;

public interface ElementAge {
	
	public Date getDate();
	
	public void setDate(Date newDate);
	
	/**
	 * Check if the element is older than the given days value.
	 * @param days
	 * @return
	 */
	public boolean isOlder(int days);
	
	

}
