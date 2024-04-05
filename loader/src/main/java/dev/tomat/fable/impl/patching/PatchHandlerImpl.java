package dev.tomat.fable.impl.patching;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import dev.tomat.fable.api.patching.Patch;
import dev.tomat.fable.api.patching.PatchHandler;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.impl.util.QuiltLoaderInternal;
import org.quiltmc.loader.impl.util.QuiltLoaderInternalType;

@QuiltLoaderInternal(QuiltLoaderInternalType.NEW_INTERNAL)
public final class PatchHandlerImpl implements PatchHandler {
	private final Map<Patch, ModContainer> modByPatch = new HashMap<>();

	@Override
	public void registerPatch(ModContainer mod, Patch patch) {
		modByPatch.put(patch, mod);
	}

	@Override
	public Set<Patch> getPatches() {
		return modByPatch.keySet();
	}

	@Override
	public Set<Patch> getPatches(ModContainer mod) {
		return modByPatch.entrySet().stream()
				.filter(entry -> entry.getValue().equals(mod))
				.map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}
}
