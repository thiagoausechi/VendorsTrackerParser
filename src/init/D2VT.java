package init;

import static lib.References.API_KEY;
import static lib.References.OAUTH_CLIENT_ID;
import static lib.References.OAUTH_CLIENT_SECRET;

import java.io.File;
import java.nio.file.Paths;
import java.time.Instant;

import db.Database;
import lib.Logger;
import lib.OAManager;
import lib.Utils;
import material.DestinyAPI;
import material.user.BungieUser;

public class D2VT
{
	public static DestinyAPI api = new DestinyAPI();
	public static Database	 db	 = new Database();
	public static OAManager	 oam = new OAManager();

	public static void main(String[] args)
	{
		start();
		// Tests.start(api);
		// Tests.stop();
	}

	private static void start()
	{
		Instant start = Instant.now();
		
		Logger.logStart("Software");
		api.setOauthManager(oam);
		api.setApiKey(API_KEY).setClientID(OAUTH_CLIENT_ID);
		api.setClientSecret(OAUTH_CLIENT_SECRET);

		Logger.logTittle("Getting User");
		BungieUser user = DestinyAPI.getValidUsers("ausechi#2352").get(0); // TODO Make user name a env variable
		Logger.logUserInfo(user);
		
		// DestinyAPI.getHttpUtils().setTokenViaAuth("17a4206454af6aa20c26b53a043de01a");
		// TODO Make OAuth Code a env variable
		
		db.start(user);
		db.exportToFile(new File(Paths.get("").toAbsolutePath() + "\\database\\vendors.json"));
		
		Instant finish = Instant.now();
		Utils.saveTimeElapsed(start, finish);
	}
}
/**
 * TODO HELPING LINKS
 * 
 * https://github.com/dec4234/JavaDestinyAPI https://bungie-net.github.io/
 * https://www.bungie.net/platform/destiny2/help/
 * 
 * Shaxx: https://i.ibb.co/V21mQrr/3603221665.png
 * Zavala: https://i.ibb.co/SxTkLFc/69482069.png
 * 
 */
