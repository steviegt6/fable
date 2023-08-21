/*
 * Copyright 2016 FabricMC
 * Copyright 2023 QuiltMC
 * Copyright 2023 Tomat and fable contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.loader.impl.entrypoint;

import dev.tomat.fable.api.entrypoint.ClassTweak;
import org.quiltmc.loader.impl.util.ExceptionUtil;
import org.quiltmc.loader.impl.util.LoaderUtil;
import org.quiltmc.loader.impl.util.QuiltLoaderInternal;
import org.quiltmc.loader.impl.util.QuiltLoaderInternalType;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.quiltmc.loader.impl.launch.common.QuiltLauncher;
import org.quiltmc.loader.impl.util.SimpleClassPath;
import org.quiltmc.loader.impl.util.log.Log;
import org.quiltmc.loader.impl.util.log.LogCategory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.zip.ZipError;

@QuiltLoaderInternal(QuiltLoaderInternalType.LEGACY_EXPOSED)

public class GameTransformer {
	private final List<GamePatch> entrypointPatches;
	private final List<ClassTweak> patches = new ArrayList<>();
	private Map<String, byte[]> patchedClasses;
	private boolean entrypointsLocated = false;

	public GameTransformer(GamePatch... entrypointPatches) {
		this.entrypointPatches = Arrays.asList(entrypointPatches);
	}

	private void addPatchedClass(ClassNode node) {
		String key = node.name.replace('/', '.');

		if (patchedClasses.containsKey(key)) {
			throw new RuntimeException("Duplicate addPatchedClasses call: " + key);
		}

		ClassWriter writer = new ClassWriter(0);
		node.accept(writer);
		patchedClasses.put(key, writer.toByteArray());
	}

	public void locateEntrypoints(QuiltLauncher launcher, List<Path> gameJars) {
		if (entrypointsLocated) {
			return;
		}

		patchedClasses = new HashMap<>();

		try (SimpleClassPath cp = new SimpleClassPath(gameJars)) {
			Function<String, ClassReader> classSource = name -> {
				byte[] data = patchedClasses.get(name);

				if (data != null) {
					return new ClassReader(data);
				}

				try {
					SimpleClassPath.CpEntry entry = cp.getEntry(LoaderUtil.getClassFileName(name));
					if (entry == null) return null;

					try (InputStream is = entry.getInputStream()) {
						return new ClassReader(is);
					} catch (IOException | ZipError e) {
						throw new RuntimeException(String.format("error reading %s in %s: %s", name, LoaderUtil.normalizePath(entry.getOrigin()), e), e);
					}
				} catch (IOException e) {
					throw ExceptionUtil.wrap(e);
				}
			};

			for (GamePatch patch : entrypointPatches) {
				patch.process(launcher, classSource, this::addPatchedClass);
			}
		} catch (IOException e) {
			throw ExceptionUtil.wrap(e);
		}

		Log.debug(LogCategory.GAME_PATCH, "Patched %d class%s", patchedClasses.size(), patchedClasses.size() != 1 ? "s" : "");
		entrypointsLocated = true;
	}

	public byte[] patchClass(String className, byte[] classBytes) {
		if (classBytes == null) {
			return null;
		}

		for (ClassTweak patch : patches) {
			Function<String, ClassReader> classSource = name -> {
				byte[] data = patchedClasses.get(name);

				if (data != null) {
					return new ClassReader(data);
				}

				return new ClassReader(classBytes);
			};

			ClassNode classNode = patch.process(className, classSource.apply(className));
			if (classNode != null) {
				addPatchedClass(classNode);
			}
		}

		return patchedClasses.getOrDefault(className, classBytes);
	}

	/**
	 * This must run first, contractually!
	 * @param className The class name,
	 * @return The transformed class data.
	 */
	public byte[] transform(String className) {
		return patchedClasses.get(className);
	}

	public void addPatch(ClassTweak patch) {
		Log.info(LogCategory.GAME_PATCH, "Adding patch %s", patch);
		patches.add(patch);
	}
}
