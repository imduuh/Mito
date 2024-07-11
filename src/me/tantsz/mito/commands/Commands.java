package me.tantsz.mito.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.tantsz.mito.Main;
import me.tantsz.mito.listeners.Listeners;

public class Commands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mito")) {
			if (args.length == 0 || args.length == 1) {
				if (args.length == 0) {
					if (Main.getMain().getConfig().getString("MitoAtual") == null) {
						sender.sendMessage(Main.getConfigString("Mensagens.SemMito"));
						return true;
					}
					int quantidade;
					if (Main.getMain().getConfig().getBoolean("MySQL.Ativado")) {
						quantidade = Main.getMain().getMysql().getKills(sender.getName());
					} else {
						quantidade = Main.getMain().getsql().getKills(sender.getName());
					}
					List<String> list = Main.getMain().getConfig().getStringList("Mensagens.MitoAtual");
					for (String str: list) {
						sender.sendMessage(str.replace("&","§").replace("{mito}", Main.getMain().getConfig().getString("MitoAtual")).replace("{quantidade}", Integer.toString(quantidade)));
					}
				} else {
					if (args[0].equalsIgnoreCase("top")) {
						if (Main.getMain().getConfig().getBoolean("MySQL.Ativado")) {
							Main.getMain().getMysql().getTopKills((Player)sender);
						} else {
							Main.getMain().getsql().getTopKills((Player)sender);
						}
					}
				}	
			}
		}

		if (cmd.getName().equalsIgnoreCase("setmito")) {
			if (sender.hasPermission("mito.setmito")) {
				if (args.length == 1) {
					if (Bukkit.getPlayer(args[0]) == null) {
						sender.sendMessage(Main.getConfigString("Mensagens.JogadorOffline").replace("{player}",args[0]));
						return true;
					}
					if (!Main.getMain().getConfig().getString("MitoAtual").equals(args[0])) {
						Listeners.setMito(Bukkit.getPlayer(args[0]));
						sender.sendMessage(Main.getConfigString("Mensagens.MitoSetado").replace("{player}",args[0]));
					} else {
						sender.sendMessage(Main.getConfigString("Mensagens.MitoIgual").replace("{player}", args[0]));
						return true;
					}
				} else {
					sender.sendMessage(Main.getConfigString("Mensagens.UsoIncorreto"));
					return true;
				}
			} else {
				sender.sendMessage(Main.getConfigString("Mensagens.SemPermissao"));
				return true;
			}
			
		}
		return false;
	}
}			