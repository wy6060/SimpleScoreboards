package me.petterim1.scoreboards;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;

public class APIDownloader {

    static boolean checkAndRun(Plugin plugin) {
        Server server = plugin.getServer();

        if (server.getPluginManager().getPlugin("ScoreboardPlugin") == null) {
            plugin.getLogger().info("下载ScoreboardAPI中...");

            String scoreboardApi = server.getFilePath() + "/plugins/ScoreboardAPI.jar";

            try {
                FileOutputStream fos = new FileOutputStream(scoreboardApi);
                fos.getChannel().transferFrom(Channels.newChannel(new URL("https://dl.dropboxusercontent.com/s/hsrflmdxqqrvc0v/ScoreboardAPI.jar").openStream()), 0, Long.MAX_VALUE);
                fos.close();
            } catch (Exception e) {
                plugin.getLogger().error("无法下载ScoreboardAPI!", e);
                return false;
            }

            plugin.getLogger().info("ScoreboardAPI下载完成！");
            server.getPluginManager().enablePlugin(server.getPluginManager().loadPlugin(scoreboardApi));
        }

        if (server.getPluginManager().getPlugin("KotlinLib") == null) {
            plugin.getLogger().info("下载KotlinLib...中");

            String placeholderApi = server.getFilePath() + "/plugins/KotlinLib.jar";

            try {
                FileOutputStream fos = new FileOutputStream(placeholderApi);
                fos.getChannel().transferFrom(Channels.newChannel(new URL("https://dl.dropboxusercontent.com/s/6rmogms1458p369/KotlinLib.jar").openStream()), 0, Long.MAX_VALUE);
                fos.close();
            } catch (Exception e) {
                plugin.getLogger().error("无法下载KotlinLib!", e);
                return false;
            }

            plugin.getLogger().info("PlaceholderAPI下载完成 !");
            server.getPluginManager().enablePlugin(server.getPluginManager().loadPlugin(placeholderApi));
        }

        if (server.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            plugin.getLogger().info("下载PlaceholderAPI中...");

            String placeholderApi = server.getFilePath() + "/plugins/PlaceholderAPI.jar";

            try {
                FileOutputStream fos = new FileOutputStream(placeholderApi);
                fos.getChannel().transferFrom(Channels.newChannel(new URL("https://dl.dropboxusercontent.com/s/b5qvtaugosf54am/PlaceholderAPI.jar").openStream()), 0, Long.MAX_VALUE);
                fos.close();
            } catch (Exception e) {
                plugin.getLogger().error("无法下载PlaceholderAPI!", e);
                return false;
            }

            plugin.getLogger().info("PlaceholderAPI下载完成！");
            server.getPluginManager().enablePlugin(server.getPluginManager().loadPlugin(placeholderApi));
        }

        return true;
    }
}
