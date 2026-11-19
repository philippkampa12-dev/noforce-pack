package de.noforcepack.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.nio.file.*;

public class NoForcePackConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("noforcepack.json");

    // ── Einstellungen ──────────────────────────────────────────────
    /** Server-Packs automatisch beim Joinen entfernen */
    public boolean autoRemoveOnJoin = false;

    /** Nur Server-Packs entfernen (true) oder alle Packs entsperren (false) */
    public boolean onlyServerPacks = true;

    /** Bestätigungsnachricht in der Action-Bar anzeigen */
    public boolean showActionBarMessage = true;

    /** Nachricht auch im Chat anzeigen */
    public boolean showChatMessage = false;

    /** Tastenkürzel-Key (GLFW Key-Code, Standard: 82 = R) */
    public int removeKeyCode = 82;
    // ───────────────────────────────────────────────────────────────

    private static NoForcePackConfig instance;

    public static NoForcePackConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static NoForcePackConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                NoForcePackConfig cfg = GSON.fromJson(reader, NoForcePackConfig.class);
                if (cfg != null) {
                    return cfg;
                }
            } catch (IOException e) {
                System.err.println("[NoForcePack] Fehler beim Laden der Config: " + e.getMessage());
            }
        }
        // Standard-Config erstellen
        NoForcePackConfig defaults = new NoForcePackConfig();
        defaults.save();
        return defaults;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            System.err.println("[NoForcePack] Fehler beim Speichern der Config: " + e.getMessage());
        }
    }
}
