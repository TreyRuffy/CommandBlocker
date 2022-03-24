package me.treyruffy.commandblocker.bukkit.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.treyruffy.commandblocker.Log;
import me.treyruffy.commandblocker.MethodInterface;
import me.treyruffy.commandblocker.Universal;
import me.treyruffy.commandblocker.bukkit.BukkitMain;
import me.treyruffy.commandblocker.bukkit.Variables;
import me.treyruffy.commandblocker.bukkit.config.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class DisabledGui implements Listener {

    private final HashMap<String, String> lookingFor = new HashMap<>();
    private final HashMap<String, Boolean> lookingForOp = new HashMap<>();
    private final HashMap<String, String> command = new HashMap<>();

    @SuppressWarnings("deprecation")
    public void openGui(Player p, String args, boolean op) {
        if (!p.hasPermission("cb.edit") && !op) {
            return;
        } else if (!p.hasPermission("cb.editop") && op) {
            return;
        }
        MethodInterface mi = Universal.get().getMethods();
        Inventory inv;
        if (op) {
            inv = Bukkit.createInventory(null, 27,
                    Variables.translateVariablesToLegacyString(mi.getOldMessage("EditOpGui",
                            "EditCommandInventoryName"), p));
        } else {
            inv = Bukkit.createInventory(null, 36,
                    Variables.translateVariablesToLegacyString(mi.getOldMessage("EditGui", "EditCommandInventoryName"), p));
        }
        SharedGui.setupWool();
        ItemStack commandSlot = new ItemStack(Material.valueOf(SharedGui.pinkGlassBlock), 1, (byte) SharedGui.pinkGlassBlockId);

        if (BukkitMain.coloredGlassPane()) {
            String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");

            String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

            ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) SharedGui.magentaId);
            ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) SharedGui.pinkId);

            magentaPinkItemSlots(magentaGlassSlots, pinkGlassSlots);

            for (int i = 0; i <= 8; i += 2) {
                inv.setItem(i, pinkGlassSlots);
                if (op) {
                    if ((i + 18) < 26) {
                        inv.setItem(i + 18, pinkGlassSlots);
                    }
                } else {
                    inv.setItem(i + 26, pinkGlassSlots);
                }
            }
            for (int i = 1; i <= 9; i += 2) {
                inv.setItem(i, magentaGlassSlots);
                if (op) {
                    if ((i + 18) < 26) {
                        inv.setItem(i + 18, magentaGlassSlots);
                    }
                } else {
                    inv.setItem(i + 26, magentaGlassSlots);
                }
            }
            inv.setItem(17, magentaGlassSlots);
            inv.setItem(18, pinkGlassSlots);
        }

        ItemStack firstSlot = null;
        if (!op) {
            firstSlot = new ItemStack(Material.valueOf(SharedGui.pinkWool), 1, (byte) SharedGui.pinkId);
        }
        ItemStack secondSlot = new ItemStack(Material.valueOf(SharedGui.magentaWool), 1, (byte) SharedGui.magentaId);
        ItemStack thirdSlot = new ItemStack(Material.valueOf(SharedGui.worldsItem), 1, (byte) SharedGui.worldsItemId);
        ItemStack fourthSlot = new ItemStack(Material.valueOf(SharedGui.blueWool), 1, (byte) SharedGui.blueId);
        ItemStack fifthSlot = new ItemStack(Material.valueOf(SharedGui.lightBlueWool), 1, (byte) SharedGui.lightBlueId);
        ItemStack sixthSlot;

        FileConfiguration getDisabledConfig = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

        if (getDisabledConfig.getString(configurationSectionName + "." + args + ".NoTabComplete") == null) {
            getDisabledConfig.set(configurationSectionName + "." + args + ".NoTabComplete", true);
            if (op) {
                mi.saveOpBlockConfig();
            } else {
                mi.saveDisabledCommandsConfig();
            }

        }
        if (!getDisabledConfig.getBoolean(configurationSectionName + "." + args + ".NoTabComplete")) {
            sixthSlot = new ItemStack(Material.valueOf(SharedGui.redWool), 1, (byte) SharedGui.redWoolId);
        } else {
            sixthSlot = new ItemStack(Material.valueOf(SharedGui.greenWool), 1, (byte) SharedGui.greenWoolId);
        }

        commandSlotItem(mi, p, args, inv, commandSlot, op);


        if (!op) {
            ItemMeta firstSlotMeta = firstSlot.getItemMeta();
            assert firstSlotMeta != null;
            firstSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "PermissionItemName")), p));

            Component permissionComponent = mi.getOldMessage(configurationSectionName, args + ".Permission",
                            getDisabledConfig)
                    .color(NamedTextColor.WHITE);

            String currentPermission = LegacyComponentSerializer.legacySection().serialize(permissionComponent);

            List<String> firstSlotLore = new ArrayList<>();
            for (Component messageComponent : mi.getOldMessages(editConfigurationSectionName, "PermissionItemLore")) {
                String message = LegacyComponentSerializer.legacySection().serialize(messageComponent);
                if (message.contains("%m") && (!getDisabledConfig.getStringList(
                        configurationSectionName + "." + args + ".Permission").isEmpty())) {
                    if (!ChatColor.stripColor(message.replace("%p", "")).equalsIgnoreCase(""))
                        firstSlotLore.add(message.replace("%p", ""));
                    firstSlotLore.add(currentPermission);
                } else {
                    firstSlotLore.add(message.replace("%p", currentPermission));
                }
            }
            firstSlotMeta.setLore(firstSlotLore);
            firstSlot.setItemMeta(firstSlotMeta);
            if (SharedGui.pinkWool.equalsIgnoreCase("SKULL_ITEM") || SharedGui.pinkWool.equalsIgnoreCase("PLAYER_HEAD")) {
                SkullMeta meta = (SkullMeta) firstSlot.getItemMeta();
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
                String playerCommandValue =
                        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY3MTY1MjI4ZWNlN2FhYzg3NmExMTA4ZjczYzJiNzYwNDQ0MGJlMWRmZjQzZWJkMmZhODQxOTU0ODkxZDhjIn19fQ==";
                gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
                Field profileField;
                try {
                    profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, gameProfile);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                    e.printStackTrace();
                }
                firstSlot.setItemMeta(meta);
            }
            inv.setItem(11, firstSlot);
        }


        ItemMeta secondSlotMeta = secondSlot.getItemMeta();
        assert secondSlotMeta != null;
        secondSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "MessageItemName")), p));

        List<String> secondSlotLore = new ArrayList<>();
        for (Component messageComponent : mi.getOldMessages(editConfigurationSectionName, "MessageItemLore")) {
            String message = LegacyComponentSerializer.legacySection().serialize(messageComponent);
            if (message.contains("%m") && (!getDisabledConfig.getStringList(
                    configurationSectionName + "." + args + ".Message").isEmpty())) {
                if (!ChatColor.stripColor(message.replace("%m", "")).equalsIgnoreCase(""))
                    secondSlotLore.add(message.replace("%m", ""));
                for (Component messageList : mi.getOldMessages(configurationSectionName, args +
                        ".Message", getDisabledConfig)) {
                    messageList = messageList.color(NamedTextColor.WHITE);
                    String messageListString = LegacyComponentSerializer.legacySection().serialize(messageList);
                    secondSlotLore.add(messageListString);
                }
            } else {
                Component component = mi.getOldMessage(configurationSectionName, args +
                        ".Message", getDisabledConfig).color(NamedTextColor.WHITE);
                String componentString = LegacyComponentSerializer.legacySection().serialize(component);
                secondSlotLore.add(message.replace("%m", componentString));
            }
        }
        secondSlotMeta.setLore(secondSlotLore);
        secondSlot.setItemMeta(secondSlotMeta);
        if (SharedGui.magentaWool.equalsIgnoreCase("SKULL_ITEM") || SharedGui.magentaWool.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) secondSlot.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String playerCommandValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjAyYWYzY2EyZDVhMTYwY2ExMTE0MDQ4Yjc5NDc1OTQyNjlhZmUyYjFiNWVjMjU1ZWU3MmI2ODNiNjBiOTliOSJ9fX0=";
            gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            secondSlot.setItemMeta(meta);
        }
        if (op) {
            inv.setItem(11, secondSlot);
        } else {
            inv.setItem(12, secondSlot);
        }


        ItemMeta thirdSlotMeta = thirdSlot.getItemMeta();
        assert thirdSlotMeta != null;
        thirdSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "WorldsItemName")), p));
        thirdSlot.setItemMeta(thirdSlotMeta);
        if (SharedGui.worldsItem.equalsIgnoreCase("SKULL_ITEM") || SharedGui.worldsItem.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) thirdSlot.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String worldHeadValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJmMDMyMTk5NjRlOTY2N2ZhMWY0ZDdhNWRlYTA1ODQwZTg5YTRkODgyNDAzNjQxYTMwMTE2MjNkOTUifX19";
            gameProfile.getProperties().put("textures", new Property("textures", worldHeadValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            thirdSlot.setItemMeta(meta);
        }
        if (op) {
            inv.setItem(12, thirdSlot);
        } else {
            inv.setItem(13, thirdSlot);
        }


        ItemMeta fourthSlotMeta = fourthSlot.getItemMeta();
        assert fourthSlotMeta != null;
        fourthSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "PlayerCommandsItemName")), p));
        fourthSlot.setItemMeta(fourthSlotMeta);
        if (op) {
            inv.setItem(13, fourthSlot);
        } else {
            inv.setItem(14, fourthSlot);
        }


        ItemMeta fifthSlotMeta = fifthSlot.getItemMeta();
        assert fifthSlotMeta != null;
        fifthSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "ConsoleCommandsItemName")), p));
        fifthSlot.setItemMeta(fifthSlotMeta);
        if (SharedGui.lightBlueWool.equalsIgnoreCase("SKULL_ITEM") || SharedGui.lightBlueWool.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) fifthSlot.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String playerCommandValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdkOTc3ZTI4ODlhMWY1MGJiYjIzZDQyMjI3NWFiOWRhYTNjMTUwYzg5ODMxZTc5ODRiNDliNDA5ZGIwNjcifX19";
            gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            fifthSlot.setItemMeta(meta);
        }
        if (op) {
            inv.setItem(14, fifthSlot);
        } else {
            inv.setItem(15, fifthSlot);
        }


        ItemMeta sixthSlotMeta = sixthSlot.getItemMeta();
        assert sixthSlotMeta != null;
        sixthSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, "EditGui", "NoTabCompletion")), p));
        sixthSlot.setItemMeta(sixthSlotMeta);
        if (op) {
            inv.setItem(15, sixthSlot);
        } else {
            inv.setItem(22, sixthSlot);
        }


        Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
    }

    private void commandSlotItem(MethodInterface mi, Player p, String args, Inventory inv, ItemStack commandSlot,
                                 boolean op) {
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";
        ItemMeta commandSlotMeta = commandSlot.getItemMeta();
        assert commandSlotMeta != null;
        commandSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "InventoryCommandName").replace("%c", args)), p));
        commandSlot.setItemMeta(commandSlotMeta);
        inv.setItem(4, commandSlot);
    }

    static void magentaPinkItemSlots(ItemStack magentaGlassSlots, ItemStack pinkGlassSlots) {
        ItemMeta magentaGlassMeta = magentaGlassSlots.getItemMeta();
        assert magentaGlassMeta != null;
        magentaGlassMeta.setDisplayName(" ");
        magentaGlassSlots.setItemMeta(magentaGlassMeta);

        ItemMeta pinkGlassMeta = pinkGlassSlots.getItemMeta();
        assert pinkGlassMeta != null;
        pinkGlassMeta.setDisplayName(" ");
        pinkGlassSlots.setItemMeta(pinkGlassMeta);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }

        MethodInterface mi = Universal.get().getMethods();

        if (e.getView().getTitle().equals(Variables.translateVariablesToLegacyString(mi.getOldMessage("EditGui",
                "EditCommandInventoryName"), e.getWhoClicked())) || e.getView().getTitle().equals(
                Variables.translateVariablesToLegacyString(mi.getOldMessage("EditOpGui",
                        "EditCommandInventoryName"), e.getWhoClicked()))) {
            boolean op = !e.getView().getTitle().equals(Variables.translateVariablesToLegacyString(mi.getOldMessage(
                    "EditGui", "EditCommandInventoryName"), e.getWhoClicked()));

            FileConfiguration getDisabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                    (FileConfiguration) mi.getDisabledCommandsConfig();
            if (!op && !e.getWhoClicked().hasPermission("cb.edit")) {
                return;
            } else if (op && !e.getWhoClicked().hasPermission("cb.editop")) {
                return;
            }
            String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
            String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

            if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName()) || e.getCurrentItem() == null) {
                e.setCancelled(true);
                return;
            }
            String cmd = ChatColor.stripColor(Objects.requireNonNull(Objects.requireNonNull(e.getInventory().getItem(4)).getItemMeta()).getDisplayName()).substring(1);
            Player p = (Player) e.getWhoClicked();
            if (!op && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "PermissionItemName")), p))) {
                p.closeInventory();

                for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "ChangePermission")) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%c", cmd);
                    mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
                }
                lookingFor.put(p.getUniqueId().toString(), "permission");
                lookingForOp.put(p.getUniqueId().toString(), true);
                command.put(p.getUniqueId().toString(), cmd);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "MessageItemName")), p))) {
                p.closeInventory();

                for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "ChangeMessage")) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%c", cmd);
                    mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
                }
                lookingFor.put(p.getUniqueId().toString(), "message");
                lookingForOp.put(p.getUniqueId().toString(), op);
                command.put(p.getUniqueId().toString(), cmd);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "WorldsItemName")), p))) {
                openWorldGui(mi, p, cmd, op);

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "PlayerCommandsItemName")), p))) {
                openPCommandsGui(mi, p, cmd, op);

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "ConsoleCommandsItemName")), p))) {
                openCCommandsGui(mi, p, cmd, op);

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "NoTabCompletion")), p))) {
                if (!getDisabled.getBoolean(configurationSectionName + "." + cmd + ".NoTabComplete")) {
                    getDisabled.set(configurationSectionName + "." + cmd + ".NoTabComplete", true);
                    if (op) {
                        mi.saveOpBlockConfig();
                    } else {
                        mi.saveDisabledCommandsConfig();
                    }
                    Log.addLog(mi, p.getName() + ": Changed tab completion for /" + cmd + " from off to on in " +
                            (op ? "opblock.yml" : "disabled.yml"));
                } else {
                    getDisabled.set(configurationSectionName + "." + cmd + ".NoTabComplete", false);
                    if (op) {
                        mi.saveOpBlockConfig();
                    } else {
                        mi.saveDisabledCommandsConfig();
                    }
                    Log.addLog(mi, p.getName() + ": Changed tab completion for /" + cmd + " from on to off in " +
                            (op ? "opblock.yml" : "disabled.yml"));
                }

                openGui(p, cmd, op);

            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, "EditGui", "EditWorldsInventoryName")), e.getWhoClicked())) ||
                e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                        Messages.getMessage(mi, "EditOpGui", "EditWorldsInventoryName")), e.getWhoClicked()))) {

            boolean op = !e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, "EditGui", "EditWorldsInventoryName")), e.getWhoClicked()));

            FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                    (FileConfiguration) mi.getDisabledCommandsConfig();
            if (!op && !e.getWhoClicked().hasPermission("cb.edit")) {
                return;
            } else if (op && !e.getWhoClicked().hasPermission("cb.editop")) {
                return;
            }
            String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
            String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

            if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
                e.setCancelled(true);
                return;
            }
            String cmd = ChatColor.stripColor(Objects.requireNonNull(Objects.requireNonNull(e.getInventory().getItem(4)).getItemMeta()).getDisplayName()).substring(1);
            Player p = (Player) e.getWhoClicked();

            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "GoBackItemName")), p))) {
                openGui(p, cmd, op);

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "AddWorldItem")), p))) {

                command.put(p.getUniqueId().toString(), cmd);
                lookingFor.put(p.getUniqueId().toString(), "world");
                lookingForOp.put(p.getUniqueId().toString(), op);
                for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "AddWorldMessage")) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%c", cmd);
                    mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders));
                }

                p.closeInventory();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            } else if (!(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "InventoryCommandName").replace("%c", cmd)), p))) {

                if (disabled.getStringList(configurationSectionName + "." + cmd + ".Worlds").indexOf("all") != -1) {
                    List<String> worlds = new ArrayList<>();
                    for (World w : Bukkit.getWorlds()) {
                        worlds.add(w.getName());
                    }
                    disabled.set(configurationSectionName + "." + cmd + ".Worlds", worlds);
                    if (op) {
                        mi.saveOpBlockConfig();
                    } else {
                        mi.saveDisabledCommandsConfig();
                    }
                }
                List<String> newWorldList = disabled.getStringList(configurationSectionName + "." + cmd + ".Worlds");
                if (disabled.getStringList(configurationSectionName + "." + cmd + ".Worlds").indexOf(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) != -1) {
                    newWorldList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    Log.addLog(Universal.get().getMethods(), p.getName() + ": Removed world " + e.getCurrentItem().getItemMeta().getDisplayName() + " from /" + cmd +
                            " in " + (op ? "opblock.yml" : "disabled.yml"));
                } else {
                    newWorldList.add(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    Log.addLog(Universal.get().getMethods(), p.getName() + ": Added world " + e.getCurrentItem().getItemMeta().getDisplayName() + " to /" + cmd +
                            " in " + (op ? "opblock.yml" : "disabled.yml"));
                }

                disabled.set(configurationSectionName + "." + cmd + ".Worlds", newWorldList);
                if (op) {
                    mi.saveOpBlockConfig();
                } else {
                    mi.saveDisabledCommandsConfig();
                }
                openWorldGui(mi, p, cmd, op);
            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, "EditGui", "EditPlayerCommandsInventoryName")), e.getWhoClicked())) ||
                e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                        Messages.getMessage(mi, "EditOpGui", "EditPlayerCommandsInventoryName")), e.getWhoClicked()))) {
            boolean op =
                    !e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                            Messages.getMessage(mi, "EditGui", "EditPlayerCommandsInventoryName")), e.getWhoClicked()));

            FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                    (FileConfiguration) mi.getDisabledCommandsConfig();
            if (!op && !e.getWhoClicked().hasPermission("cb.edit")) {
                return;
            } else if (op && !e.getWhoClicked().hasPermission("cb.editop")) {
                return;
            }
            String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
            String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

            if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
                e.setCancelled(true);
                return;
            }
            String cmd = ChatColor.stripColor(Objects.requireNonNull(Objects.requireNonNull(e.getInventory().getItem(4)).getItemMeta()).getDisplayName()).substring(1);
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "GoBackItemName")), p))) {
                openGui(p, cmd, op);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "AddPlayerCommandItem")), p))) {
                command.put(p.getUniqueId().toString(), cmd);
                lookingFor.put(p.getUniqueId().toString(), "playercommand");
                lookingForOp.put(p.getUniqueId().toString(), op);
                for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "AddPlayerCommandMessage")) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%c", cmd);
                    mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders, true));
                }
                p.closeInventory();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            } else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "InventoryCommandName").replace("%c", cmd)), p)))) {

                List<String> newPCommandList = disabled.getStringList(configurationSectionName + "." + cmd +
                        ".PlayerCommands");
                if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
                    newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
                    Log.addLog(Universal.get().getMethods(),
                            p.getName() + ": Removed player command " + e.getCurrentItem().getItemMeta().getDisplayName()
                                    + " from /" + cmd + " in " + (op ? "opblock.yml" : "disabled.yml"));
                } else {
                    newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    Log.addLog(Universal.get().getMethods(),
                            p.getName() + ": Removed none tag from player commands " + "for /" + cmd + " in " + (op ?
                                    "opblock.yml" : "disabled.yml"));
                }

                disabled.set(configurationSectionName + "." + cmd + ".PlayerCommands", newPCommandList);
                if (op) {
                    mi.saveOpBlockConfig();
                } else {
                    mi.saveDisabledCommandsConfig();
                }
                openPCommandsGui(mi, p, cmd, op);
            }
            e.setCancelled(true);
        } else if (e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, "EditGui", "EditConsoleCommandsInventoryName")), e.getWhoClicked())) ||
                e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                        Messages.getMessage(mi, "EditOpGui", "EditConsoleCommandsInventoryName")), e.getWhoClicked()))) {

            boolean op = !e.getView().getTitle().equals(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, "EditGui", "EditConsoleCommandsInventoryName")), e.getWhoClicked()));

            FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                    (FileConfiguration) mi.getDisabledCommandsConfig();
            if (!op && !e.getWhoClicked().hasPermission("cb.edit")) {
                return;
            } else if (op && !e.getWhoClicked().hasPermission("cb.editop")) {
                return;
            }
            String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
            String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

            if (e.getRawSlot() < 0 || e.getRawSlot() >= 54 || (!e.getCurrentItem().getItemMeta().hasDisplayName())) {
                e.setCancelled(true);
                return;
            }
            String cmd = ChatColor.stripColor(Objects.requireNonNull(Objects.requireNonNull(e.getInventory().getItem(4)).getItemMeta()).getDisplayName()).substring(1);
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "GoBackItemName")), p))) {
                openGui(p, cmd, op);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "AddConsoleCommandItem")), p))) {
                command.put(p.getUniqueId().toString(), cmd);
                lookingFor.put(p.getUniqueId().toString(), "consolecommand");
                lookingForOp.put(p.getUniqueId().toString(), op);
                for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "AddConsoleCommandMessage")) {
                    HashMap<String, String> placeholders = new HashMap<>();
                    placeholders.put("%c", cmd);
                    mi.sendMessage(p, Variables.translateVariables(msgToSend, p, placeholders, true));
                }
                p.closeInventory();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(" ")) {
                e.setCancelled(true);
            } else if (!(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                    Messages.getMessage(mi, editConfigurationSectionName, "InventoryCommandName").replace("%c", cmd)), p)))) {

                List<String> newPCommandList = disabled.getStringList(configurationSectionName + "." + cmd +
                        ".ConsoleCommands");
                if (!ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("none")) {
                    newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).substring(1));
                    Log.addLog(Universal.get().getMethods(),
                            p.getName() + ": Removed console command " + e.getCurrentItem().getItemMeta().getDisplayName() +
                                    " from /" + cmd + " in " + (op ? "opblock.yml" : "disabled.yml"));
                } else {
                    newPCommandList.remove(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    Log.addLog(Universal.get().getMethods(),
                            p.getName() + ": Removed none tag from console commands " + "for /" + cmd + " in " + (op ?
                                    "opblock.yml" : "disabled.yml"));
                }

                disabled.set(configurationSectionName + "." + cmd + ".ConsoleCommands", newPCommandList);
                if (op) {
                    mi.saveOpBlockConfig();
                } else {
                    mi.saveDisabledCommandsConfig();
                }
                openCCommandsGui(mi, p, cmd, op);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!lookingForOp.containsKey(p.getUniqueId().toString())) {
            return;
        }
        if ((!p.hasPermission("cb.edit") && !lookingForOp.get(p.getUniqueId().toString())) || (!p.hasPermission("cb" +
                ".editop") && lookingForOp.get(p.getUniqueId().toString()))) {
            return;
        }
        if (!lookingFor.containsKey(p.getUniqueId().toString())) {
            return;
        }

        MethodInterface mi = Universal.get().getMethods();

        boolean op = lookingForOp.get(p.getUniqueId().toString());

        FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";

        if (e.getMessage().replace(" ", "").equalsIgnoreCase("cancel")) {
            lookingFor.remove(p.getUniqueId().toString());
            lookingForOp.remove(p.getUniqueId().toString());
            command.remove(p.getUniqueId().toString());
            for (Component msgToSend : mi.getOldMessages("Main", "Cancelled", mi.getMessagesConfig())) {
                mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
            }
            e.setCancelled(true);
            return;
        }

        if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("permission")) {

            Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed permission from " +
                    disabled.getString(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".Permission") + " to "+ e.getMessage()	+ "for /" + command.get(p.getUniqueId().toString()) +
                    " in " + (op ? "opblock.yml" : "disabled.yml"));

            disabled.set(configurationSectionName + "." + command.get(p.getUniqueId().toString()) + ".Permission",
                    e.getMessage());

            if (op) {
                mi.saveOpBlockConfig();
            } else {
                mi.saveDisabledCommandsConfig();
            }
            lookingFor.remove(p.getUniqueId().toString());
            lookingForOp.remove(p.getUniqueId().toString());

            openGui(p, command.get(p.getUniqueId().toString()), op);
            command.remove(p.getUniqueId().toString());
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {

            Log.addLog(Universal.get().getMethods(), p.getName() + ": Changed message from " +
                    disabled.getString(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".Message") + " to "+ e.getMessage() + "for /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            disabled.set(configurationSectionName + "." + command.get(p.getUniqueId().toString()) + ".Message",
                    e.getMessage());

            if (op) {
                mi.saveOpBlockConfig();
            } else {
                mi.saveDisabledCommandsConfig();
            }
            lookingFor.remove(p.getUniqueId().toString());
            lookingForOp.remove(p.getUniqueId().toString());

            openGui(p, command.get(p.getUniqueId().toString()), op);
            command.remove(p.getUniqueId().toString());
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
            Log.addLog(Universal.get().getMethods(),
                    p.getName() + ": Added world " + e.getMessage() + " for /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            List<String> worldList =
                    disabled.getStringList(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".Worlds");
            worldList.add(e.getMessage());
            disabled.set(configurationSectionName + "." + command.get(p.getUniqueId().toString()) + ".Worlds",
                    worldList);

            if (op) {
                mi.saveOpBlockConfig();
            } else {
                mi.saveDisabledCommandsConfig();
            }
            lookingFor.remove(p.getUniqueId().toString());
            lookingForOp.remove(p.getUniqueId().toString());

            openWorldGui(mi, p, command.get(p.getUniqueId().toString()), op);
            command.remove(p.getUniqueId().toString());
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {
            Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command /" + e.getMessage() + " " +
                    "for /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            List<String> pCmdList =
                    disabled.getStringList(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".PlayerCommands");
            pCmdList.add(e.getMessage());
            addCmd(mi, p, disabled, pCmdList, op);
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {
            Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command /" + e.getMessage() + " " +
                    "for /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            List<String> cCmdList =
                    disabled.getStringList(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".ConsoleCommands");
            cCmdList.add(e.getMessage());
            addConsoleCmd(mi, p, disabled, cCmdList, op);
        }
        e.setCancelled(true);
    }

    private void addConsoleCmd(MethodInterface mi, Player p, FileConfiguration disabled, List<String> cCmdList,
                               boolean op) {
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        disabled.set(configurationSectionName + "." + command.get(p.getUniqueId().toString()) + ".ConsoleCommands",
                cCmdList);
        if (op) {
            mi.saveOpBlockConfig();
        } else {
            mi.saveDisabledCommandsConfig();
        }
        lookingFor.remove(p.getUniqueId().toString());
        lookingForOp.remove(p.getUniqueId().toString());
        openCCommandsGui(mi, p, command.get(p.getUniqueId().toString()), op);
        command.remove(p.getUniqueId().toString());
    }

    private void addCmd(MethodInterface mi, Player p, FileConfiguration disabled, List<String> pCmdList, boolean op) {
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        disabled.set(configurationSectionName + "." + command.get(p.getUniqueId().toString()) + ".PlayerCommands",
                pCmdList);
        if (op) {
            mi.saveOpBlockConfig();
        } else {
            mi.saveDisabledCommandsConfig();
        }
        lookingFor.remove(p.getUniqueId().toString());
        lookingForOp.remove(p.getUniqueId().toString());
        openPCommandsGui(mi, p, command.get(p.getUniqueId().toString()), op);
        command.remove(p.getUniqueId().toString());
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!lookingForOp.containsKey(p.getUniqueId().toString())) {
            return;
        }

        if ((!p.hasPermission("cb.edit") && !lookingForOp.get(p.getUniqueId().toString())) || (!p.hasPermission("cb" +
                ".editop") && lookingForOp.get(p.getUniqueId().toString()))) {
            return;
        }
        if (!lookingFor.containsKey(p.getUniqueId().toString())) {
            return;
        }
        MethodInterface mi = Universal.get().getMethods();
        boolean op = lookingForOp.get(p.getUniqueId().toString());

        FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

        if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("permission")) {
            for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "CannotUseCommand")) {
                mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
            }
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("message")) {
            for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "CannotUseCommand")) {
                mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
            }
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("world")) {
            for (Component msgToSend : mi.getOldMessages(editConfigurationSectionName, "CannotUseCommand")) {
                mi.sendMessage(p, Variables.translateVariables(msgToSend, p));
            }
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("playercommand")) {

            Log.addLog(Universal.get().getMethods(), p.getName() + ": Added player command " + e.getMessage() + " for" +
                    " /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            List<String> pCmdList =
                    disabled.getStringList(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".PlayerCommands");
            pCmdList.add(e.getMessage().substring(1));
            addCmd(mi, p, disabled, pCmdList, op);
        } else if (lookingFor.get(p.getUniqueId().toString()).equalsIgnoreCase("consolecommand")) {

            Log.addLog(Universal.get().getMethods(), p.getName() + ": Added console command " + e.getMessage() + " " +
                    "for /" + command.get(p.getUniqueId().toString()) + " in " + (op ? "opblock.yml" : "disabled.yml"));

            List<String> cCmdList =
                    disabled.getStringList(configurationSectionName + "." + command.get(p.getUniqueId().toString()) +
                            ".ConsoleCommands");
            cCmdList.add(e.getMessage().substring(1));
            addConsoleCmd(mi, p, disabled, cCmdList, op);
        }
        e.setCancelled(true);
    }

    @SuppressWarnings("deprecation")
    public void openWorldGui(MethodInterface mi, Player p, String args, boolean op) {
        if ((!p.hasPermission("cb.edit") && !op) || (!p.hasPermission("cb.editop") && op) ) {
            return;
        }
        FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

        Inventory inv = Bukkit.createInventory(null, 54, Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "EditWorldsInventoryName")), p));

        worldGui(mi, p, args, inv, op);

        if (disabled.getString(configurationSectionName + "." + args + ".Worlds") == null) {
            List<String> list = new ArrayList<>();
            for (World w : Bukkit.getWorlds()) {
                list.add(w.getName());
            }
            disabled.set(configurationSectionName + "." + args + ".Worlds", list);
            if (op) {
                mi.saveOpBlockConfig();
            } else {
                mi.saveDisabledCommandsConfig();
            }
        }

        int worldCount = 10;
        for (World w : Bukkit.getWorlds()) {
            ItemStack world;
            ItemMeta worldMeta;
            if ((disabled.getStringList(configurationSectionName + "." + args + ".Worlds").indexOf(w.getName()) != -1) ||
                    (disabled.getStringList(configurationSectionName + "." + args + ".Worlds").indexOf("all") != -1)) {
                String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                int limeWoolId = 0;
                if (limeWool.equalsIgnoreCase("wool")) {
                    limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                }

                world = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                worldMeta = world.getItemMeta();
                assert worldMeta != null;
                worldMeta.setDisplayName(ChatColor.GREEN + w.getName());
            } else {
                String redWool = new LegacyBlocks().getBlock("RED_WOOL");
                int redWoolId = 0;
                if (redWool.equalsIgnoreCase("wool")) {
                    redWoolId = new LegacyBlocks().getLegacyId("RED");
                }

                world = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
                worldMeta = world.getItemMeta();
                assert worldMeta != null;
                worldMeta.setDisplayName(ChatColor.RED + w.getName());
            }
            world.setItemMeta(worldMeta);
            if (worldCount == 17) worldCount += 2;
            if (worldCount == 26) worldCount += 2;
            if (worldCount == 35) worldCount += 2;
            if (worldCount == 43) break;
            inv.setItem(worldCount, world);
            worldCount++;

        }
        for (String s : disabled.getStringList(configurationSectionName + "." + args + ".Worlds")) {
            if (Bukkit.getWorld(s) == null) {
                if (!s.equalsIgnoreCase("all")) {
                    String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                    int limeWoolId = 0;
                    if (limeWool.equalsIgnoreCase("wool")) {
                        limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                    }

                    ItemStack world = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                    ItemMeta worldMeta = world.getItemMeta();
                    assert worldMeta != null;
                    worldMeta.setDisplayName(ChatColor.GREEN + s);
                    world.setItemMeta(worldMeta);
                    if (worldCount == 17) worldCount += 2;
                    if (worldCount == 26) worldCount += 2;
                    if (worldCount == 35) worldCount += 2;
                    if (worldCount == 43) break;
                    inv.setItem(worldCount, world);
                    worldCount++;
                }
            }
        }
        String magentaWool;
        if (BukkitMain.customSkulls()) {
            if (BukkitMain.newBlocks()) {
                magentaWool = "PLAYER_HEAD";
            } else {
                magentaWool = "SKULL_ITEM";
            }
        } else {
            magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
        }
        int magentaWoolId = 0;
        if (magentaWool.equalsIgnoreCase("wool")) {
            magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
        } else if (magentaWool.equalsIgnoreCase("skull_item")) {
            magentaWoolId = 3;
        }

        ItemStack world = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
        ItemMeta worldMeta = world.getItemMeta();
        assert worldMeta != null;
        worldMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "AddWorldItem")), p));
        world.setItemMeta(worldMeta);
        if (magentaWool.equalsIgnoreCase("SKULL_ITEM") || magentaWool.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) world.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String playerCommandValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3MTViZWNjNGI3ZWIxNzY4ZDRhMDM3NmY3NzU5ZmZiY2M1NzkwOWY4ZmQ0MTRkMDVhNDA5NGI2YjNhYmYwIn19fQ==";
            gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            world.setItemMeta(meta);
        }
        setSlots(p, inv, worldCount, world);

    }

    @SuppressWarnings("deprecation")
    private void worldGui(MethodInterface mi, Player p, String args, Inventory inv, boolean op) {
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";
        String pinkGlassBlock = new LegacyBlocks().getBlock("GLASS");
        int pinkGlassBlockId = 0;
        if (BukkitMain.coloredGlassPane()) {
            pinkGlassBlock = new LegacyBlocks().getBlock("PINK_GLASS");
            if (pinkGlassBlock.equalsIgnoreCase("stained_glass")) {
                pinkGlassBlockId = new LegacyBlocks().getLegacyId("PINK");
            }
        }

        ItemStack commandSlot = new ItemStack(Material.valueOf(pinkGlassBlock), 1, (byte) pinkGlassBlockId);

        if (BukkitMain.coloredGlassPane()) {
            String magentaGlass = new LegacyBlocks().getBlock("MAGENTA_GLASS_PANE");
            String pinkGlass = new LegacyBlocks().getBlock("PINK_GLASS_PANE");

            int magentaId = 0;
            int pinkId = 0;
            if (magentaGlass.equalsIgnoreCase("stained_glass_pane")) {
                magentaId = new LegacyBlocks().getLegacyId("MAGENTA");
            }
            if (pinkGlass.equalsIgnoreCase("stained_glass_pane")) {
                pinkId = new LegacyBlocks().getLegacyId("PINK");
            }

            ItemStack magentaGlassSlots = new ItemStack(Material.valueOf(magentaGlass), 1, (byte) magentaId);
            ItemStack pinkGlassSlots = new ItemStack(Material.valueOf(pinkGlass), 1, (byte) pinkId);

            magentaPinkItemSlots(magentaGlassSlots, pinkGlassSlots);

            setSlot(inv, magentaGlassSlots, pinkGlassSlots);
        }

        String pinkDye = new LegacyBlocks().getBlock("PINK_DYE");

        int pinkId = 0;
        if (pinkDye.equalsIgnoreCase("ink_sack")) {
            pinkId = 9;
        }
        ItemStack backSlot = new ItemStack(Material.valueOf(pinkDye), 1, (byte) pinkId);
        ItemMeta backSlotMeta = backSlot.getItemMeta();
        assert backSlotMeta != null;
        backSlotMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "GoBackItemName")), p));
        backSlot.setItemMeta(backSlotMeta);
        inv.setItem(49, backSlot);

        commandSlotItem(mi, p, args, inv, commandSlot, op);
    }

    static void setSlot(Inventory inv, ItemStack magentaGlassSlots, ItemStack pinkGlassSlots) {
        for (int i = 0; i <= 8; i += 2) {
            inv.setItem(i, pinkGlassSlots);
            inv.setItem(i + 44, pinkGlassSlots);
        }
        for (int i = 1; i <= 9; i += 2) {
            inv.setItem(i, magentaGlassSlots);
            inv.setItem(i + 44, magentaGlassSlots);
        }
        inv.setItem(17, magentaGlassSlots);
        inv.setItem(18, pinkGlassSlots);
        inv.setItem(26, pinkGlassSlots);
        inv.setItem(27, magentaGlassSlots);
        inv.setItem(35, magentaGlassSlots);
        inv.setItem(36, pinkGlassSlots);
    }

    @SuppressWarnings("deprecation")
    public void openPCommandsGui(MethodInterface mi, Player p, String args, boolean op) {
        if ((!p.hasPermission("cb.edit") && !op) || (!p.hasPermission("cb.editop") && op) ) {
            return;
        }
        FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

        Inventory inv = Bukkit.createInventory(null, 54, Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "EditPlayerCommandsInventoryName")), p));
        worldGui(mi, p, args, inv, op);

        int cmdCount = 10;
        if (!disabled.getStringList(configurationSectionName + "." + args + ".PlayerCommands").contains("none")) {
            for (String s : disabled.getStringList(configurationSectionName + "." + args + ".PlayerCommands")) {
                String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                int limeWoolId = 0;
                if (limeWool.equalsIgnoreCase("wool")) {
                    limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                }

                ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                ItemMeta cmdMeta = cmd.getItemMeta();
                assert cmdMeta != null;
                cmdMeta.setDisplayName("/" + s);
                cmd.setItemMeta(cmdMeta);
                if (cmdCount == 17) cmdCount += 2;
                if (cmdCount == 26) cmdCount += 2;
                if (cmdCount == 35) cmdCount += 2;
                if (cmdCount == 43) break;
                inv.setItem(cmdCount, cmd);
                cmdCount++;
            }
        } else {
            for (String s : disabled.getStringList(configurationSectionName + "." + args + ".PlayerCommands")) {
                ItemStack cmd;
                ItemMeta cmdMeta;
                if (!s.equalsIgnoreCase("none")) {
                    String redWool = new LegacyBlocks().getBlock("RED_WOOL");
                    int redWoolId = 0;
                    if (redWool.equalsIgnoreCase("wool")) {
                        redWoolId = new LegacyBlocks().getLegacyId("RED");
                    }

                    cmd = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
                    cmdMeta = cmd.getItemMeta();
                    assert cmdMeta != null;
                    cmdMeta.setDisplayName("/" + s);
                } else {
                    String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                    int limeWoolId = 0;
                    if (limeWool.equalsIgnoreCase("wool")) {
                        limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                    }

                    cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                    cmdMeta = cmd.getItemMeta();
                    assert cmdMeta != null;
                    cmdMeta.setDisplayName("none");
                }
                cmd.setItemMeta(cmdMeta);
                if (cmdCount == 17) cmdCount += 2;
                if (cmdCount == 26) cmdCount += 2;
                if (cmdCount == 35) cmdCount += 2;
                if (cmdCount == 43) break;
                inv.setItem(cmdCount, cmd);
                cmdCount++;
            }
        }
        String magentaWool;
        if (BukkitMain.customSkulls()) {
            if (BukkitMain.newBlocks()) {
                magentaWool = "PLAYER_HEAD";
            } else {
                magentaWool = "SKULL_ITEM";
            }
        } else {
            magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
        }
        int magentaWoolId = 0;
        if (magentaWool.equalsIgnoreCase("wool")) {
            magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
        } else if (magentaWool.equalsIgnoreCase("skull_item")) {
            magentaWoolId = 3;
        }

        ItemStack cmd = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
        ItemMeta cmdMeta = cmd.getItemMeta();
        assert cmdMeta != null;
        cmdMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "AddPlayerCommandItem")), p));
        cmd.setItemMeta(cmdMeta);
        if (magentaWool.equalsIgnoreCase("SKULL_ITEM") || magentaWool.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) cmd.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String playerCommandValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3MTViZWNjNGI3ZWIxNzY4ZDRhMDM3NmY3NzU5ZmZiY2M1NzkwOWY4ZmQ0MTRkMDVhNDA5NGI2YjNhYmYwIn19fQ==";
            gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            cmd.setItemMeta(meta);
        }
        setSlots(p, inv, cmdCount, cmd);

    }

    static void setSlots(Player p, Inventory inv, int cmdCount, ItemStack cmd) {
        if (cmdCount == 17) cmdCount += 2;
        if (cmdCount == 26) cmdCount += 2;
        if (cmdCount == 35) cmdCount += 2;
        if (cmdCount == 44) cmdCount -= 1;
        inv.setItem(cmdCount, cmd);

        Bukkit.getScheduler().runTaskLater(BukkitMain.get(), () -> p.openInventory(inv), 1);
    }

    @SuppressWarnings("deprecation")
    public void openCCommandsGui(MethodInterface mi, Player p, String args, boolean op) {
        if ((!p.hasPermission("cb.edit") && !op) || (!p.hasPermission("cb.editop") && op) ) {
            return;
        }
        FileConfiguration disabled = op ? (FileConfiguration) mi.getOpBlockConfig() :
                (FileConfiguration) mi.getDisabledCommandsConfig();
        String configurationSectionName = op ? "DisabledOpCommands" : "DisabledCommands";
        String editConfigurationSectionName = op ? "EditOpGui" : "EditGui";

        Inventory inv = Bukkit.createInventory(null, 54, Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "EditConsoleCommandsInventoryName")), p));
        worldGui(mi, p, args, inv, op);

        int cmdCount = 10;
        if (!disabled.getStringList(configurationSectionName + "." + args + ".ConsoleCommands").contains("none")) {
            for (String s : disabled.getStringList(configurationSectionName + "." + args + ".ConsoleCommands")) {
                String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                int limeWoolId = 0;
                if (limeWool.equalsIgnoreCase("wool")) {
                    limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                }

                ItemStack cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                ItemMeta cmdMeta = cmd.getItemMeta();
                assert cmdMeta != null;
                cmdMeta.setDisplayName("/" + s);
                cmd.setItemMeta(cmdMeta);
                if (cmdCount == 17) cmdCount += 2;
                if (cmdCount == 26) cmdCount += 2;
                if (cmdCount == 35) cmdCount += 2;
                if (cmdCount == 43) break;
                inv.setItem(cmdCount, cmd);
                cmdCount++;
            }
        } else {
            for (String s : disabled.getStringList(configurationSectionName + "." + args + ".ConsoleCommands")) {
                ItemStack cmd;
                ItemMeta cmdMeta;
                if (!s.equalsIgnoreCase("none")) {
                    String redWool = new LegacyBlocks().getBlock("RED_WOOL");
                    int redWoolId = 0;
                    if (redWool.equalsIgnoreCase("wool")) {
                        redWoolId = new LegacyBlocks().getLegacyId("RED");
                    }

                    cmd = new ItemStack(Material.valueOf(redWool), 1, (byte) redWoolId);
                    cmdMeta = cmd.getItemMeta();
                    assert cmdMeta != null;
                    cmdMeta.setDisplayName("/" + s);
                } else {
                    String limeWool = new LegacyBlocks().getBlock("LIME_WOOL");
                    int limeWoolId = 0;
                    if (limeWool.equalsIgnoreCase("wool")) {
                        limeWoolId = new LegacyBlocks().getLegacyId("LIME");
                    }

                    cmd = new ItemStack(Material.valueOf(limeWool), 1, (byte) limeWoolId);
                    cmdMeta = cmd.getItemMeta();
                    assert cmdMeta != null;
                    cmdMeta.setDisplayName("none");
                }
                cmd.setItemMeta(cmdMeta);
                if (cmdCount == 17) cmdCount += 2;
                if (cmdCount == 26) cmdCount += 2;
                if (cmdCount == 35) cmdCount += 2;
                if (cmdCount == 43) break;
                inv.setItem(cmdCount, cmd);
                cmdCount++;
            }
        }
        String magentaWool;
        if (BukkitMain.customSkulls()) {
            if (BukkitMain.newBlocks()) {
                magentaWool = "PLAYER_HEAD";
            } else {
                magentaWool = "SKULL_ITEM";
            }
        } else {
            magentaWool = new LegacyBlocks().getBlock("MAGENTA_WOOL");
        }
        int magentaWoolId = 0;
        if (magentaWool.equalsIgnoreCase("wool")) {
            magentaWoolId = new LegacyBlocks().getLegacyId("MAGENTA");
        } else if (magentaWool.equalsIgnoreCase("skull_item")) {
            magentaWoolId = 3;
        }

        ItemStack cmd = new ItemStack(Material.valueOf(magentaWool), 1, (byte) magentaWoolId);
        ItemMeta cmdMeta = cmd.getItemMeta();
        assert cmdMeta != null;
        cmdMeta.setDisplayName(Variables.translateVariablesToString(ChatColor.translateAlternateColorCodes('&',
                Messages.getMessage(mi, editConfigurationSectionName, "AddConsoleCommandItem")), p));
        cmd.setItemMeta(cmdMeta);
        if (magentaWool.equalsIgnoreCase("SKULL_ITEM") || magentaWool.equalsIgnoreCase("PLAYER_HEAD")) {
            SkullMeta meta = (SkullMeta) cmd.getItemMeta();
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
            String playerCommandValue =
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE3MTViZWNjNGI3ZWIxNzY4ZDRhMDM3NmY3NzU5ZmZiY2M1NzkwOWY4ZmQ0MTRkMDVhNDA5NGI2YjNhYmYwIn19fQ==";
            gameProfile.getProperties().put("textures", new Property("textures", playerCommandValue));
            Field profileField;
            try {
                profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(meta, gameProfile);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            cmd.setItemMeta(meta);
        }
        setSlots(p, inv, cmdCount, cmd);

    }

}
