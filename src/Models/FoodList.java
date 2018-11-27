package Models;

import java.util.UUID;

public class FoodList {
	private BPTree<UUID, FoodItem> data; // IDK about UUID

	public void setData(BPTree<UUID, FoodItem> data) {
		this.data = data;
	}
	
	public BPTree<UUID, FoodItem> getData() {
		return this.data;
	}
}



