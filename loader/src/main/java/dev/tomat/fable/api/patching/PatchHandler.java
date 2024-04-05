package dev.tomat.fable.api.patching;

import java.util.Set;

import org.quiltmc.loader.api.ModContainer;

public interface PatchHandler {
	void registerPatch(ModContainer mod, Patch patch);

	Set<Patch> getPatches();

	Set<Patch> getPatches(ModContainer mod);
}
