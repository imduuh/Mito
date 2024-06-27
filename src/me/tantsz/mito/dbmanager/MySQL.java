package me.tantsz.mito.dbmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.entity.Player;
import me.tantsz.mito.Main;

public class MySQL {
	private Connection connection;
	private Statement stmt;
	
	public MySQL(String user, String password, String database, String host) {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
			(this.stmt = this.connection.createStatement()).execute("CREATE TABLE IF NOT EXISTS mito (kills INTEGER, player VARCHAR(255))");
			}
		catch (SQLException e) {
			e.printStackTrace();
			} 
		}
	
	public void addNew(String player, int kills) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "INSERT INTO mito (player, kills) VALUES ('" + player + "', '" + kills + "');";
			this.stmt.executeUpdate(sql);
			}
		catch (Exception e) {
			e.printStackTrace();
			} 
		}
	
	public int getKills(String player) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "SELECT kills FROM mito WHERE player='" + player + "';";
			ResultSet rs = this.stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("kills");
				}
			}
		catch (Exception e) {
			e.printStackTrace();
			} 
		return 0;
		}
	
	public void updateKills(String player, int kills) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "UPDATE mito SET kills='" + (getKills(player) + kills) + "' WHERE player='" + player + "';";
			this.stmt.executeUpdate(sql);
			}
		catch (Exception e) {
			e.printStackTrace();
			}
		}
	
	public boolean hasPlayer(String player) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "SELECT * FROM mito WHERE player='" + player + "'";
			ResultSet rs = this.stmt.executeQuery(sql);
			return (rs.next() && rs.getString("player").equalsIgnoreCase(player));
			}
		catch (Exception e) {
			e.printStackTrace();
			return false;
			} 
		}
	
	public void addKill(String player) {
		if (hasPlayer(player)) {
			updateKills(player, 1);
			} else {
				addNew(player, 1);
			} 
		}
	
	public void getTopKills(Player p) {
		try {
			p.sendMessage(Main.getConfigString("Top.Cabecalho"));
			Class.forName("com.mysql.jdbc.Driver");
			String sql = "SELECT * FROM mito ORDER BY kills DESC LIMIT " + Main.getMain().getConfig().getInt("Top.Quantidade");
			ResultSet rs = this.stmt.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				p.sendMessage(Main.getConfigString("Top.Linhas").replace("{posicao}",String.valueOf(++i)).replace("{player}", rs.getString("player")).replace("{vezes}",String.valueOf(rs.getInt("kills"))));
				}
			}
		catch (Exception e) {
			e.printStackTrace();
			} 
		}

}
