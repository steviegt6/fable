package dev.tomat.modmenu_patches;

import dev.tomat.modmenu_patches.patches.ModMenuPatch;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.entrypoint.PreLaunchEntrypoint;

public final class ModMenuPatches implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch(ModContainer mod) {
		QuiltLoader.getPatchHandler().registerPatch(mod, new ModMenuPatch());
	}
}
