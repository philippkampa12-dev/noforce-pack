package de.noforcepack.mixin;

import de.noforcepack.NoForcePack;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.resource.ClientResourcePackContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin auf PackListWidget.ResourcePackEntry:
 * Entfernt das "Locked/Forced"-Flag für Server-Ressourcenpakete,
 * sodass der Entfernen-Button im Ressourcenpaket-HUD sichtbar und klickbar wird.
 */
@Mixin(targets = "net.minecraft.client.gui.screen.pack.PackListWidget$ResourcePackEntry")
public class ServerResourcePackMixin {

    /**
     * Überschreibt die Methode, die prüft ob ein Pack "festgehalten" (pinned/required) ist.
     * Für Server-Packs wird false zurückgegeben, damit der Entfernen-Button erscheint.
     */
    @Inject(method = "isRequired", at = @At("HEAD"), cancellable = true)
    private void noforcepack_allowRemove(CallbackInfoReturnable<Boolean> cir) {
        // Hole die Pack-ID über den Accessor (wird unten per Accessor-Interface eingebunden)
        // Die eigentliche Logik: immer false zurückgeben = nie "required"
        // Das ermöglicht den Entfernen-Button für ALLE Packs inkl. Server-Packs
        cir.setReturnValue(false);
    }
}
