package dev.tomat.fable.tweaks.fabric;

import dev.tomat.fable.api.entrypoint.TweakInitializer;
import dev.tomat.fable.tweaks.fabric.patches.mods.armoredelytra.ArmoredElytraMixinPriorityPatch;
import dev.tomat.fable.tweaks.fabric.patches.mods.betterloadingscreen.BetterLoadingScreenMixinFixPatch;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.impl.entrypoint.GameTransformer;

public class FableFabricTweakInitializer implements TweakInitializer {
	@Override
	public void onInitialize(GameTransformer transformer) {
		if (QuiltLoader.isModLoaded("better_loading_screen")) {
			transformer.addPatch(new BetterLoadingScreenMixinFixPatch());
		}

		// Newer versions of the mod (which don't need this patch) go by "armored_elytra", by the way.
		if (QuiltLoader.isModLoaded("armoredelytra") || QuiltLoader.isModLoaded("armored-elytra")) {
			transformer.addPatch(new ArmoredElytraMixinPriorityPatch());
		}
	}
}
