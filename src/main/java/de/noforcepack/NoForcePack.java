package de.noforcepack;

import de.noforcepack.config.NoForcePackConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoForcePack implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("noforcepack");
    public static KeyBinding removePackKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("NoForcePack geladen.");

        NoForcePackConfig config = NoForcePackConfig.get();

        // Tastenbelegung registrieren
        removePackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.noforcepack.remove",
                InputUtil.Type.KEYSYM,
                config.removeKeyCode,
                "category.noforcepack.general"
        ));

        // Tastendruck-Handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (removePackKey.wasPressed()) {
                handleRemovePack(client);
            }
        });

        // Auto-Remove beim Server-Join (wenn aktiviert)
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (NoForcePackConfig.get().autoRemoveOnJoin) {
                LOGGER.info("Auto-Remove aktiv: entferne Server-Ressourcenpaket...");
                // Kurz verzögert, damit der Server das Pack erst senden kann
                new Thread(() -> {
                    try { Thread.sleep(3000); } catch (InterruptedException ignored) {}
                    client.execute(() -> handleRemovePack(client));
                }).start();
            }
        });
    }

    private void handleRemovePack(MinecraftClient client) {
        if (client.player == null) return;
        NoForcePackConfig config = NoForcePackConfig.get();

        boolean removed = ServerPackRemover.removeServerPack(client);

        if (removed) {
            if (config.showActionBarMessage) {
                client.player.sendMessage(
                        Text.translatable("message.noforcepack.removed"), true);
            }
            if (config.showChatMessage) {
                client.player.sendMessage(
                        Text.translatable("message.noforcepack.removed"), false);
            }
        } else {
            if (config.showActionBarMessage) {
                client.player.sendMessage(
                        Text.translatable("message.noforcepack.notfound"), true);
            }
        }
    }
}
