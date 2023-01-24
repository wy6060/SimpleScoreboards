package me.petterim1.scoreboards;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;

import de.theamychan.scoreboard.network.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends PluginBase implements Listener {

    private static final int currentConfig = 3;

    static PlaceholderAPI placeholderApi;

    static boolean incompatibleJava;

    public static String scoreboardTitle;
    public static final List<String> scoreboardText = new ArrayList<>();
    public static final List<String> noScoreboardWorlds = new ArrayList<>();

    static final Map<Player, Scoreboard> scoreboards = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        if (!APIDownloader.checkAndRun(this)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        placeholderApi = PlaceholderAPI.getInstance();

        saveDefaultConfig();
        Config config = getConfig();

        if (config.getInt("版本") < currentConfig) {
            getLogger().warning("SimpleScoreboards插件的配置文件config.yml已经过期，请删除旧的配置文件config.yml。");
        }

        scoreboardTitle = config.getString("标题");
        scoreboardText.addAll(config.getStringList("文本"));
        noScoreboardWorlds.addAll(config.getStringList("noScoreboardWorlds"));

        try {
            if (Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) > 11) {
                getLogger().warning("该插件不能保证在这个Java版本上运行，为了获得最佳的兼容性和性能，请使用Java 8或11");
                incompatibleJava = true;
            }
        } catch (Exception e) {
            getLogger().warning("检查Java版本失败，如果你的Java版本在Java 8以下，请使用Java 8或11以获取更好的兼容性和性能。");
            if (Nukkit.DEBUG > 1) {
                e.printStackTrace();
            }
        }

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        if (config.getInt("update") > 0) {
            getServer().getScheduler().scheduleDelayedRepeatingTask(this, new ScoreboardUpdater(this), config.getInt("update"), config.getInt("update"), config.getBoolean("async", true));
        } else {
            getLogger().notice("未启用计分板更新 (更新 <= 0)");
        }
    }

    static String getScoreboardString(Player p, String text) {
        try {
            String t = placeholderApi.translateString(getKDRStatsReplaced(p, text), p);
            return placeholderApi.translateString(t, p);
        } catch (Exception e) {
            e.printStackTrace();
            return "PlaceholderAPI发生错误!";
        }
    }

    private static String getKDRStatsReplaced(Player p, String textToReplace) {
        try {
            Class.forName("kdr.Main");

            return textToReplace.replace("%kdr_kdr%", String.format("%.2f", kdr.Main.plugin.getKDR(p)))
                    .replace("%kdr_kills%", String.valueOf(kdr.Main.plugin.getKills(p)))
                    .replace("%kdr_deaths%", String.valueOf(kdr.Main.plugin.getDeaths(p)))
                    .replace("%kdr_topkdr%", String.format("%.2f", kdr.Main.plugin.getTopKDRScore()))
                    .replace("%kdr_topkdrplayer%", kdr.Main.plugin.getTopKDRPlayer())
                    .replace("%kdr_topkills%", String.valueOf(kdr.Main.plugin.getTopKills()))
                    .replace("%kdr_topdeaths%", String.valueOf(kdr.Main.plugin.getTopDeaths()))
                    .replace("%kdr_topkillsplayer%", kdr.Main.plugin.getTopKillsPlayer())
                    .replace("%kdr_topdeathsplayer%", kdr.Main.plugin.getTopDeathsPlayer());
        } catch (Exception e) {
            return textToReplace;
        }
    }
}
