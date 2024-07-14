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
package me.rok.magicalwands.fire.customitems;

import me.lars.game.Main;
import me.lars.game.utils.ChatUtil;
import me.lars.game.wands.FireItems;
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

public class IgnatiusBow
implements Listener {
    private Scoreboard scoreboard;
    private Team arrowTeam;

    public IgnatiusBow() {
        ScoreboardManager scoreboardManager = ((Main)Main.getPlugin(Main.class)).getServer().getScoreboardManager();
        this.scoreboard = scoreboardManager.getMainScoreboard();
        this.arrowTeam = this.scoreboard.getTeam("IgnatiusArrow");
        if (this.arrowTeam == null) {
            this.arrowTeam = this.scoreboard.registerNewTeam("IgnatiusArrow");
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        Player player;
        if (event.getEntity() instanceof Player && event.getProjectile().getType() == EntityType.ARROW && (player = (Player)event.getEntity()).getScoreboardTags().contains("IgnatiusBow") && player.getItemInHand().getItemMeta().getDisplayName().equals(FireItems.IgnatiusBow.getItemMeta().getDisplayName())) {
            final Arrow arrow = (Arrow)event.getProjectile();
            this.arrowTeam.addEntry(arrow.getUniqueId().toString());
            new BukkitRunnable(){

                public void run() {
                    if (!arrow.isDead() && !arrow.isOnGround()) {
                        arrow.setDamage(arrow.getDamage() + 1.0);
                        arrow.getWorld().spawnParticle(Particle.SPELL_MOB, arrow.getLocation(), 20, 0.2, 0.3, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 20, 0.0, 0.0, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 1, 0.0, 0.0, 0.0, 0.01);
                        arrow.getWorld().spawnParticle(Particle.ASH, arrow.getLocation(), 5, 0.0, 0.0, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.SMOKE_NORMAL, arrow.getLocation(), 5, 0.0, 0.0, 0.0, 0.02);
                    } else {
                        IgnatiusBow.this.arrowTeam.removeEntry(arrow.getUniqueId().toString());
                        player.getWorld().createExplosion(arrow.getLocation(), 0.0f);
                        arrow.getWorld().spawnParticle(Particle.SPELL_MOB, arrow.getLocation(), 20, 0.2, 0.3, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.FLAME, arrow.getLocation(), 20, 0.0, 0.0, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.LAVA, arrow.getLocation(), 1, 0.0, 0.0, 0.0, 0.01);
                        arrow.getWorld().spawnParticle(Particle.ASH, arrow.getLocation(), 5, 0.0, 0.0, 0.0, 0.03);
                        arrow.getWorld().spawnParticle(Particle.SMOKE_NORMAL, arrow.getLocation(), 5, 0.0, 0.0, 0.0, 0.02);
                        Location centerLocation = player.getLocation();
                        double radius = 3.0;
                        Bukkit.broadcast((String)(String.valueOf(ChatUtil.color("&7[&2&lLOG&f: &f" + player.getName() + " &7shot a &6Ignatius Special Arrow &7at the location: &7" + arrow.getLocation().getBlockX() + ChatUtil.color("&a, &7") + arrow.getLocation().getBlockY())) + ChatUtil.color("&a, &7") + arrow.getLocation().getBlockZ() + ChatUtil.color("&7]")), (String)"admin.log");
                        for (final Player target : centerLocation.getWorld().getPlayers()) {
                            if (!(target.getLocation().distance(arrow.getLocation()) <= radius)) continue;
                            new BukkitRunnable(){
                                private int count = 0;

                                public void run() {
                                    ++this.count;
                                    arrow.getWorld().spawnParticle(Particle.SPELL_MOB, target.getLocation(), 20, 0.2, 0.3, 0.0, 0.03);
                                    arrow.getWorld().spawnParticle(Particle.FLAME, target.getLocation(), 20, 0.0, 0.0, 0.0, 0.03);
                                    arrow.getWorld().spawnParticle(Particle.LAVA, target.getLocation(), 1, 0.0, 0.0, 0.0, 0.01);
                                    arrow.getWorld().spawnParticle(Particle.ASH, target.getLocation(), 5, 0.0, 0.0, 0.0, 0.03);
                                    arrow.getWorld().spawnParticle(Particle.SMOKE_NORMAL, target.getLocation(), 5, 0.0, 0.0, 0.0, 0.02);
                                    if (this.count >= 12) {
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer((Plugin)Main.getPlugin(Main.class), 0L, 4L);
                            target.sendMessage(ChatUtil.color("&7[&cMagic&7] &cYou got hit by an arrow filled with &6Eternal Flame&4, &cYour skin lights up from the fire inside you."));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0));
                            target.setFireTicks(100);
                        }
                        player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, arrow.getLocation(), 50, 0.5, 0.5, 0.5, 0.03);
                        player.getScoreboardTags().remove("IgnatiusBow");
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

