package lib;

import material.DestinyAPI;

public class Tests
{
	public static void start(DestinyAPI api)
	{
		Logger.log("Starting Debugging Labs");
		api.enableDebugMode();
		api.disableDebugMode();
	}
/*
	private boolean needUpdate()
	{
		boolean flag = false;

		try
		{

			for (File file : files)
			{
				if (!file.exists())
					flag = true;

				try
				{
					JsonObject jo		 = new JsonParser().parse(new FileReader(file.getAbsolutePath())).getAsJsonObject();
					Timestamp  timestamp = new Timestamp(jo.get("next_update_timestamp").getAsLong());
					Timestamp  now		 = new Timestamp(System.currentTimeMillis());

					Logger.logItem("File timestamp", timestamp.toGMTString());
					Logger.logItem("Current time", now.toGMTString());
					Logger.log("Is NOW after file timestamp?" + now.after(timestamp));

					return now.after(timestamp);
				} catch (IllegalStateException e)
				{
					flag = true;
					Logger.log(e.getMessage());
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			flag = true;
		}
		return flag;
	}

	private JsonObject[] updateCachedFiles(BungieUser user)
	{
		JsonObject[] allClassesVendors = Utils.requestAllVendorsRaw(user);

		for (JsonObject specificClassVendors : allClassesVendors)
		{

		}
		vendors_jo.add("next_update_timestamp", new JsonPrimitive(1L));

		save();

		return allClassesVendors;
	}
*/
	public static void stop()
	{
	}
}
