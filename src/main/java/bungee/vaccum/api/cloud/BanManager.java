/*
 * Developed by Paul Artjomow
 * Last update 07.06.20, 17:12
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.cloud;

import bungee.vaccum.api.rest.RestAPI;
import bungee.vaccum.api.vBungeeAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONObject;

public class BanManager {

    RestAPI restAPI = vBungeeAPI.getInstance().getRestAPI();

    public boolean doesPlayerExists(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/exists", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

}
