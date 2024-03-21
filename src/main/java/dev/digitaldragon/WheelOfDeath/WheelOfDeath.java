package dev.digitaldragon.WheelOfDeath;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WheelOfDeath extends JavaPlugin implements Listener {
    public static WheelOfDeath instance;
    public static Location wheelLocation;
    public void onEnable() {
        instance = this;
        getLogger().info("WheelOfDeath has been enabled!");
        Bukkit.getPluginManager().registerEvents(new WheelListener(), this);
        //get overworld
        World world = Bukkit.getWorlds().get(0);
        //AnimationManager.loadAnimationFrame(new Location(world, 0, 200, 0), 0);
        //load config
        this.saveDefaultConfig();
        this.getConfig().getString("wheelLocation");
        //split string and get location
        String[] loc = this.getConfig().getString("wheelLocation").split(" ");
        wheelLocation = new Location(world, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));
        AnimationManager.loadAnimationFrame(wheelLocation, 0);
    }

    public void onDisable() {
        getLogger().info("WheelOfDeath has been disabled!");
    }

}