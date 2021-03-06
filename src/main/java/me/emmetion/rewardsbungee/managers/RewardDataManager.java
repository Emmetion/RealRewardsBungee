package me.emmetion.rewardsbungee.managers;

import me.emmetion.rewardsbungee.RewardsBungee;
import me.emmetion.rewardsbungee.database.Database;
import me.emmetion.rewardsbungee.database.RewardData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.entity.Player;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * The type Reward data manager.
 */
public class RewardDataManager {

    /**
     * The constant playerDataHashMap.
     */
    public static HashMap<String, RewardData> playerDataHashMap = new HashMap<>();


    /**
     * Instantiates a new Reward data manager.
     */
    public RewardDataManager() {

    }

    /**
     * Has player data boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasPlayerData(Player player){
        ProxiedPlayer proxiedPlayer = RewardsBungee.plugin.getProxy().getPlayer(player.getUniqueId().toString());
        return hasPlayerData(proxiedPlayer);
    }

    /**
     * Has player data boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public static boolean hasPlayerData(ProxiedPlayer player) {
        return playerDataHashMap.containsKey(player.getUniqueId().toString());
    }

    /**
     * Add player data.
     *
     * @param player the player
     */
    public static void addPlayerData(Player player){
        ProxiedPlayer proxiedPlayer = RewardsBungee.plugin.getProxy().getPlayer(player.getUniqueId().toString());
        addPlayerData(proxiedPlayer);
    }

    /**
     * Add player data.
     *
     * @param player the player
     */
    public static void addPlayerData(ProxiedPlayer player) {
        String uuid = "";
        int tier1 = 0, tier2 = 0, tier3 = 0;
        try {
            PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM Rewards WHERE PlayerUUID=?");
            System.out.println("Attempting to retrieve data from H2!");
            preparedStatement.setString(1, player.getUniqueId().toString());

            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {

                uuid = result.getString("PlayerUUID");
                tier1 = result.getInt("Tier1");
                tier2 = result.getInt("Tier2");
                tier3 = result.getInt("Tier3");

                playerDataHashMap.put(player.getUniqueId().toString(), new RewardData(uuid, tier1, tier2, tier3));

                System.out.println("successfully placed data into playerDataHashMap!");

                return;
            }


        } catch (SQLException e) {
            System.out.println("Issue when retrieving playerdata from h2!");

            e.printStackTrace();
        }


        if (uuid == "") {
            System.out.println("Issue inserting data into table, doesn't exist!");
            return;
        }

        playerDataHashMap.put(player.getUniqueId().toString(), new RewardData(uuid, tier1, tier2, tier3));
    }

    /**
     * Remove player data.
     *
     * @param player the player
     */
    public static void removePlayerData(Player player){
        ProxiedPlayer proxiedPlayer = RewardsBungee.plugin.getProxy().getPlayer(player.getUniqueId().toString());
        removePlayerData(proxiedPlayer);
    }

    /**
     * Remove player data.
     *
     * @param player the player
     */
    public static void removePlayerData(ProxiedPlayer player) {
        if (hasPlayerData(player)) {
            playerDataHashMap.remove(player.getUniqueId().toString());
        }
    }

    /**
     * Get reward data on Player.
     *
     * @param player the player
     * @return the reward data
     */
    public static RewardData getRewardData(Player player){
        ProxiedPlayer proxiedPlayer = RewardsBungee.plugin.getProxy().getPlayer(player.getUniqueId().toString());
        return getRewardData(proxiedPlayer);
    }

    /**
     * Gets reward data.
     *
     * @param player the player
     * @return the reward data
     */
    public static RewardData getRewardData(ProxiedPlayer player) {

        if (hasPlayerData(player)) {
            String uuid = player.getUniqueId().toString();
            return playerDataHashMap.get(uuid);
        }

        return null;
    }


    /**
     * Gets player data hash map.
     *
     * @return the player data hash map
     */
    public HashMap<String, RewardData> getPlayerDataHashMap() {
        return playerDataHashMap;
    }

}
