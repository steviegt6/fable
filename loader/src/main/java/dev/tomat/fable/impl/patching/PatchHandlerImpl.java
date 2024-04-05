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
