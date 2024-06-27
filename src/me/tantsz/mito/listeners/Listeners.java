package me.tantsz.mito.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.tantsz.mito.Main;
import me.tantsz.mito.utils.RemoveBatsRunnable;
import java.util.HashSet;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener {
	
	public static void setMito(Player player) {
		Main.getMain().getConfig().set("MitoAntigo", Main.getMain().getConfig().getString("MitoAtual"));
		Main.getMain().getConfig().set("MitoAtual", player.getName());
		Main.getMain().saveConfig();
		
		List<String> list2 = Main.getMain().getConfig().getStringList("Comandos.NovoMito");
		for (String str2: list2) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str2.replace("&","§").replace("{mito}", Main.getMain().getConfig().getString("MitoAntigo")).replace("{novomito}", Main.getMain().getConfig().getString("MitoAtual")));
		}
		
		List<String> list3 = Main.getMain().getConfig().getStringList("Comandos.AntigoMito");
		for (String str3: list3) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str3.replace("&","§").replace("{mito}", Main.getMain().getConfig().getString("MitoAntigo")).replace("{novomito}", Main.getMain().getConfig().getString("MitoAtual")));
		}
		
		HashSet<Bat> spawnedBats = new HashSet<>();
		if (Main.getMain().getConfig().getBoolean("Raios.Ativar")) {
			for (int raio = 0; raio < Main.getMain().getConfig().getInt("Raios.Quantidade"); raio++) {
				player.getWorld().strikeLightningEffect(player.getLocation());
			}
		}
		if (Main.getMain().getConfig().getBoolean("Morcegos.Ativar")) {
			for (int bats = 0; bats < Main.getMain().getConfig().getInt("Morcegos.Quantidade"); bats++) {
				Bat bat = player.getWorld().spawn(player.getLocation().add(0.0D, 0.5D, 0.0D), Bat.class);
				bat.setCustomNameVisible(true);
				bat.setCustomName(Main.getConfigString("Morcegos.Nome"));
			    spawnedBats.add(bat);
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.getMain(), (Runnable)new RemoveBatsRunnable(spawnedBats), (Main.getMain().getConfig().getInt("Morcegos.Tempo") * 20)); 
		}
		if (Main.getMain().getConfig().getBoolean("MySQL.Ativado")) {
			Main.getMain().getMysql().addKill(player.getName());
		} else {
			Main.getMain().getsql().addKill(player.getName());
		}
		Bukkit.broadcastMessage(Main.getConfigString("Mensagens.NovoMitoSetado").replace("{player}", player.getName()));
	}
	
	@EventHandler
	public void onChatMessageEvent(ChatMessageEvent e) {
		Player p = e.getSender();
		if (Main.getMain().getConfig().contains("MitoAtual") && e.getTags().contains("mito") && p.getName().equalsIgnoreCase(Main.getConfigString("MitoAtual"))) {
			e.setTagValue("mito", Main.getConfigString("Tag"));
			}
		}
	
	@EventHandler
	private void onDeath(PlayerDeathEvent e) {
		Player antigo = e.getEntity();
		Player novo = e.getEntity().getKiller();
		if (antigo instanceof Player && novo instanceof Player && antigo.getName().equals(Main.getMain().getConfig().getString("MitoAtual"))) {
			setMito(novo);
			int quantidade;
			if (Main.getMain().getConfig().getBoolean("MySQL.Ativado")) {
				quantidade = Main.getMain().getMysql().getKills(novo.getName());
			} else {
				quantidade = Main.getMain().getsql().getKills(novo.getName());
			}
			List<String> list = Main.getMain().getConfig().getStringList("Mensagens.NovoMito");
			for (String str: list) {
				Bukkit.broadcastMessage(str.replace("&","§").replace("{mito}", antigo.getName()).replace("{novomito}", novo.getName()).replace("{quantidade}", Integer.toString(quantidade)));
			}
		}
	}

}