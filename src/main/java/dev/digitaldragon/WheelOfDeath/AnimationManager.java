package dev.digitaldragon.WheelOfDeath;

import com.fastasyncworldedit.core.extent.clipboard.io.schematic.MinecraftStructure;
import com.sk89q.worldedit.extent.clipboard.io.NBTSchematicReader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.structure.Structure;

public class AnimationManager {
    //lime blue purple magenta red orange yellow cyan brown gray
    private static final Material[] BLOCKS = new Material[] {
            Material.LIME_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PINK_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.YELLOW_CONCRETE,

            //dupe of above list
            Material.LIME_CONCRETE,
            Material.CYAN_CONCRETE,
            Material.PINK_CONCRETE,
            Material.PURPLE_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.BLUE_CONCRETE,
            Material.ORANGE_CONCRETE,
            Material.YELLOW_CONCRETE,

    };
    public static void loadAnimationFrame(Location location, int frame) {
        if (frame > 7) return;

        Material z = Material.WHITE_CONCRETE;
        Material i = Material.AIR;
        //1-8 as material[0] - material[7]
        Material a = BLOCKS[0 + frame];
        Material b = BLOCKS[1 + frame];
        Material c = BLOCKS[2 + frame];
        Material d = BLOCKS[3 + frame];
        Material e = BLOCKS[4 + frame];
        Material f = BLOCKS[5 + frame];
        Material g = BLOCKS[6 + frame];
        Material h = BLOCKS[7 + frame];


        Material[][] blocks = new Material[][] {
                {i,i,i,i,i,i,z,z,z,i,i,i,i,i,i},
                {i,i,i,i,z,z,a,a,a,z,z,i,i,i,i},
                {i,i,z,z,h,a,a,a,a,a,b,z,z,i,i},
                {i,i,z,h,h,h,a,a,a,b,b,b,z,i,i},
                {i,z,h,h,h,h,a,a,a,b,b,b,b,z,i},
                {i,z,g,h,h,h,h,a,b,b,b,b,c,z,i},
                {z,g,g,g,g,h,h,a,b,b,c,c,c,c,z},
                {z,g,g,g,g,g,g,z,c,c,c,c,c,c,z},
                {z,g,g,g,g,f,f,e,d,d,c,c,c,c,z},
                {i,z,g,f,f,f,f,e,d,d,d,d,c,z,i},
                {i,z,f,f,f,f,e,e,e,d,d,d,d,z,i},
                {i,i,z,f,f,f,e,e,e,d,d,d,z,i,i},
                {i,i,z,z,f,e,e,e,e,e,d,z,z,i,i},
                {i,i,i,i,z,z,e,e,e,z,z,i,i,i,i},
                {i,i,i,i,i,i,z,z,z,i,i,i,i,i,i},
            };

        //place blocks starting from location and moving down
        for (int y = 0; y < blocks.length; y++) {
            for (int x = 0; x < blocks[y].length; x++) {
                Material block = blocks[y][x];
                location.clone().add(x, 0, y).getBlock().setType(block);
            }
        }

    }
}
