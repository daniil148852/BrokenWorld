package dev.daniil.oasisclient.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Reserved for future hooks into GameRenderer if needed.
 * Currently the mod uses HudRenderCallback instead.
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // No injections needed currently — overlay is rendered via HudRenderCallback
}
