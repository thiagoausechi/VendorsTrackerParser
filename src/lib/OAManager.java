package lib;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import material.DestinyAPI;
import utils.framework.OAuthManager;

public class OAManager extends OAuthManager
{
	private File	   file	= new File(Paths.get("").toAbsolutePath() + "\\database\\cached\\oauth.json");
	private JsonObject jsonObject;

	public OAManager()
	{
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			try
			{
				jsonObject = new JsonParser().parse(new FileReader(file.getAbsolutePath())).getAsJsonObject();
			} catch (IllegalStateException exception)
			{ // If the file is empty or corrupted
				jsonObject = new JsonObject();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		Logger.logItem("Cached OAuth Code is at", file.getAbsolutePath());
	}

	@Override
	public String getAccessToken()
	{
		if (jsonObject.has("access-token"))
		{
			return jsonObject.get("access-token").getAsString();
		}
		return null;
	}

	@Override
	public String getRefreshToken()
	{
		if (jsonObject.has("refresh-token"))
		{
			return jsonObject.get("refresh-token").getAsString();
		}
		return null;
	}

	@Override
	public String getAPIToken()
	{
		return DestinyAPI.getApiKey();
	}

	@Override
	public void setAccessToken(String accessToken)
	{
		jsonObject.addProperty("access-token", accessToken);
		save();
	}

	@Override
	public void setRefreshToken(String refreshToken)
	{
		jsonObject.addProperty("refresh-token", refreshToken);
		save();
	}

	@Override
	public void setAPIToken(String apiToken)
	{
	}

	public void save()
	{

		try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath()))
		{
			fileWriter.write(jsonObject.toString());
			fileWriter.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
