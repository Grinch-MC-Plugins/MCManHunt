package io.github.thesummergrinch.mcmanhunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ManHuntUtilities {


    private static final AtomicBoolean IS_FIRST_RUN = new AtomicBoolean(false);
    private static final Server SERVER = Bukkit.getServer();
    private static final Plugin MANHUNT_PLUGIN = SERVER.getPluginManager().getPlugin("MCManHunt");
    private static final HashMap<UUID, Player> HUNTER_MAP;
    private static final HashMap<UUID, Player> RUNNER_MAP;
    private static final HashSet<Player> RANDOM_TEAM_QUEUE;

    private static final HashSet<UUID> SAVED_GAME_HUNTERS;
    private static final HashSet<UUID> SAVED_GAME_RUNNERS;

    private static int maxRunners;
    private static int maxHunters;

    static {
        HUNTER_MAP = new HashMap<>();
        RUNNER_MAP = new HashMap<>();
        RANDOM_TEAM_QUEUE = new HashSet<>();
        SAVED_GAME_HUNTERS = new HashSet<>();
        SAVED_GAME_RUNNERS = new HashSet<>();
    }

    public static OfflinePlayer getPlayer(final UUID playerUUID) {
        return SERVER.getOfflinePlayer(playerUUID);
    }

    static int getMaxHunters() {
        return maxHunters;
    }

    /**
     * Sets the maximum number of players allowed in the Hunter-team. Only effective when set before the start of the
     * game.
     *
     * @param maxHunters - The maximum value.
     */
    public static synchronized void setMaxHunters(final int maxHunters) {
        ManHuntUtilities.maxHunters = maxHunters;
    }

    static int getMaxRunners() {
        return maxRunners;
    }

    /**
     * Sets the maximum number of players allowed in the Runner-team. Only effective when set before the start of the
     * game.
     *
     * @param maxRunners - The maximum value.
     */
    public static synchronized void setMaxRunners(final int maxRunners) {
        ManHuntUtilities.maxRunners = maxRunners;
    }

    static synchronized void addSavedGameHunter(final UUID playerUUID) {
        SAVED_GAME_HUNTERS.add(playerUUID);
    }

    static synchronized void addSavedGameRunner(final UUID playerUUID) {
        SAVED_GAME_RUNNERS.add(playerUUID);
    }

    public static synchronized boolean isPlayerInSavedGame(final UUID playerUUID) {
        return SAVED_GAME_HUNTERS.contains(playerUUID) || SAVED_GAME_RUNNERS.contains(playerUUID);
    }

    public static synchronized boolean isPlayerSavedHunter(final UUID playerUUID) {
        return SAVED_GAME_HUNTERS.contains(playerUUID);
    }

    public static synchronized boolean isPlayerSavedRunner(final UUID playerUUID) {
        return SAVED_GAME_RUNNERS.contains(playerUUID);
    }

    public static void removePlayerFromSavedGameData(final UUID playerUUID) {
        if (isPlayerSavedHunter(playerUUID)) {
            SAVED_GAME_HUNTERS.remove(playerUUID);
        } else if (isPlayerSavedRunner(playerUUID)) {
            SAVED_GAME_RUNNERS.remove(playerUUID);
        }
    }

    /**
     * Adds a player to the Random Queue. This allows players to be randomly assigned to a team when the game starts.
     *
     * @param player - The player that is to be placed into the Random Queue.
     * @return boolean - True if the player was successfully added to the Random Queue. False otherwise.
     */
    public static boolean addPlayerToRandomQueue(final Player player) {
        return RANDOM_TEAM_QUEUE.add(player);
    }

    /**
     * Returns a Set containing all Player-objects in the Random Queue.
     *
     * @return Set of Player-objects in the Random Queue.
     */
    static Set<Player> getPlayersInRandomQueue() {
        return RANDOM_TEAM_QUEUE;
    }

    /**
     * Clears the Random Queue.
     */
    static void clearRandomTeamQueue() {
        RANDOM_TEAM_QUEUE.clear();
    }

    /**
     * Returns a cached Plugin-object for this plugin.
     *
     * @return Plugin-object.
     */
    static Plugin getManHuntPlugin() {
        return ManHuntUtilities.MANHUNT_PLUGIN;
    }

    /**
     * Returns the value of the IS_FIRST_RUN AtomicBoolean.
     *
     * @return boolean - IS_FIRST_RUN.get()
     */
    public static boolean isFirstRun() {
        return ManHuntUtilities.IS_FIRST_RUN.get();
    }

    /**
     * Sets the value of the IS_FIRST_RUN AtomicBoolean to the true.
     */
    static void setFirstRun() {
        ManHuntUtilities.IS_FIRST_RUN.set(true);
    }

    /**
     * Gets the Player-object of the specified player.
     *
     * @param playerName - The username of the player associated with the required Player-object.
     * @return player - The Player-object associated with the given username.
     */
    public static synchronized Player getPlayer(String playerName) {
        return SERVER.getPlayer(playerName);
    }

    /**
     * Checks whether or not the number of players in the Hunter-team is equal to, or exceeds, the maximum value set by
     * the maxHunters-field.
     *
     * @return boolean - True if full, false if not.
     */
    public static synchronized boolean isHunterTeamFull() {
        return maxHunters <= ManHuntUtilities.HUNTER_MAP.size();
    }

    /**
     * Checks whether or not the number of players in the Runner-team is equal to, or exceeds, the maximum value set by
     * the maxRunners-field.
     *
     * @return boolean - True if full, false if not.
     */
    public static synchronized boolean isRunnerTeamFull() {
        return maxRunners <= ManHuntUtilities.RUNNER_MAP.size();
    }

    /**
     * Checks whether or not the number of players in the Runner-team exceeds the maximum value set by the maxRunners-
     * field.
     *
     * @return boolean - True if the value exceeds the maximum, false if not.
     */
    public static synchronized boolean isRunnerTeamOverCapacity() {
        return maxRunners < ManHuntUtilities.RUNNER_MAP.size();
    }

    /**
     * Check whether or not the number of players in the Hunter-team exceeds the maximum value set by the maxHunters-
     * field.
     *
     * @return boolean - True if the value exceeds the maximum, false if not.
     */
    public static synchronized boolean isHunterTeamOverCapacity() {
        return maxHunters < ManHuntUtilities.HUNTER_MAP.size();
    }

    /**
     * Wrapper used to broadcast a message to the Server, without exposing the Server-object to the rest of the project.
     *
     * @param message - The message to be broadcast to the Server.
     */
    public static synchronized void broadcastMessage(final String message) {
        ManHuntUtilities.SERVER.broadcastMessage(message);
    }

    /**
     * Updates the configurable settings read from the config.yml.
     */
    public static void updateFromConfig() {
        maxHunters = ConfigurationUtilities.getInt(SERVER.getPluginManager().getPlugin("MCManHunt"), "max-hunters");
        maxRunners = ConfigurationUtilities.getInt(SERVER.getPluginManager().getPlugin("MCManHunt"), "max-runners");
    }

    /**
     * Adds the specified player to the Hunter-team.
     *
     * @param player - Player-object of the player that needs to be added to the Hunter-team.
     * @return boolean - True if successfully added to the Hunter-team, false otherwise.
     */
    public static synchronized boolean addHunter(final Player player) {
        final UUID playerUUID = player.getUniqueId();
        if (player.isOnline() && !ManHuntUtilities.isHunter(playerUUID)
                && !ManHuntUtilities.isRunner(playerUUID) && !ManHuntUtilities.isHunterTeamOverCapacity()) {
            ManHuntUtilities.HUNTER_MAP.put(playerUUID, player);
            if (GameFlowUtilities.isGameInProgress()) PlayerInventoryUtilities.givePlayerHunterCompasses(player);
            return true;
        }
        return false;
    }

    /**
     * Adds the specified player to the Runner-team.
     *
     * @param player - Player-object of the player that needs to be added to the Runner-team.
     * @return boolean - True if successfully added to the Runner-team.
     */
    public static synchronized boolean addRunner(final Player player) {
        final UUID playerUUID = player.getUniqueId();
        if (player.isOnline() && !ManHuntUtilities.isHunter(playerUUID)
                && !ManHuntUtilities.isRunner(playerUUID) && !ManHuntUtilities.isRunnerTeamFull()) {
            ManHuntUtilities.RUNNER_MAP.put(playerUUID, player);
            return true;
        }
        return false;
    }

    /**
     * Clears the HashMaps containing the Hunter-team and Runner-team data. Effectively clears the teams.
     */
    public static synchronized void resetplayerroles() {
        ManHuntUtilities.HUNTER_MAP.clear();
        ManHuntUtilities.RUNNER_MAP.clear();
    }

    /**
     * Checks if the specified player is in the Runner-team.
     *
     * @param playerUUID - The username of the player that is being checked.
     * @return boolean - True if the player is in the Hunter-team, false otherwise.
     */
    public static synchronized boolean isHunter(final UUID playerUUID) {
        return ManHuntUtilities.HUNTER_MAP.containsKey(playerUUID);
    }

    /**
     * Checks that the Hunter-team is empty.
     *
     * @return boolean - True if the Hunter-team is empty, false otherwise.
     */
    public static synchronized boolean isHunterMapEmpty() {
        return ManHuntUtilities.HUNTER_MAP.isEmpty();
    }

    /**
     * Checks that the Runner-team is empty.
     *
     * @return boolean - True if the Runner-team is empty, false otherwise.
     */
    public static synchronized boolean isRunnerMapEmpty() {
        return ManHuntUtilities.RUNNER_MAP.isEmpty();
    }

    /**
     * Returns a Collection containing all the Player-objects in the HUNTER_MAP.
     *
     * @return hunters - Collection of all the Player-objects in the HUNTER_MAP.
     */
    public static synchronized Collection<Player> getHunters() {
        return ManHuntUtilities.HUNTER_MAP.values();
    }

    /**
     * Returns a Collection containing all the Player-objects in the RUNNER_MAP.
     *
     * @return runners - Collection of all the Player-objects in the RUNNER_MAP.
     */
    public static synchronized Collection<Player> getRunners() {
        return ManHuntUtilities.RUNNER_MAP.values();
    }

    /**
     * Removes the specified player from the Hunter-team.
     *
     * @param playerUUID - UUID of the player that is to be removed from the Hunter-team.
     */
    public static synchronized boolean removeHunter(final UUID playerUUID) {
        return ManHuntUtilities.HUNTER_MAP.remove(playerUUID) != null;
    }

    /**
     * Removes the specified player from the Runner-team.
     *
     * @param playerUUID - UUID of the player that is to be removed from the Runner-team.
     */
    public static synchronized boolean removeRunner(final UUID playerUUID) {
        return ManHuntUtilities.RUNNER_MAP.remove(playerUUID) != null;
    }

    /**
     * Checks whether or not the specified player is in the Hunter-team.
     *
     * @param playerUUID - The UUID of the player that is to be checked.
     * @return - True if the specified player is in the Hunter-team, false otherwise.
     */
    public static synchronized boolean isRunner(final UUID playerUUID) {
        return ManHuntUtilities.RUNNER_MAP.containsKey(playerUUID);
    }

    /**
     * Checks if the specified String contains letters.
     *
     * @param argument - The String to be checked.
     * @return boolean - True if the String contains letters, false otherwise.
     */
    public static boolean hasLetters(String argument) {
        for (int x = 0; x < argument.length(); x++) {
            if (Character.isLetter(argument.charAt(x))) return true;
        }
        return false;
    }

    /**
     * Gets the UUID of the player with the specified playername.
     *
     * @param playerName - Name of the player.
     * @return UUID - The UUID of the player.
     */
    public static UUID getPlayerUUID(final String playerName) {
        return SERVER.getPlayerUniqueId(playerName);
    }

}

