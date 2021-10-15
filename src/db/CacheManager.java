package db;

import com.google.gson.JsonObject;

import lib.Logger;
import lib.References;
import lib.References.Guardian;
import lib.Utils;
import lib.base.DestinyItemArmor;
import lib.base.DestinyVendor;
import lib.base.DestinyVendorXur;
import lib.base.DestinyItemArmor.ArmorType;
import material.user.BungieUser;

public class CacheManager
{
	private Database db;

	public CacheManager(Database database)
	{
		this.db = database;
	}

	public void start(BungieUser user)
	{
		Logger.logStart("CacheManager");
		JsonObject[] all_vendors_raw = Utils.requestAllVendorsRaw(user);

		for (int i = 0; i < all_vendors_raw.length; i++)
		{ // Class loop
			JsonObject class_vendors_raw = all_vendors_raw[i];
			Guardian   guardian			 = Guardian.values()[i];

			Logger.logTittle("Getting vendors for character: " + guardian, false, false);

			for (String vendor_hash : class_vendors_raw.getAsJsonObject("sales").keySet())
			{ // Vendors loop if

				if (db.isValidVendor(vendor_hash))
				{
					Logger.blankLine();
					Logger.logVendorsDetails(vendor_hash);
					DestinyVendor	 vendor	= db.getVendor(vendor_hash);
					DestinyVendorXur xur	= null;
					JsonObject		 sales	= Utils.getSales(class_vendors_raw, vendor_hash);

					if (vendor_hash.contains(References.VENDOR_XUR))
						xur = (DestinyVendorXur) vendor;

					for (String sale_slot : sales.keySet())
					{ // Sales loop
						String	  item_hash	= Utils.getItemHash(sales.getAsJsonObject(sale_slot));

						boolean[] flags		= Utils.isArmorAndExotic(item_hash);

						if (flags[0])
						{
							ArmorType		 armor_type	 = DestinyItemArmor.getArmorType(Utils.getItemType(item_hash));
							DestinyItemArmor armor_piece = new DestinyItemArmor(item_hash, armor_type);

							Logger.logFormatted("%s (%s)", armor_piece.getName(), item_hash);
							armor_piece.setSeasonWatermark(Utils.getItemSeasonalWatermaerk(item_hash));

							Utils.parseArmorStatus(class_vendors_raw, vendor_hash, sale_slot, armor_piece);

							if (xur != null && flags[1]
								&& (Utils.getArmorClassType(item_hash) == guardian.getClassType()))
								xur.setExoticSale(guardian, armor_piece);
							else if (!flags[1])
								vendor.addSale(guardian, armor_piece);
						}
					}
				}
			}
		}

		all_vendors_raw = null; // Clear RAM ?
	}
}
