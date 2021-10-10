package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import init.D2VT;
import lib.References.Guardian;
import lib.base.DestinyItemArmor;
import material.DestinyAPI;
import material.manifest.ManifestEntityTypes;
import material.user.BungieUser;

public class Utils
{
	public static JsonObject[] requestAllVendorsRaw(BungieUser user)
	{
		JsonObject warlock = requestVendorRaw(user, Guardian.WARLOCK);
		JsonObject hunter  = requestVendorRaw(user, Guardian.HUNTER);
		JsonObject titan   = requestVendorRaw(user, Guardian.TITAN);
		return new JsonObject[] { warlock ,hunter ,titan };
		// TODO Fix Eclipse Syntax formatter
	}

	public static JsonObject requestVendorRaw(BungieUser user, Guardian guardian)
	{
		Logger.logFormatted("Making a Vendor request for %s (%s)", user.getGlobalDisplayName(), guardian.toString());

		String url = "https://www.bungie.net/Platform/Destiny2/%s/Profile/%s/Character/%s/Vendors/?components=402,304";
		url = url.formatted(user.getMembershipType(), user.getBungieMembershipID(), user.getCharacters().get(guardian.ordinal()).getCharacterID());

		JsonObject response	= DestinyAPI.getHttpUtils().urlRequestGETOauth(url).getAsJsonObject("Response");
		JsonObject sales_jo	= response.getAsJsonObject("sales").getAsJsonObject("data");
		JsonObject stats_jo	= response.getAsJsonObject("itemComponents");

		JsonObject result	= new JsonObject();
		result.add("character", createCharacterJO(user, guardian));
		result.add("sales", sales_jo);
		result.add("stats", stats_jo);

		return result;
	}

	public static JsonObject createCharacterJO(BungieUser user, Guardian guardian)
	{
		JsonObject result = new JsonObject();

		result.add("guardian", new JsonPrimitive(guardian.toString()));
		result.add("bungie_user", user.getJO());

		return result;
	}

	public static JsonObject getSales(JsonObject raw_vendor, String vendor_hash)
	{
		return raw_vendor.getAsJsonObject("sales").getAsJsonObject(vendor_hash).getAsJsonObject("saleItems");
	}

	public static JsonObject getStats(JsonObject raw_vendor, String vendor_hash, String sale_slot)
	{
		return raw_vendor.getAsJsonObject("stats").getAsJsonObject(vendor_hash).getAsJsonObject("stats").getAsJsonObject("data").getAsJsonObject(sale_slot).getAsJsonObject("stats");
	}

	public static String getItemType(String item_hash)
	{
		return D2VT.db.manifest.getDefinitionLibrary(ManifestEntityTypes.INVENTORYITEM).getAsJsonObject(item_hash).get("itemTypeDisplayName").getAsString();
	}

	public static String getItemTier(String item_hash)
	{
		return D2VT.db.manifest.getDefinitionLibrary(ManifestEntityTypes.INVENTORYITEM).getAsJsonObject(item_hash).getAsJsonObject("inventory").get("tierTypeName").getAsString();
	}

	public static String getItemSeasonalWatermaerk(String item_hash)
	{
		JsonArray icons = D2VT.db.manifest.getDefinitionLibrary(ManifestEntityTypes.INVENTORYITEM).getAsJsonObject(item_hash).getAsJsonObject("quality").getAsJsonArray("displayVersionWatermarkIcons");
		return icons.get(icons.size() - 1).getAsString();
	}

	public static boolean[] isArmorAndExotic(String item_hash)
	{
		boolean	flag0 = Arrays.asList(DestinyItemArmor.getValiableArmorSlot()).contains(getItemType(item_hash));
		boolean	flag1 = false;
		if (flag0)
			flag1 = getItemTier(item_hash).contains("Exotic");

		return new boolean[] { flag0 ,flag1 };
	}

	public static String getItemHash(JsonObject item)
	{
		return String.valueOf(item.get("itemHash").getAsLong());
	}

	public static void parseArmorStatus(JsonObject raw_vendor, String vendor_hash, String sale_slot, DestinyItemArmor armor_piece)
	{
		Logger.blankLine();
		Logger.logFormatted("• Armor %s - %s (%s) // %s", sale_slot, armor_piece.getName(), armor_piece.getHashID(), armor_piece.getType().name());
		Logger.logItem("Armor Icon URL", armor_piece.getIcon());

		JsonObject stats_list = getStats(raw_vendor, vendor_hash, sale_slot);
		armor_piece.setStatus(stats_list);
		Logger.logArmorStatus(armor_piece);
	}

	public static void saveTimeElapsed(Instant start, Instant finish)
	{
		Logger.logFormatted("Everything done in %s minutes and %s seconds.", Duration.between(start, finish).toMinutesPart(), Duration.between(start, finish).toSecondsPart());

		// TODO implements a way to save this into file
		// Format: dd/mm/aaaa HHhMM - MMm SSs
	}

	/**
	 * ================================
	 * XÛR AREA
	 * ===============================
	 */

	public static JsonObject XUR_LOCATION;

	public static boolean isXurActive()
	{
		return getXurApiData() != null;
	}

	public static String getXurLocation(boolean isInitials)
	{
		if (isInitials)
			return XUR_LOCATION.get("location").getAsString();
		else
			return "%s, in %s".formatted(XUR_LOCATION.get("bubbleName").getAsString(), XUR_LOCATION.get("destinationName").getAsString());
	}

	public static JsonObject getXurApiData()
	{
		JsonObject result = null;

		try
		{
			URL				  url		 = new URL("https://paracausal.science/xur/current.json");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("accept", "application/json");

			InputStream	   is		= connection.getInputStream();
			BufferedReader rd		= new BufferedReader(new InputStreamReader(is));
			StringBuilder  response	= new StringBuilder();
			String		   line;

			while ((line = rd.readLine()) != null)
			{
				response.append(line);
				response.append('\r');
			}

			if (!response.toString().contains("null"))
				result = JsonParser.parseString(response.toString()).getAsJsonObject();
			XUR_LOCATION = result;

		} catch (IOException e)
		{
			Logger.logErr(e.getMessage());
		}

		return result;
	}
}
