package de.noforcepack;

import de.noforcepack.config.NoForcePackConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;

import java.util.ArrayList;
import java.util.List;

public class ServerPackRemover {

    public static boolean removeServerPack(MinecraftClient client) {
        ResourcePackManager manager = client.getResourcePackManager();
        NoForcePackConfig config = NoForcePackConfig.get();

        List<String> enabledPacks = new ArrayList<>(manager.getEnabledNames());
        boolean found = false;

        for (String packId : new ArrayList<>(enabledPacks)) {
            boolean isServerPack = packId.startsWith("server/") || packId.startsWith("serverresourcepack");

            // Wenn onlyServerPacks=false → alle Packs entfernen außer "vanilla" und "fabric"
            boolean shouldRemove = config.onlyServerPacks
                    ? isServerPack
                    : (!packId.equals("vanilla") && !packId.startsWith("fabric"));

            if (shouldRemove) {
                enabledPacks.remove(packId);
                found = true;
                NoForcePack.LOGGER.info("Ressourcenpaket entfernt: {}", packId);
            }
        }

        if (found) {
            client.getResourcePackManager().setEnabledProfiles(enabledPacks);
            client.reloadResources();
        }

        return found;
    }
}
