package dev.tomat.modmenu_patches;

import dev.tomat.fable.api.patching.PatchHandler;
import dev.tomat.modmenu_patches.patches.FixParentEntriesNotBeingExpandablePatch;
import dev.tomat.modmenu_patches.patches.MakeMinecraftAChildOfBtaPatch;
import dev.tomat.modmenu_patches.patches.TreatBtaAsMinecraftPatch;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint;

public final class ModMenuPatches implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch(ModContainer mod) {
		if (!QuiltLoader.isModLoaded("modmenu")) {
			return;
		}

		PatchHandler patcher = QuiltLoader.getPatchHandler();
		patcher.registerPatch(mod, new TreatBtaAsMinecraftPatch());
		patcher.registerPatch(mod, new MakeMinecraftAChildOfBtaPatch());
		patcher.registerPatch(mod, new FixParentEntriesNotBeingExpandablePatch());
	}
}
