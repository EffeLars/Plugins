package me.magicwands.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;


public class MagicWandsTabCompleter implements TabCompleter {

    private final List<String> wandNames = List.of("Fire", "Ice", "Nature", "Light", "Dark", "Witch", "Empire");
    private final List<String> itemNames = List.of("Horn", "FireBow", "ignatiusbow", "LightSword", "mortiferumbow");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

       
        if (args.length == 1) {
            if ("configure".startsWith(args[0].toLowerCase())) {
                suggestions.add("configure");
            }
            if ("wand".startsWith(args[0].toLowerCase())) {
                suggestions.add("wand");
            }
            if ("item".startsWith(args[0].toLowerCase())) {
                suggestions.add("item");
            }
            if ("getall".startsWith(args[0].toLowerCase())) {
                suggestions.add("getall");
            }
            if ("reload".startsWith(args[0].toLowerCase())) {
                suggestions.add("reload");
            }
            if ("toggleparticles".startsWith(args[0].toLowerCase())) {
                suggestions.add("toggleparticles");
            }
            if ("togglesounds".startsWith(args[0].toLowerCase())) {
                suggestions.add("togglesounds");
            }
            if ("togglelogs".startsWith(args[0].toLowerCase())) {
                suggestions.add("togglelogs");
            }
        } 
    
        else if (args.length == 2) {
            if ("wand".equalsIgnoreCase(args[0])) {
               
                for (String wand : wandNames) {
                    if (wand.toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(wand);
                    }
                }
            } 
            else if ("item".equalsIgnoreCase(args[0])) {
                
                for (String item : itemNames) {
                    if (item.toLowerCase().startsWith(args[1].toLowerCase())) {
                        suggestions.add(item);
                    }
                }
            }
            else if ("configure".equalsIgnoreCase(args[0])) {
                
                suggestions.add("wand");
                suggestions.add("item");
            }
        }
        
        else if (args.length == 3) {
            if ("configure".equalsIgnoreCase(args[0])) {
                if ("wand".equalsIgnoreCase(args[1])) {
                   
                    for (String wand : wandNames) {
                        if (wand.toLowerCase().startsWith(args[2].toLowerCase())) {
                            suggestions.add(wand);
                        }
                    }
                }
                else if ("item".equalsIgnoreCase(args[1])) {
                   
                    for (String item : itemNames) {
                        if (item.toLowerCase().startsWith(args[2].toLowerCase())) {
                            suggestions.add(item);
                        }
                    }
                }
            }
            
            else if ("wand".equalsIgnoreCase(args[0])) {
         
                for (String wand : wandNames) {
                    if (wand.toLowerCase().startsWith(args[2].toLowerCase())) {
                        suggestions.add(wand);
                    }
                }
            }
            else if ("item".equalsIgnoreCase(args[0])) {
              
                for (String item : itemNames) {
                    if (item.toLowerCase().startsWith(args[2].toLowerCase())) {
                        suggestions.add(item);
                    }
                }
            }
        } 
      
        else if (args.length == 4) {
            if ("configure".equalsIgnoreCase(args[0])) {
                if ("wand".equalsIgnoreCase(args[1]) || "item".equalsIgnoreCase(args[1])) {
                  
                    if ("setname".startsWith(args[3].toLowerCase())) {
                        suggestions.add("setname");
                    }
                    if ("setcolor".startsWith(args[3].toLowerCase())) {
                        suggestions.add("setcolor");
                    }
                    if ("setitem".startsWith(args[3].toLowerCase())) {
                        suggestions.add("setitem");
                    }
                }
            }
        }
        
        else if (args.length == 5) {
            if ("configure".equalsIgnoreCase(args[0])) {
                if ("wand".equalsIgnoreCase(args[1]) || "item".equalsIgnoreCase(args[1])) {
                    String action = args[3].toLowerCase();
                    if ("setname".equals(action)) {
                        suggestions.add("<new name>");
                    } else if ("setcolor".equals(action)) {
                    
                        suggestions.add("<color>");
                    } else if ("setitem".equals(action)) {
                      
                        for (String item : itemNames) {
                            if (item.toLowerCase().startsWith(args[4].toLowerCase())) {
                                suggestions.add(item);
                            }
                        }
                    }
                }
            }
        }

        return suggestions;
    }
}
