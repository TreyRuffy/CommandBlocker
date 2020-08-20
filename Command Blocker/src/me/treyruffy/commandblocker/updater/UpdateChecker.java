package me.treyruffy.commandblocker.updater;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UpdateChecker {

	public static String request(String RESOURCE_ID, String PLUGIN_NAME) {
		
		String REQUEST_URL = "https://api.spiget.org/v2/resources/" + RESOURCE_ID + "/versions?size=" + Integer.MAX_VALUE + "&sort=-releaseDate";
		try {
			URL url = new URL(REQUEST_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", PLUGIN_NAME);
			
			InputStream inputStream = connection.getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			
			JsonElement element = new JsonParser().parse(reader);

			if(!element.isJsonArray()) {
				return "";
			}
			
			reader.close();
			
			JsonObject latestVersion = element.getAsJsonArray().get(0).getAsJsonObject();
			
			return latestVersion.get("name").getAsString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	
}
