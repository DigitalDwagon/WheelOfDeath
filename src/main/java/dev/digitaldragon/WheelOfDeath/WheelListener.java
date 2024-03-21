package dev.digitaldragon.WheelOfDeath;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Instant;
import java.util.function.Consumer;

public class WheelListener implements Listener {
    public static boolean isRunning = false;
    public static Instant lastRun = Instant.MIN;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.LEVER) return;
        String[] loc = WheelOfDeath.instance.getConfig().getString("leverLocation").split(" ");
        World world = Bukkit.getWorlds().get(0);

        Location leverLocation = new Location(world, Double.parseDouble(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]));
        if (!event.getClickedBlock().getLocation().equals(leverLocation)) return;
        event.setCancelled(true);
        if (isRunning) {
            event.getPlayer().sendMessage("The wheel is already spinning!");
            return;
        }
        if (lastRun.plusSeconds(3600).isAfter(Instant.now())) {
            event.getPlayer().sendMessage(String.format("The wheel is still cooling down! Try again in %sm %ss", (3600 - (Instant.now().getEpochSecond() - lastRun.getEpochSecond())) / 60, (3600 - (Instant.now().getEpochSecond() - lastRun.getEpochSecond())) % 60));
            return;
        }

        lastRun = Instant.now();

        isRunning = true;

        event.getPlayer().sendMessage("The wheel is spinning!");
        AnimationManager.loadAnimationFrame(WheelOfDeath.wheelLocation, 0);

        //pick a random number between 0 and 7
        //int choice = (int) (Math.random() * 8) ;
        int choice = 6;
        int delayIncrement = 1;
        int delay = 0;

        int frame = 0;

        while (true) {
            int finalFrame = frame;
            Bukkit.getScheduler().runTaskLater(WheelOfDeath.instance, () -> {
                AnimationManager.loadAnimationFrame(WheelOfDeath.wheelLocation, finalFrame);
            }, delay);
            frame++;

            if (frame > 7) {
                frame = 0;
            }

            if (delayIncrement > 10 && choice == frame) {
                Bukkit.getScheduler().runTaskLater(WheelOfDeath.instance, () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        runEffect(choice, player);
                    }
                    isRunning = false;
                }, delay);
                break;
            }

            delay += delayIncrement;
            // 25% chance to increase delay
            if ((frame + 1) % 4 == 0  && delay > 30) {
                delayIncrement++;
            }
        }




    }

    private void runEffect(int choice, Player spinner) {
        Component text = Component.text("The wheel has stopped on... ");
        switch (choice) {
            case 1 -> {
                broadcastMessage(text.append(Component.text("LIME!").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                effectAllPlayers(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 1)));
            }
            case 2 -> {
                broadcastMessage(text.append(Component.text("CYAN!").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                spinner.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
            }
            case 3 -> {
                broadcastMessage(text.append(Component.text("PINK!").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                effectAllPlayers(player -> player.getInventory().addItem(new ItemStack(Material.PINK_TULIP, 1)));
            }
            case 4 -> {
                broadcastMessage(text.append(Component.text("PURPLE!").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                effectAllPlayers(player -> player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16)));
            }
            case 5 -> {
                broadcastMessage(text.append(Component.text("LIGHT BLUE!").color(NamedTextColor.BLUE).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                spinner.getWorld().strikeLightningEffect(spinner.getLocation());
                spinner.damage(5);
            }
            case 6 -> {
                broadcastMessage(text.append(Component.text("BLUE!").color(NamedTextColor.DARK_BLUE).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));

                effectAllPlayers(player -> {
                    Location loc = player.getLocation();
                    Location offset = loc.add(5, 0, 5).clone();

                    //spawn 10 zombies 5 blocks from the player
                    for (int i = 0; i < 10; i++) {
                        Bukkit.getScheduler().runTask(WheelOfDeath.instance, () -> spawnZombie(loc));
                    }
                });



            }
            case 7 -> {
                broadcastMessage(text.append(Component.text("ORANGE!").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                effectAllPlayers(player -> player.getWorld().spawn(player.getLocation(), Cat.class));
            }
            case 0 -> {
                broadcastMessage(text.append(Component.text("YELLOW!").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD, TextDecoration.UNDERLINED)));
                broadcastMessage(Component.text("The sun shines down upon you.").color(NamedTextColor.YELLOW));
            }
        }
    }

    private void broadcastMessage(Component component) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(component);
        }
    }

    private void effectAllPlayers(Consumer<Player> func) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            func.accept(player);
        }
    }

    private void spawnZombie(Location loc) {
        World world = loc.getWorld();
        Zombie zombie = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);

        // Equip the zombie with full iron armor
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);

        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
    }

}
