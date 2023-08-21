/*
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

package net.fabricmc.loader.impl.metadata;

import org.quiltmc.loader.impl.fabric.metadata.ParseMetadataException;
import org.quiltmc.loader.impl.metadata.FabricLoaderModMetadata;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Included by Fable for backwards compatibility.
 */
@Deprecated
public final class ModMetadataParser {
	@Deprecated
	public static final int LATEST_VERSION = org.quiltmc.loader.impl.fabric.metadata.FabricModMetadataReader.LATEST_VERSION;

	@Deprecated
	public static final Set<String> IGNORED_KEYS = org.quiltmc.loader.impl.fabric.metadata.FabricModMetadataReader.IGNORED_KEYS;

	@Deprecated
	public static FabricLoaderModMetadata parseMetadata(InputStream is, String ignoredModPath, List<String> ignoredModParentPaths, VersionOverrides ignoredVersionOverrides, DependencyOverrides ignoredDepOverrides, boolean ignoredIsDevelopment) throws ParseMetadataException {
		return org.quiltmc.loader.impl.fabric.metadata.FabricModMetadataReader.parseMetadata(is);
	}
}
