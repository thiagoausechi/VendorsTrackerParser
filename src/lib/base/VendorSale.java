package lib.base;

import java.util.ArrayList;

import com.google.gson.JsonArray;

import lib.JSONFactory;

public class VendorSale implements JSONFactory
{
	private ArrayList<DestinyItemArmor> items;

	public VendorSale()
	{
		items = new ArrayList<DestinyItemArmor>();
	}

	public ArrayList<DestinyItemArmor> getItems()
	{
		return items;
	}

	public ArrayList<DestinyItemArmor> addItem(DestinyItemArmor item)
	{
		items.add(item);
		return items;
	}

	@Override
	public JsonArray export()
	{
		JsonArray result = new JsonArray();

		for (DestinyItemArmor item : items)
			result.add(item.export());

		return result;
	}

}
