package me.treyruffy.commandblocker.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

public class CommandBlockerTabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("cb")) {
			ArrayList<String> tabCompletion = new ArrayList<>();
			List<String> tabList = Lists.newArrayList();
			if (sender.hasPermission("cb.add")) {
				tabCompletion.add("add");
			}
			if (sender.hasPermission("cb.remove")) {
				tabCompletion.add("remove");
			}
			if (sender.hasPermission("cb.edit")) {
				tabCompletion.add("edit");
			}
			if (sender.hasPermission("cb.addop")) {
				tabCompletion.add("addOP");
			}
			if (sender.hasPermission("cb.removeop")) {
				tabCompletion.add("removeOP");
			}
			if (sender.hasPermission("cb.editop")) {
				tabCompletion.add("editOP");
			}
			if (sender.hasPermission("cb.reload")) {
				tabCompletion.add("reload");
			}
			if (args.length == 1) {
				for (String list : tabCompletion) {
					if (list.toLowerCase().startsWith(args[0].toLowerCase())) {
						tabList.add(list);
					}
				}
			}
			return tabList;
		}
		return null;
	}

}
