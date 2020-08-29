/*
 * Developed by Paul Artjomow
 * Last update 11.06.20, 10:34
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.cloud;

import bungee.vaccum.api.rest.RestAPI;
import bungee.vaccum.api.vBungeeAPI;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeServiceProperty;
import de.dytanic.cloudnet.ext.bridge.player.CloudPlayer;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import me.gong.mcleaks.MCLeaksAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CloudHandler {

    RestAPI restAPI = vBungeeAPI.getInstance().getRestAPI();
    MCLeaksAPI mcLeaksAPI = vBungeeAPI.getInstance().getMcLeaksAPI();
    private final IPlayerManager playerManager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);

    public boolean kickCloudPlayer(CloudPlayer cloudPlayer, String reason) {
        if(cloudPlayer != null) {
            cloudPlayer.getPlayerExecutor().kick(reason);
            return true;
        } else {
            return false;
        }
    }

    public void sendCloudPlayerMessage(CloudPlayer cloudPlayer, String message) {
        cloudPlayer.getPlayerExecutor().sendChatMessage(message);
    }

    public void sendCloudPlayerToServer(CloudPlayer cloudPlayer, String server) {
        cloudPlayer.getPlayerExecutor().connect(server);
    }

    public int getTotalPlayerCount() {
        int counter = 0;
        for (ServiceInfoSnapshot serviceInfoSnapshot : CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices("Proxy")) {
            counter += serviceInfoSnapshot.getProperty(BridgeServiceProperty.ONLINE_COUNT).orElse(0);
        }
        return counter;
    }

    public boolean isMcLeaks(UUID uuid) {
        return mcLeaksAPI.checkAccount(uuid).isMCLeaks();
    }

    public boolean isMcLeaks(String username) {
        return mcLeaksAPI.checkAccount(username).isMCLeaks();
    }

    public boolean isMcLeaks(ProxiedPlayer proxiedPlayer) {
        return mcLeaksAPI.checkAccount(proxiedPlayer.getUniqueId()).isMCLeaks();
    }

    public boolean isProxiedIP(String ip) throws IOException {
        JSONObject jsonObject = restAPI.getObjectFromWebsite("https://proxycheck.io/v2/" + ip + "?key=XXXXX&risk=1&vpn=1");
        boolean isProxied;

        switch(jsonObject.getJSONObject(ip).getString("proxy")) {
            case "yes":
                isProxied = true;
                break;
            case "no":
                isProxied = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + jsonObject.getJSONObject(ip).getString("proxy"));
        }

        return isProxied;
    }

    public boolean doesPlayerExists(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/exists", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public String registerPlayer(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());
        jsonObject.put("username", proxiedPlayer.getName());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/register", jsonObject.toString());
        return jsonResponse.getString("data");
    }

    public boolean hasPlayerAcceptedTerms(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/TermsStatus", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public String acceptPlayerTerms(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/set/AcceptTermsStatus", jsonObject.toString());
        return jsonResponse.getString("data");
    }

    public String getDisplayName(ProxiedPlayer proxiedPlayer) {
        IPermissionUser permissionUser = CloudNetDriver.getInstance().getPermissionManagement().getUser(proxiedPlayer.getUniqueId());

        if(permissionUser != null) {
            return CloudNetDriver.getInstance().getPermissionManagement().getHighestPermissionGroup(permissionUser).getPrefix().replace("&", "§") + proxiedPlayer.getName();
        } else
            return "null";
    }

    public String getUsername(String uuid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", uuid);

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/Username", jsonObject.toString());
        return jsonResponse.getString("data");
    }
}
