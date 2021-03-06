package io.github.thesummergrinch.mcmanhunt;

import io.github.thesummergrinch.mcmanhunt.commands.config.SetMaxHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.config.SetMaxRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.gamestate.*;
import io.github.thesummergrinch.mcmanhunt.commands.info.ListHuntersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.info.ListRunnersCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.roles.AddHunterCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.roles.AddRunnerCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.roles.ResetPlayerRolesCommandExecutor;
import io.github.thesummergrinch.mcmanhunt.commands.roles.player.*;
import io.github.thesummergrinch.mcmanhunt.eventhandlers.*;
import io.github.thesummergrinch.mcmanhunt.utils.ConfigurationUtilities;
import io.github.thesummergrinch.mcmanhunt.utils.ManHuntUtilities;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MCManhunt extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigurationUtilities.loadConfig(this);
        this.getServer().getPluginManager().registerEvents(new OnCompassInteractEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnRespawnEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerMoveEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnEnderDragonDeathEventHandler(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDimensionChangeEventHandler(), this);
        this.getCommand("addhunter").setExecutor(new AddHunterCommandExecutor());
        this.getCommand("addrunner").setExecutor(new AddRunnerCommandExecutor());
        this.getCommand("resetplayerroles").setExecutor(new ResetPlayerRolesCommandExecutor());
        this.getCommand("startgame").setExecutor(new StartGameCommandExecutor());
        this.getCommand("pausegame").setExecutor(new PauseGameCommandExecutor());
        this.getCommand("resumegame").setExecutor(new ResumeGameCommandExecutor());
        this.getCommand("stopgame").setExecutor(new StopGameCommandExecutor());
        this.getCommand("listrunners").setExecutor(new ListRunnersCommandExecutor());
        this.getCommand("listhunters").setExecutor(new ListHuntersCommandExecutor());
        this.getCommand("joinhunters").setExecutor(new JoinHuntersCommandExecutor());
        this.getCommand("joinrunners").setExecutor(new JoinRunnersCommandExecutor());
        this.getCommand("leaverunners").setExecutor(new LeaveRunnersCommandExecutor());
        this.getCommand("leavehunters").setExecutor(new LeaveHuntersCommandExecutor());
        this.getCommand("setmaxhunters").setExecutor(new SetMaxHuntersCommandExecutor());
        this.getCommand("setmaxrunners").setExecutor(new SetMaxRunnersCommandExecutor());
        this.getCommand("joinrandomteam").setExecutor(new JoinRandomTeamCommandExecutor());
        this.getCommand("setrandomgame").setExecutor(new SetRandomGameCommandExecutor());
        ManHuntUtilities.updateFromConfig();
        if (ConfigurationUtilities.getBoolean(this, "allow-metrics") && !ManHuntUtilities.isFirstRun()) {
            getLogger().log(Level.CONFIG, "Metrics are enabled.");
            final int pluginID = 8784;
            new Metrics(this, pluginID);
        }
    }

    @Override
    public void onDisable() {
        ConfigurationUtilities.saveConfig(this);
    }
}
