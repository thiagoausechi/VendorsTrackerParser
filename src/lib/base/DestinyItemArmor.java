package lib.base;

import java.util.HashMap;

import org.apache.maven.shared.utils.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lib.JSONFactory;
import material.inventory.DestinyItem;

public class DestinyItemArmor extends DestinyItem implements JSONFactory
{
	String									seasonWatermark;
	ArmorType								type;
	protected HashMap<ArmorStatus, Integer>	status;

	public DestinyItemArmor(String hashID, ArmorType type)
	{
		super(hashID);
		this.type = type;
		status = new HashMap<ArmorStatus, Integer>();
	}

	public String getSeasonWatermark()
	{
		return seasonWatermark;
	}

	public void setSeasonWatermark(String seasonWatermark)
	{
		this.seasonWatermark = seasonWatermark;
	}

	public void setStatus(int m, int r, int e, int d, int i, int s)
	{
		this.setMobility(m);
		this.setResilience(r);
		this.setRecovery(e);
		this.setDiscipline(d);
		this.setIntellect(i);
		this.setStrenght(s);
	}

	public boolean setStatus(JsonObject stats_list)
	{

		if (stats_list == null)
			return false;
		else
		{
			for (ArmorStatus atribute : ArmorStatus.values())
				status.put(atribute, getStatus(atribute, stats_list));
			return true;
		}
	}

	public int getStatus(ArmorStatus atribute, JsonObject stats_list)
	{
		return stats_list.getAsJsonObject(atribute.hash()).get("value").getAsInt();
	}

	public int getTotalStatus()
	{
		int total = 0;
		for (ArmorStatus atribute : status.keySet())
			total += status.get(atribute);
		return total;
	}

	public int getMobility()
	{
		return this.status.get(ArmorStatus.MOBILITY);
	}

	public void setMobility(int value)
	{
		this.status.put(ArmorStatus.MOBILITY, value);
	}

	public int getResilience()
	{
		return this.status.get(ArmorStatus.RESILIENCE);
	}

	public void setResilience(int value)
	{
		this.status.put(ArmorStatus.RESILIENCE, value);
	}

	public int getRecovery()
	{
		return this.status.get(ArmorStatus.RECOVERY);
	}

	public void setRecovery(int value)
	{
		this.status.put(ArmorStatus.RECOVERY, value);
	}

	public int getDiscipline()
	{
		return this.status.get(ArmorStatus.DISCIPLINE);
	}

	public void setDiscipline(int value)
	{
		this.status.put(ArmorStatus.DISCIPLINE, value);
	}

	public int getIntellect()
	{
		return this.status.get(ArmorStatus.INTELLECT);
	}

	public void setIntellect(int value)
	{
		this.status.put(ArmorStatus.INTELLECT, value);
	}

	public int getStrenght()
	{
		return this.status.get(ArmorStatus.STRENGHT);
	}

	public void setStrenght(int value)
	{
		this.status.put(ArmorStatus.STRENGHT, value);
	}

	public ArmorType getType()
	{
		return this.type;
	}

	public enum ArmorType
	{
		HELMET, GAUNTLETS, CHEST, LEG
	}

	// ========================================================================================================================================================================================
	public enum ArmorStatus
	{
		MOBILITY("2996146975"), RESILIENCE("392767087"), RECOVERY("1943323491"), DISCIPLINE("1735777505"), INTELLECT("144602215"), STRENGHT("4244567218");

		// TODO Fix formatter
		private String hash;

		ArmorStatus(String hash)
		{
			this.hash = hash;
		}

		public String hash()
		{
			return this.hash;
		}

		public String getName()
		{
			return StringUtils.capitalise(this.name().toLowerCase());
		}
	}

	public static String[] getValiableArmorSlot()
	{
		return new String[] { "Helmet" ,"Gauntlets" ,"Chest Armor" ,"Leg Armor" };
	}

	public static ArmorType getArmorType(String type)
	{
		if (type.contains("Helmet"))
			return ArmorType.HELMET;
		if (type.contains("Gauntlets"))
			return ArmorType.GAUNTLETS;
		if (type.contains("Chest"))
			return ArmorType.CHEST;
		if (type.contains("Leg"))
			return ArmorType.LEG;
		else
			return null;
	}

	public JsonArray getStatusForExport()
	{
		JsonArray result = new JsonArray();

		for (ArmorStatus atribute : status.keySet())
			result.add(getStatusForExport(atribute));

		return result;
	}

	public JsonObject getStatusForExport(ArmorStatus atribute)
	{
		JsonObject result = new JsonObject();

		result.addProperty("name", atribute.getName());
		result.addProperty("value", status.get(atribute));

		return result;
	}

	@Override
	public JsonObject export()
	{
		JsonObject result = new JsonObject();
		JsonObject item	  = new JsonObject();

		item.addProperty("hash", getHashID());
		item.addProperty("name", getName());
		item.addProperty("icon", "https://www.bungie.net" + getIcon());
		item.addProperty("watermark", "https://www.bungie.net" + getSeasonWatermark());

		result.add("item", item);
		result.add("status", getStatusForExport());
		return result;
	}
}
