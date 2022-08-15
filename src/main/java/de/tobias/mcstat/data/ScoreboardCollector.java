package de.tobias.mcstat.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class ScoreboardCollector {

    public static HashMap<String, Object> getTeams() {
        HashMap<String, Object> values = new HashMap<>();
        for(Team t : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            values.put(t.getName(), getTeamValues(t));
        }
        return values;
    }

    public static HashMap<String, Object> getTeamValues(Team t) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", t.getName());
        values.put("displayName", t.getDisplayName());
        values.put("prefix", t.getPrefix());
        values.put("suffix", t.getSuffix());
        values.put("color", t.getColor().toString());
        values.put("size", t.getSize());
        values.put("allowFriendlyFire", t.allowFriendlyFire());
        values.put("canSeeFriendlyInvisibles", t.canSeeFriendlyInvisibles());
        return values;
    }

    public static HashMap<String, Object> getObjectives() {
        HashMap<String, Object> values = new HashMap<>();
        for(Objective o : Bukkit.getScoreboardManager().getMainScoreboard().getObjectives()) {
            values.put(o.getName(), getObjectiveValues(o));
        }
        return values;
    }

    public static HashMap<String, Object> getObjectiveValues(Objective o) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", o.getName());
        values.put("displayName", o.getDisplayName());
        values.put("criteria", o.getCriteria());
        values.put("displaySlot", o.getDisplaySlot());
        values.put("renderType", o.getRenderType());

        /*HashMap<String, Object> scores = new HashMap<>();
        for(OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            scores.put(op.getName(), o.getScore(op.getName()));
        }
        values.put("scores", scores);*/

        return values;
    }
}
