package db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
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
			vendors.add(new DestinyVendorXur("Xûr", "2190858386").setColor("rgb(79, 87, 92, 0.40)"));
		vendors.add(new DestinyVendor("Zavala", "69482069").setLocation("Courtyard, in Tower").setColor("rgb(4, 29, 44, 0.59)"));
		vendors.add(new DestinyVendor("Lord Shaxx", "3603221665").setLocation("Courtyard, in Tower").setColor("rgba(217, 35, 41, 0.23)"));
		vendors.add(new DestinyVendor("Drifter", "248695599").setLocation("Bazaar, in Tower").setColor("rgb(36, 62, 58, 0.67)"));
		vendors.add(new DestinyVendor("Ada-1", "350061650").setLocation("Bazaar, in Tower").setColor("rgb(18, 14, 14, 0.54)"));
		vendors.add(new DestinyVendor("Devrim Kay", "396892126").setLocation("Trostland, in European Dead Zone").setColor("rgb(70, 58, 51, 0.46)"));
		vendors.add(new DestinyVendor("Failsafe", "1576276905").setLocation("Exodus Black, in Nessus").setColor("rgb(126, 62, 53, 0.43)"));

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

		// new FileWriter(file.getAbsolutePath())
		try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))
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
