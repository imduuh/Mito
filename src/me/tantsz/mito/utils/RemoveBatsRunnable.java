package me.tantsz.mito.utils;

import java.util.HashSet;
import org.bukkit.entity.Bat;

public class RemoveBatsRunnable implements Runnable {
	private HashSet<Bat> spawnedBats;
	
	public RemoveBatsRunnable(HashSet<Bat> spawnedBats) {
		this.spawnedBats = spawnedBats;
		}
	
	public void run() {
		for (Bat bat : this.spawnedBats)
			bat.remove(); 
		this.spawnedBats.clear();
		}
	}