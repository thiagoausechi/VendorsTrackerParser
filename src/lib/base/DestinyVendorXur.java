package lib.base;

import java.util.HashMap;

import org.apache.maven.shared.utils.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lib.References.Guardian;
import lib.Utils;

public class DestinyVendorXur extends DestinyVendor
{
	private HashMap<Guardian, DestinyItemArmor> exotics;

	public DestinyVendorXur(String name, String hash)
	{
		super(name, hash);

		this.exotics = new HashMap<>();
		this.setLocation(Utils.XUR_LOCATION.get("locationName").getAsString());
	}

	public DestinyItemArmor getExoticSale(Guardian guardian)
	{
		return this.exotics.get(guardian);
	}

	public DestinyVendor setExoticSale(Guardian guardian, DestinyItemArmor exotic)
	{
		this.exotics.putIfAbsent(guardian, exotic);
		return this;
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
			guardian_jo.add("sales", exotics.get(guardian).export());
			guardian_jo.add("sales", sales.get(guardian).export());

			guardians.add(guardian_jo);
		}

		result.add("guardians", guardians);

		return result;
	}

}
