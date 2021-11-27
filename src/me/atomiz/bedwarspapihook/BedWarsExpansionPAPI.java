package me.atomiz.bedwarspapihook;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.CachedArena;
import com.andrei1058.bedwars.proxy.arenamanager.ArenaManager;
import com.andrei1058.bedwars.proxy.language.Language;
import com.andrei1058.bedwars.proxy.language.LanguageManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BedWarsExpansionPAPI extends PlaceholderExpansion {

    BedWarsProxy bwp;

    @NotNull
    @Override
    public String getAuthor() {
        return "Atomiz";
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "bedwars";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getRequiredPlugin() {
        return "BedWarsProxy";
    }

    @Override
    public boolean canRegister() {
        return (bwp = (BedWarsProxy) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }

    @Override
    public boolean register() {
        bwp = (BedWarsProxy) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());
        if (bwp != null) {
            return super.register();
        }
        return false;
    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params) {

        // %bedwars_<arena>_players_current%
        // %bedwars_<arena>_players_max%
        // %bedwars_<arena>_status%
        // %bedwars_<arena>_mode%
        // %bedwars_<arena>_name%

        Language lang = (Language) LanguageManager.get().getPlayerLanguage(p.getPlayer());
        try {
            String[] placeholder = params.split("_");
            CachedArena arena = null;

            for (CachedArena cachedArena : ArenaManager.getArenas()) {
                if (cachedArena.getArenaName().equals(placeholder[0])) {
                    arena = cachedArena;
                    break;
                }
            }

            if (arena == null)
                return "&4Invalid Arena";

            switch (placeholder[1]) {
                case "players":
                    if (placeholder[2].equals("current"))
                        return arena.getCurrentPlayers() + "";
                    else if (placeholder[2].equals("max"))
                        return arena.getMaxPlayers() + "";

                case "status":
                    return arena.getDisplayStatus(lang);

                case "mode":
                    return arena.getDisplayGroup(lang);

                case "name":
                    return arena.getDisplayName(lang);
            }
        } catch (Exception ignored) {
        }
        return params;
    }
}