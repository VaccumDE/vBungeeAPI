/*
 * Developed by Paul Artjomow
 * Last update 11.06.20, 10:34
 * Copyright (c) 2020 Vaccum.de | Paul Artjomow
 */

package bungee.vaccum.api;

import bungee.vaccum.api.cloud.CloudHandler;
import bungee.vaccum.api.cloud.ReportManager;
import bungee.vaccum.api.cloud.TeamChatManager;
import bungee.vaccum.api.rest.RestAPI;
import me.gong.mcleaks.MCLeaksAPI;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class vBungeeAPI extends Plugin {

    public static vBungeeAPI instance;
    private RestAPI restAPI;
    private CloudHandler cloudHandler;
    private ReportManager reportManager;
    private TeamChatManager teamChatManager;
    private Configuration configuration;

    private MCLeaksAPI mcLeaksAPI = MCLeaksAPI.builder()
            .threadCount(2)
            .expireAfter(10, TimeUnit.MINUTES).build();

    public String noperms = "§cDazu hast du keine Berechtigung!";

    @Override
    public void onEnable() {
        instance = this;
        restAPI = new RestAPI();
        cloudHandler = new CloudHandler();
        reportManager = new ReportManager();
        teamChatManager = new TeamChatManager();

        try {
            if(!getDataFolder().exists())
                getDataFolder().mkdir();
            File file = new File(getDataFolder().getPath(), "config.yml");
            if(!file.exists()) {
                file.createNewFile();
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                configuration.set("api-key", "key");
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        getProxy().getConsole().sendMessage("[" + getDescription().getName() + "] Started successfully with build " + getDescription().getVersion());
        getProxy().getConsole().sendMessage("[" + getDescription().getName() + "] Loading modules...");
    }

    public static vBungeeAPI getInstance() {
        return instance;
    }

    public RestAPI getRestAPI() {
        return restAPI;
    }

    public CloudHandler getCloudHandler() { return cloudHandler; }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public TeamChatManager getTeamChatManager() { return teamChatManager; }

    public MCLeaksAPI getMcLeaksAPI() { return mcLeaksAPI; }

    public Configuration getConfiguration() {
        return configuration;
    }

}
