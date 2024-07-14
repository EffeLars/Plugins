/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityShootBowEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scoreboard.Scoreboard
 *  org.bukkit.scoreboard.ScoreboardManager
 *  org.bukkit.scoreboard.Team
 */
package me.rok.magicalwands.haldirion.customitems;

import java.util.ArrayList;
import me.lars.game.Main;
import me.lars.game.event.MortiferumWandEffect;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.HaldirionItems;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class HaldirionBow
implements Listener {
    private Scoreboard scoreboard;
    private Team arrowTeam;

    public HaldirionBow() {
        ScoreboardManager scoreboardManager = ((Main)Main.getPlugin(Main.class)).getServer().getScoreboardManager();
        this.scoreboard = scoreboardManager.getMainScoreboard();
        this.arrowTeam = this.scoreboard.getTeam("HaldirionArrow");
        if (this.arrowTeam == null) {
            this.arrowTeam = this.scoreboard.registerNewTeam("HaldirionArrow");
        }
    }

    private void spawnparticles(Player player) {
        MortiferumWandEffect wandAnimation = new MortiferumWandEffect(player);
        wandAnimation.start();
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        Player player;
        if (event.getEntity() instanceof Player && event.getProjectile().getType() == EntityType.ARROW && (player = (Player)event.getEntity()).getScoreboardTags().contains("HaldirionBow") && player.getItemInHand().getItemMeta().getDisplayName().equals(HaldirionItems.HaldirionBow.getItemMeta().getDisplayName())) {
            final Arrow arrow = (Arrow)event.getProjectile();
            this.arrowTeam.addEntry(arrow.getUniqueId().toString());
            new BukkitRunnable(){

                public void run() {
                    if (!arrow.isDead() && !arrow.isOnGround()) {
                        arrow.setDamage(arrow.getDamage() + 1.0);
                        player.getWorld().spawnParticle(Particle.CLOUD, arrow.getLocation(), 2, 0.5, 0.5, 0.5, 0.01);
                        player.getWorld().spawnParticle(Particle.ASH, arrow.getLocation(), 5, 0.0, 0.0, 0.0, 0.03);
                        player.getWorld().spawnParticle(Particle.SPELL_INSTANT, arrow.getLocation(), 2, 0.5, 0.5, 0.5, 0.03);
                        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, arrow.getLocation(), 50, 0.5, 0.5, 0.5, 0.03);
                    } else {
                        HaldirionBow.this.arrowTeam.removeEntry(arrow.getUniqueId().toString());
                        player.getWorld().createExplosion(arrow.getLocation(), 0.0f);
                        player.getWorld().spawnParticle(Particle.CLOUD, arrow.getLocation(), 22, 0.5, 0.5, 0.5, 0.01);
                        player.getWorld().spawnParticle(Particle.ASH, arrow.getLocation(), 22, 0.0, 0.0, 0.0, 0.03);
                        player.getWorld().spawnParticle(Particle.SPELL_INSTANT, arrow.getLocation(), 22, 0.5, 0.5, 0.5, 0.03);
                        Location centerLocation = player.getLocation();
                        double radius = 3.0;
                        Bukkit.broadcast((String)(String.valueOf(ChatUtil.color("&7[&2&lLOG&f: &f" + player.getName() + " &7shot a &cHaldirion Special Arrow &7at the location: &7" + arrow.getLocation().getBlockX() + ChatUtil.color("&a, &7") + arrow.getLocation().getBlockY())) + ChatUtil.color("&a, &7") + arrow.getLocation().getBlockZ() + ChatUtil.color("&7]")), (String)"admin.log");
                        final ArrayList<String> playerNames = new ArrayList<String>();
                        for (final Player target : centerLocation.getWorld().getPlayers()) {
                            playerNames.add(target.getName());
                            playerNames.remove(player.getName());
                            if (!(target.getLocation().distance(arrow.getLocation()) <= radius)) continue;
                            new BukkitRunnable(){
                                private int count = 0;

                                public void run() {
                                    ++this.count;
                                    player.getWorld().spawnParticle(Particle.FLASH, target.getLocation().add(0.0, 2.0, 0.0), 20, 0.5, 2.5, 0.5, 0.01);
                                    if (this.count >= 12) {
                                        this.cancel();
                                        playerNames.clear();
                                    }
                                }
                            }.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 4L);
                            target.sendMessage(ChatUtil.color("&7[&cMagic&7] &cYou get blinded because of a pure Light arrow..."));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                        }
                        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, arrow.getLocation(), 50, 0.5, 0.5, 0.5, 0.03);
                        player.getScoreboardTags().remove("HaldirionBow");
                        this.cancel();
                    }
                }
            }.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 1L);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Arrow arrow;
        if (event.getEntityType() == EntityType.ARROW && this.arrowTeam.hasEntry((arrow = (Arrow)event.getEntity()).getUniqueId().toString())) {
            this.arrowTeam.removeEntry(arrow.getUniqueId().toString());
        }
    }
}

