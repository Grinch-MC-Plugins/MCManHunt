package io.github.thesummergrinch.mcmanhunt.commands.roles.player;

import io.github.thesummergrinch.mcmanhunt.utils.GameFlowUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JoinRunnersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && ((Player) sender).isOnline() && !GameFlowUtilities.isGameInProgress()) {
            final Player player = ((Player) sender).getPlayer();
            final UUID playerUUID = player.getUniqueId();
            if (GameFlowUtilities.areTeamsRandomized()) {
                player.sendMessage("The game is in random-mode! Use /joinrandomteam to participate in this game!");
                return true;
            } else if (ManHuntUtilities.isHunter(playerUUID)) {
                player.sendMessage("You are already a member of the Hunter-team! To leave the Hunter-team and join the Hunter-team, use /leavehunters and /joinrunners.");
            } else if (ManHuntUtilities.isRunner(playerUUID)) {
                player.sendMessage("You are already a member of the Runner-team.");
            } else if (ManHuntUtilities.isRunnerTeamFull()) {
                player.sendMessage("The Runner-team has reached maximum capacity. Please join another team.");
            } else if (ManHuntUtilities.addRunner(player)) {
                ManHuntUtilities.broadcastMessage(player.getName() + " has joined the Runner-team!");
            } else {
                player.sendMessage("Something went wrong. The server could not add you to the Runner-team. Please contact the plugin-author.");
            }
            return true;
        }
        return false;
    }

}
