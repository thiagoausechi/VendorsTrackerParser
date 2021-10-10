package lib.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lib.JSONFactory;
import lib.References;
import material.inventory.DestinyItem;

public class DestinyItemArmor extends DestinyItem implements JSONFactory
{
	String	  seasonWatermark;
	ArmorType type;
	int		  mobility, resilience, recovery, discipline, intellect, strenght;
	// TODO Make this HashMap!

	public DestinyItemArmor(String hashID, ArmorType type)
	{
		super(hashID);
		this.type = type;
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
			int	m = getStatus(References.MOBILITY, stats_list);
			int	r = getStatus(References.RESILIENCE, stats_list);
			int	e = getStatus(References.RECOVERY, stats_list);
			int	d = getStatus(References.DISCIPLINE, stats_list);
			int	i = getStatus(References.INTELLECT, stats_list);
			int	s = getStatus(References.STRENGHT, stats_list);
			setStatus(m, r, e, d, i, s);
			return true;
		}
	}

	public int getStatus(String atribute, JsonObject stats_list)
	{
		return stats_list.getAsJsonObject(atribute).get("value").getAsInt();
	}

	public int getTotalStatus()
	{
		return mobility + resilience + recovery + discipline + intellect + strenght;
	}

	public int getMobility()
	{
		return mobility;
	}

	public void setMobility(int mobility)
	{
		this.mobility = mobility;
	}

	public int getResilience()
	{
		return resilience;
	}

	public void setResilience(int resilience)
	{
		this.resilience = resilience;
	}

	public int getRecovery()
	{
		return recovery;
	}

	public void setRecovery(int recovery)
	{
		this.recovery = recovery;
	}

	public int getDiscipline()
	{
		return discipline;
	}

	public void setDiscipline(int discipline)
	{
		this.discipline = discipline;
	}

	public int getIntellect()
	{
		return intellect;
	}

	public void setIntellect(int intellect)
	{
		this.intellect = intellect;
	}

	public int getStrenght()
	{
		return strenght;
	}

	public void setStrenght(int strenght)
	{
		this.strenght = strenght;
	}

	public ArmorType getType()
	{
		return this.type;
	}

	public enum ArmorType
	{
		HELMET, GAUNTLETS, CHEST, LEG
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
		JsonArray	 result	= new JsonArray();
		JsonObject[] status	= new JsonObject[] { getStatusForExport("Mobility") ,getStatusForExport("Resilience") ,
			getStatusForExport("Recovery") ,getStatusForExport("Discipline") ,getStatusForExport("Intellect") ,
			getStatusForExport("Strength") , };

		for (JsonObject stats : status)
			result.add(stats);

		return result;
	}

	public JsonObject getStatusForExport(String name)
	{
		JsonObject result = new JsonObject();

		switch (name) // TODO Improve for some loop?
		{
			case "Mobility": {
				result.addProperty("name", "Mobility");
				result.addProperty("value", mobility);
				return result;
			}
			case "Resilience": {
				result.addProperty("name", "Resilience");
				result.addProperty("value", resilience);
				return result;
			}
			case "Recovery": {
				result.addProperty("name", "Recovery");
				result.addProperty("value", recovery);
				return result;

			}
			case "Discipline": {
				result.addProperty("name", "Discipline");
				result.addProperty("value", discipline);
				return result;
			}
			case "Intellect": {
				result.addProperty("name", "Intellect");
				result.addProperty("value", intellect);
				return result;
			}
			case "Strength": {
				result.addProperty("name", "Strength");
				result.addProperty("value", strenght);
				return result;
			}
		}

		return null;
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
