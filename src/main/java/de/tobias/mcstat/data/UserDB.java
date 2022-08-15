package de.tobias.mcstat.data;

import com.google.common.base.Strings;
import de.tobias.mcstat.main;
import de.tobias.mcstat.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class UserDB {

    public static HashMap<UUID, UserDB> activeDBs = new HashMap<>();

    UUID uuid;
    File f;
    Connection connection;
    ArrayList<String> executeBatchCache = new ArrayList<>();

    public static UserDB getDB(UUID uuid) {
        if(activeDBs.containsKey(uuid)) {
            if(activeDBs.get(uuid).isConnected()) {
                return activeDBs.get(uuid);
            } else {
                activeDBs.remove(uuid);
            }
        }
        UserDB db = new UserDB(uuid);
        db.prepare();
        db.connect();
        return db;
    }

    public static void closeAll() {
        Logger.info("Closing all Databases...");
        for(UserDB db : activeDBs.values()) {
            db.close();
        }
        Logger.info("All Databases are now closed!");
    }

    public UserDB(UUID uuid) {
        this.uuid = uuid;
        this.f = new File(main.pl.getDataFolder() + "/db/" + uuid.toString() + ".db");
        activeDBs.put(uuid, this);
    }

    public void prepare() {
        if(!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
            Logger.debug("Created Parent Directories for new Database");
        }
        //if(f.exists()) f.delete();
    }

    public boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + f.getAbsolutePath());
            if(connection == null) throw new Error("Connection was null");
            if(!isConnected()) throw new Error("Connection already closed");
            this.prepareTables();
            Logger.debug("Opened Database for: " + uuid.toString());
        } catch (Exception ex) {
            Logger.warn("Failed to open Database:");
            Logger.warn("(DB: " + f.getAbsolutePath() + ")");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void prepareTables() {
        execute("CREATE TABLE IF NOT EXISTS `metadata` (`KEY` TEXT NOT NULL , `VALUE` TEXT NOT NULL , `LAST_CHANGED` INT)", false);
        execute("CREATE TABLE IF NOT EXISTS `travel` (`TIME` INT NOT NULL , `DISTANCE` INT NOT NULL , `TYPE` TEXT NOT NULL, `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `build` (`TIME` INT NOT NULL , `COUNT` INT NOT NULL , `BLOCKTYPE` TEXT NOT NULL , `TYPE` TEXT NOT NULL, `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `entityKills` (`TIME` INT NOT NULL , `COUNT` INT NOT NULL , `ENTITYTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `entityDamage` (`TIME` INT NOT NULL , `DAMAGE` INT NOT NULL , `ENTITYTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `entityDamageTaken` (`TIME` INT NOT NULL , `DAMAGE` INT NOT NULL , `ENTITYTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `foodLevel` (`TIME` INT NOT NULL, `VALUE` INT NOT NULL, `ADDED` INT NOT NULL, `REMOVED` INT NOT NULL, `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `foodEaten` (`TIME` INT NOT NULL , `COUNT` INT NOT NULL , `ITEMTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `playTime` (`TIME` INT NOT NULL , `VALUE` INT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `craftedItems` (`TIME` INT NOT NULL , `COUNT` INT NOT NULL , `ITEMTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `brokenItems` (`TIME` INT NOT NULL , `COUNT` INT NOT NULL , `ITEMTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `damagedItems` (`TIME` INT NOT NULL , `DAMAGE` INT NOT NULL , `ITEMTYPE` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
        execute("CREATE TABLE IF NOT EXISTS `advancementsDone` (`TIME` INT NOT NULL , `NAME` TEXT NOT NULL , `CRITERIA` TEXT NOT NULL , `WORLD` TEXT NOT NULL)", false);
    }

    public boolean isConnected() {
        try {
            return (connection != null && !connection.isClosed());
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean close() {
        if(isConnected()) {
            try {
                connection.close();
                connection = null;
                Logger.debug("Closed Database for: " + uuid.toString());
            } catch (Exception ex) {
                Logger.warn("Failed to close Database:");
                Logger.warn("(DB: " + f.getAbsolutePath() + ")");
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean execute(String command, Boolean isBatch) {
        if(isBatch) {
            executeBatchCache.add(command);
            return true;
        }

        try {
            Statement st = connection.createStatement();
            Logger.debug("Executing command: " + command);
            st.execute(command);
            st.close();
            return true;
        } catch (Exception ex) {
            logError("EXECUTE_FAIL", new String[]{ command }, ex);
            return false;
        }
    }

    public boolean executeBatch(String[] commands) {
        if(commands.length == 0) return true;
        try {
            Statement st = connection.createStatement();
            Logger.debug("Executing Batch:");
            for(String command : commands) {
                st.addBatch(command);
                Logger.debug("  " + command);
            }
            st.executeBatch();
            st.close();
            return true;
        } catch (Exception ex) {
            logError("EXECUTE_BATCH_FAIL", commands, ex);
            return false;
        }
    }

    public ResultSet query(String query) {
        try {
            Statement st = connection.createStatement();
            Logger.debug("Running Query: " + query);
            return st.executeQuery(query);
        } catch (Exception ex) {
            logError("QUERY_FAIL", new String[]{ query }, ex);
            return null;
        }
    }

    public Boolean flushExecuteBatches() {
        Boolean result = executeBatch(executeBatchCache.toArray(new String[0]));
        executeBatchCache.clear();
        return result;
    }

    public void logError(String type, String[] commands, Exception ex) {
        int randomNumber = new Random().nextInt(9000) + 1000;
        Logger.warn("----- [SQL ERROR #" + randomNumber + "] -----");
        Logger.warn("Type: " + type);
        Logger.warn("Database: " + f.getAbsolutePath());
        for(int i=0; i<commands.length; i++) {
            Logger.warn("SQL " + i + ": " + commands[i]);
        }

        Logger.warn("");
        Logger.warn("Stacktrace:");
        StringWriter errorWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(errorWriter));
        String[] lines = errorWriter.toString().split("\n");
        for(String line : lines) Logger.warn(line);

        Logger.warn("----- [SQL ERROR #" + randomNumber + "] -----");

        Player p = Bukkit.getPlayer(uuid);
        if(p != null && p.isOnline()) {
            p.sendMessage(Logger.prefix + "§cThere was an error writing your stats to the Database! (Error: §b#" + randomNumber + "§c, §a" + commands.length + " Items §cdiscarded)");
        }
    }
}
