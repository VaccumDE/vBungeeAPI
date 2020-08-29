/*
 * Developed by Paul Artjomow
 * Last update 11.06.20, 10:35
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.cloud;

import bungee.vaccum.api.rest.RestAPI;
import bungee.vaccum.api.vBungeeAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class TeamChatManager {

    RestAPI restAPI = vBungeeAPI.getInstance().getRestAPI();

    public boolean isPlayerLoggedIntoTeamChat(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/TeamChat", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public void setTeamChatStatus(ProxiedPlayer proxiedPlayer, boolean status) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());
        jsonObject.put("status", status);
        restAPI.postObjectToWebsite("https://api.vaccum.de/user/set/TeamChat", jsonObject.toString());
    }

    public boolean hasPlayerTeamChatAutoLogin(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/TeamChatAutoLogin", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public void setPlayerTeamChatAutoLogin(ProxiedPlayer proxiedPlayer, boolean status) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());
        jsonObject.put("status", status);

        restAPI.postObjectToWebsite("https://api.vaccum.de/user/set/TeamChatAutoLogin", jsonObject.toString());
    }

    public ArrayList<UUID> getOnlineTeamChatPlayers() throws IOException {
        ArrayList<UUID> list = new ArrayList<>();
        JSONObject jsonResult = restAPI.getObjectFromWebsite("https://api.vaccum.de/user/get/OnlineTeamChatPlayers?api-key=" + vBungeeAPI.getInstance().getConfiguration().getString("api-key"));
        JSONArray jsonArray = jsonResult.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++)
            list.add(UUID.fromString(jsonArray.getString(i)));

        return list;
    }


}
