/*
 * Developed by Paul Artjomow
 * Last update 10.06.20, 18:04
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api.cloud;

import bungee.vaccum.api.objects.Report;
import bungee.vaccum.api.rest.RestAPI;
import bungee.vaccum.api.utils.ReportReason;
import bungee.vaccum.api.vBungeeAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportManager {

    RestAPI restAPI = vBungeeAPI.getInstance().getRestAPI();


    public String createCase(UUID issuer, UUID suspect, ReportReason reportReason) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("issuer", issuer);
        jsonObject.put("suspect", suspect);
        jsonObject.put("reason", reportReason);

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/report/create", jsonObject.toString());
        return jsonResponse.getString("data");
    }

    public boolean deleteCase(String caseId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/report/delete", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public Report getReport(String caseId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/get/Case", jsonObject.toString());
        JSONObject jsonResultObject = jsonResult.getJSONArray("data").getJSONObject(0);

        return new Report(jsonResultObject.getString("caseId"), jsonResultObject.getString("suspected"), jsonResultObject.getString("issuer"), ReportReason.valueOf(jsonResultObject.getString("reason")), jsonResultObject.getString("editor"), Timestamp.valueOf(jsonResultObject.getString("creationTime")));
    }

    public List<Report> getOpenCases() throws IOException {
        List<Report> list = new ArrayList<>();
        /*getReportManager().getOpenCases().get(i).getReportReason());*/
        JSONObject jsonResult = restAPI.getObjectFromWebsite("https://api.vaccum.de/report/get/openCases?api-key=3ec3044c-6377-4b07-b0ed-70d5990e02dd");
        JSONArray jsonArray = jsonResult.getJSONArray("data");

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(new Report(jsonObject.getString("caseId"), jsonObject.getString("suspected"), jsonObject.getString("issuer"), ReportReason.valueOf(jsonObject.getString("reason")), jsonObject.getString("editor"), Timestamp.valueOf(jsonObject.getString("creationTime"))));
        }

        return list;
    }

    public boolean getReportStatus(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId().toString());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/ReportStatus", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public void setReportStatus(ProxiedPlayer proxiedPlayer, boolean status) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId().toString());
        jsonObject.put("status", status);

        restAPI.postObjectToWebsite("https://api.vaccum.de/user/set/ReportStatus", jsonObject.toString());
    }

    public boolean getReportAutoLoginStatus(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId().toString());

        JSONObject jsonResponse = restAPI.postObjectToWebsite("https://api.vaccum.de/user/get/ReportAutoLoginStatus", jsonObject.toString());
        return jsonResponse.getBoolean("data");
    }

    public void setReportAutoLoginStatus(ProxiedPlayer proxiedPlayer, boolean status) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uuid", proxiedPlayer.getUniqueId().toString());
        jsonObject.put("status", status);

        restAPI.postObjectToWebsite("https://api.vaccum.de/user/set/ReportAutoLogin", jsonObject.toString());
    }

    public ArrayList<UUID> getOnlineReportPlayers() throws IOException {
        ArrayList<UUID> list = new ArrayList<>();
        JSONObject jsonResult = restAPI.getObjectFromWebsite("https://api.vaccum.de/user/get/OnlineReportPlayers?api-key=" + vBungeeAPI.getInstance().getConfiguration().getString("api-key"));
        JSONArray jsonArray = jsonResult.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++)
            list.add(UUID.fromString(jsonArray.getString(i)));

        return list;
    }

    public boolean isCaseOpen(String caseId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/exists", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

    public boolean setEditor(String caseId, ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);
        jsonObject.put("editor", proxiedPlayer.getUniqueId());

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/set/Editor", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

    public boolean setEditorToNull(String caseId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);
        jsonObject.put("editor", "");

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/set/Editor", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

    public boolean isEditorAlreadyInCase(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("editor", proxiedPlayer.getUniqueId());

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/isEditorAlreadyInCase", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

    public Report getCaseOfEditor(ProxiedPlayer proxiedPlayer) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("editor", proxiedPlayer.getUniqueId());

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/get/CaseOfEditor", jsonObject.toString());
        JSONArray jsonArray = jsonResult.getJSONArray("data");
        JSONObject jsonReport = jsonArray.getJSONObject(0);

        return new Report(jsonReport.getString("caseId"), jsonReport.getString("suspected"), jsonReport.getString("issuer"), ReportReason.valueOf(jsonReport.getString("reason")), jsonReport.getString("editor"), Timestamp.valueOf(jsonReport.getString("creationTime")));
    }

    public boolean isPlayerAlreadyReported(UUID uuid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("suspect", uuid.toString());

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/isPlayerAlreadyReported", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

    public boolean closeReport(String caseId) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("caseId", caseId);

        JSONObject jsonResult = restAPI.postObjectToWebsite("https://api.vaccum.de/report/delete", jsonObject.toString());
        return jsonResult.getBoolean("data");
    }

}
