package Models;

import java.util.UUID;

/**
 * 
 * @author A-Team 71
 */
public class FoodList {
	
	private BPTree<UUID, FoodDataItem> data; // IDK about UUID

	public void setData(BPTree<UUID, FoodDataItem> data) {
		this.data = data;
	}
	
	public BPTree<UUID, FoodDataItem> getData() {
		return this.data;
	}
}



