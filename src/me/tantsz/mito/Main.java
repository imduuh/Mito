package me.tantsz.mito;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.tantsz.mito.commands.Commands;
import me.tantsz.mito.dbmanager.MySQL;
import me.tantsz.mito.dbmanager.SQLite;
import me.tantsz.mito.listeners.Listeners;

public class Main extends JavaPlugin {
	public static Main main;
	private SQLite sqlite;
	private MySQL mysql;
	
	public MySQL getMysql() {
		return mysql;
		}
	
	public SQLite getsql() {
		return sqlite;
		}
	
	public static Main getMain() {
		return main;
		}
	
	public static String getConfigString(String msg) {
		return main.getConfig().getString(msg).replace("&", "§");
		}
	
	@Override
	public void onEnable() {
		main = this;
		getCommand("setmito").setExecutor(new Commands());
		getCommand("mito").setExecutor(new Commands());
		if (!(new File(getDataFolder(), "config.yml")).exists()) {
			saveDefaultConfig();
		}
		Bukkit.getPluginManager().registerEvents((Listener)new Listeners(), (Plugin)this);
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§eMITO: §aPlugin habilitado!");
		Bukkit.getConsoleSender().sendMessage("§eAuthor: §fTantsZ");
		Bukkit.getConsoleSender().sendMessage("§eVersao: §f1.0");
		Bukkit.getConsoleSender().sendMessage("");
		File database;
		if (!(database = new File(getDataFolder() + File.separator + "database.db")).exists()) {
			try {
				database.createNewFile();
				}
			catch (IOException e) {
				e.printStackTrace();
				} 
			}
		if (getConfig().getBoolean("MySQL.Ativado")) {
			mysql = new MySQL(getConfig().getString("MySQL.Usuario"), getConfig().getString("MySQL.Senha"), getConfig().getString("MySQL.Database"), getConfig().getString("MySQL.Host"));
			} else {
				sqlite = new SQLite();
			} 
		}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage("§eMITO: §4Plugin desabilitado!");
		Bukkit.getConsoleSender().sendMessage("§eAuthor: §fTantsZ");
		Bukkit.getConsoleSender().sendMessage("§eVersao: §f1.0");
		Bukkit.getConsoleSender().sendMessage("");
	}
	
}