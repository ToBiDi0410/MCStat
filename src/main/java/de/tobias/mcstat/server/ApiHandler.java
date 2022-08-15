package de.tobias.mcstat.server;

import com.google.gson.GsonBuilder;
import de.tobias.mcstat.data.ScoreboardCollector;
import de.tobias.mcstat.data.TravelCollector;
import de.tobias.mcstat.util.Logger;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class ApiHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Logger.debug("API Request: " + request.getPathInfo());
        String path = request.getPathInfo().toLowerCase();


        Object returnVal;
        if(path.startsWith("/player/")) {
            String playerUUID = path.split("/")[2];
            path = path.replace("/player/" + playerUUID, "");

            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
            if(op == null) {
                returnVal = "PLAYER_NOT_FOUND";
            } else if(!op.hasPlayedBefore()) {
                returnVal = "PLAYER_NEVER_PLAYED_BEFORE";
            } else {
                returnVal = getRouteValue(path, op, request.getParameterMap());
            }
        } else {
            returnVal = getRouteValue(path, null, request.getParameterMap());
        }

        if(returnVal == null) returnVal = "NO_VALUE";

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(returnVal));
    }

    public static Object getRouteValue(String path, OfflinePlayer op, Map<String, String[]> parameters) {
        if(path.startsWith("/global/teams")) return ScoreboardCollector.getTeams();
        if(path.startsWith("/global/objectives")) return ScoreboardCollector.getObjectives();

        if(path.startsWith("/travel")) {
            if(parameters.containsKey("maxage")) {
                return TravelCollector.getMovementsForAllWorlds(UUID.fromString(op.getUniqueId().toString()), Long.valueOf(parameters.get("maxage")[0]));
            } else {
                return TravelCollector.getMovementsForAllWorlds(UUID.fromString(op.getUniqueId().toString()), 1000L * 60L * 5L);
            }
        }
        return null;
    }

}
