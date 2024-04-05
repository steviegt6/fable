/*
 * Fable - quilt-/fabric-loader fork; <https://github.com/steviegt6/fable>
 * Copyright (C) 2024  Tomat et al.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
