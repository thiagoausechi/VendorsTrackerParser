package db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lib.Logger;
import lib.Utils;
import lib.base.DestinyVendor;
import lib.base.DestinyVendorXur;
import material.manifest.DestinyManifest;
import material.user.BungieUser;

public class Database
{
	private ArrayList<DestinyVendor>	   vendors;
	private HashMap<String, DestinyVendor> vendors_map;
	private CacheManager				   cache;
	public DestinyManifest				   manifest;

	public Database()
	{
		vendors = new ArrayList<DestinyVendor>();
		vendors_map = new HashMap<String, DestinyVendor>();
		cache = new CacheManager(this);
		manifest = new DestinyManifest();
	}

	public void start(BungieUser user)
	{
		Logger.logStart("Database");

		if (Utils.isXurActive())
			vendors.add(new DestinyVendorXur("Xûr", "2190858386").setColor("rgba(100, 107, 112, 0.23)"));
		vendors.add(new DestinyVendor("Zavala", "69482069").setLocation("Courtyard, in Tower").setColor("rgba(33, 115, 168, 0.23)"));
		vendors.add(new DestinyVendor("Lord Shaxx", "3603221665").setLocation("Courtyard, in Tower").setColor("rgba(217, 35, 41, 0.23)"));
		vendors.add(new DestinyVendor("Drifter", "248695599").setLocation("Bazaar, in Tower").setColor("rgba(58, 157, 143, 0.23)"));
		vendors.add(new DestinyVendor("Ada-1", "350061650").setLocation("Bazaar, in Tower").setColor("rgba(0, 0, 0, 0.4)"));
		vendors.add(new DestinyVendor("Devrim", "396892126").setLocation("Trostland, in European Dead Zone").setColor("rgba(64, 59, 56, 0.23)"));
		vendors.add(new DestinyVendor("Failsafe", "1576276905").setLocation("Exodus Black, in Nessus").setColor("rgba(136, 67, 57, 0.23)"));

		for (DestinyVendor vendor : vendors)
			vendors_map.put(vendor.getHash(), vendor);

		cache.start(user);

		for (DestinyVendor vendor : vendors)
			vendor.sort();
	}

	public DestinyVendor getVendor(String hash)
	{
		return vendors_map.get(hash);
	}

	public boolean isValidVendor(String vendor_hash)
	{
		return vendors_map.containsKey(vendor_hash);
	}

	public JsonObject exportToFile(File file)
	{
		JsonObject result		 = new JsonObject();
		JsonArray  vendors_array = new JsonArray();

		try
		{

			if (!file.exists())
			{
				file.createNewFile();
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		for (DestinyVendor vendor : vendors)
			vendors_array.add(vendor.export());

		result.add("vendors", vendors_array);

		try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath()))
		{
			fileWriter.write(vendors_array.toString());
			fileWriter.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		Logger.blankLine();
		Logger.log("Exported file.");

		return result;
	}
}
