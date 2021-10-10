package lib;

import init.D2VT;
import lib.base.DestinyItemArmor;
import lib.base.DestinyVendor;
import material.user.BungieUser;

public class Logger
{
	public static final Logger log = new Logger();

	public static Logger log(String var0)
	{
		System.out.println(var0);
		return log;
	}

	public static Logger logFormatted(String var0, Object... var1)
	{
		System.out.println(var0.formatted(var1));
		return log;
	}

	public static Logger logErr(String var0)
	{
		System.err.println(var0);
		return log;
	}

	public static Logger logTittle(String var0)
	{
		logTittle(var0, false, true);
		return log;
	}

	public static Logger logTittle(String var0, boolean... flag)
	{
		blankLine();
		logErr(var0);
		logBreak(flag[0], flag[1]);
		return log;
	}

	public static Logger logStart(String var0)
	{
		blankLine();
		System.out.println(">> Starting [%s] ...".formatted(var0));
		return log;
	}

	public static Logger logForNull(String var0, Object var2)
	{
		logTestForNull(var0, false, var2);
		return log;
	}

	public static Logger logForNullHiglight(String var0, Object var2)
	{
		logTestForNull(var0, true, var2);
		return log;
	}

	public static Logger logTestForNull(String var0, boolean var1, Object var2)
	{
		String object_name = var0.toUpperCase();
		if (var1)
			Logger.logErr(var2 == null ? object_name + " IS null" : object_name + " is NOT null");
		else
			Logger.log(var2 == null ? object_name + " IS null" : object_name + " is NOT null");
		return log;
	}

	public static Logger logItem(String var0, Object... var1)
	{
		var0 = var0 + ": ";

		for (Object obj : var1)
		{
			var0 += obj.toString();
		}
		System.out.println(var0);
		return log;
	}

	public static Logger logBreak()
	{
		logBreak(true, false);
		return log;
	}

	public static Logger logBreak(boolean blankBefore, boolean blankAfter)
	{
		if (blankBefore)
			blankLine();
		System.out.println("==================================================================");
		if (blankAfter)
			blankLine();
		return log;
	}

	public static Logger blankLine()
	{
		System.out.println(" ");
		return log;
	}

	/*
	 * =========================================================================
	 * SPECIFIC LOGGERS
	 * =========================================================================
	 */
	public static Logger logUserInfo(BungieUser user)
	{
		logItem("Usarname", user.getSupplementalDisplayName());
		logItem("Membership ID", user.getBungieMembershipID());
		return log;
	}

	public static Logger logVendorsDetails(String hash)
	{
		DestinyVendor vendor = D2VT.db.getVendor(hash);
		logFormatted(">> Vendor %s (%s)", vendor.getName(), vendor.getHash());
		return log;
	}

	public static Logger logArmorStatus(DestinyItemArmor armor_piece)
	{
		Logger.logItem("Mobility", armor_piece.getMobility());
		Logger.logItem("Resilience", armor_piece.getResilience());
		Logger.logItem("Recovery", armor_piece.getRecovery());
		Logger.logItem("Discipline", armor_piece.getDiscipline());
		Logger.logItem("Intellect", armor_piece.getIntellect());
		Logger.logItem("Strenght", armor_piece.getStrenght());
		Logger.logItem("Total", armor_piece.getTotalStatus());
		return log;
	}
}
