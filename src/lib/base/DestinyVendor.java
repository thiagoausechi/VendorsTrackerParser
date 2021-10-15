package lib.base;

import java.util.HashMap;

import org.apache.maven.shared.utils.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lib.JSONFactory;
import lib.References.Guardian;
import lib.base.DestinyItemArmor.ArmorType;
import material.DestinyAPI;

public class DestinyVendor implements JSONFactory
{
	protected HashMap<String, String>		properties;
	// hash, name, location, color, icon, large_icon, map_icon

	protected HashMap<Guardian, VendorSale>	sales;

	public DestinyVendor(String name, String hash)
	{
		this.properties = new HashMap<String, String>();
		sales = new HashMap<>();

		this.properties.put("name", name);
		this.properties.put("hash", hash);

		for (Guardian guardian : Guardian.values())
			sales.put(guardian, new VendorSale());

		this.setAllIcons();
	}

	public String getHash()
	{
		return this.properties.get("hash");
	}

	public String getName()
	{
		return this.properties.get("name");
	}

	public DestinyVendor setLocation(String location)
	{
		this.properties.put("location", location);
		return this;
	}

	public String getLocation()
	{
		return this.properties.get("location");
	}

	public DestinyVendor setColor(String color)
	{
		this.properties.put("color", color);
		return this;
	}

	public String getColor()
	{
		return this.properties.get("color");
	}

	public DestinyVendor setIcon(String type, String url)
	{
		this.properties.put(type, url);
		return this;
	}

	public String getIcon(String type)
	{
		return this.properties.get(type);
	}

	public DestinyItemArmor getSale(Guardian guardian, ArmorType type)
	{
		return getSales(guardian).getItems().get(type.ordinal());
	}

	public VendorSale getSales(Guardian guardian)
	{
		return sales.get(guardian);
	}

	public DestinyVendor addSale(Guardian guardian, DestinyItemArmor sale)
	{
		sales.get(guardian).addItem(sale);
		return this;
	}

	public void sort()
	{
		for (Guardian guardian : sales.keySet())
			sales.get(guardian).getItems().sort((o1, o2) -> o1.type.compareTo(o2.type));
	}

	private void setAllIcons()
	{
		String	   url			 = "https://www.bungie.net/Platform/Destiny2/Manifest/DestinyVendorDefinition/%s/".formatted(getHash());
		JsonObject response		 = DestinyAPI.getHttpUtils().urlRequestGETOauth(url).getAsJsonObject("Response");
		JsonObject display_props = response.getAsJsonObject("displayProperties");

		setIcon("icon", "https://www.bungie.net" + display_props.get("smallTransparentIcon").getAsString());
		setIcon("large_icon", "https://www.bungie.net" + display_props.get("largeTransparentIcon").getAsString());
		setIcon("map_icon", "https://www.bungie.net" + display_props.get("mapIcon").getAsString());
	}

	@Override
	public JsonObject export()
	{
		JsonObject result = new JsonObject();

		for (String prop : properties.keySet())
			result.addProperty(prop, properties.get(prop));

		JsonArray guardians = new JsonArray();

		for (Guardian guardian : sales.keySet())
		{
			JsonObject guardian_jo = new JsonObject();

			guardian_jo.addProperty("name", StringUtils.capitalise(guardian.name().toLowerCase()));
			guardian_jo.add("sales", sales.get(guardian).export());

			guardians.add(guardian_jo);
		}

		result.add("guardians", guardians);

		return result;
	}
}
