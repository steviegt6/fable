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

package dev.tomat.fable.impl.plugin.fable;

import org.quiltmc.loader.api.gui.QuiltLoaderIcon;
import org.quiltmc.loader.api.plugin.ModContainerExt;
import org.quiltmc.loader.api.plugin.ModMetadataExt;
import org.quiltmc.loader.api.plugin.QuiltPluginContext;
import org.quiltmc.loader.impl.gui.GuiManagerImpl;
import org.quiltmc.loader.impl.plugin.base.InternalModOptionBase;
import org.quiltmc.loader.impl.plugin.fabric.FabricModContainer;

import java.nio.file.Path;

public class FableModOption extends InternalModOptionBase {
	public FableModOption(QuiltPluginContext pluginContext, ModMetadataExt meta, Path from, QuiltLoaderIcon fileIcon, Path resourceRoot, boolean mandatory, boolean requiresRemap) {
		super(pluginContext, meta, from, fileIcon, resourceRoot, mandatory, requiresRemap);
	}

	@Override
	public QuiltLoaderIcon modTypeIcon() {
		return GuiManagerImpl.ICON_JAR; // TODO: Icon.
	}

	@Override
	public ModContainerExt convertToMod(Path transformedResourceRoot) {
		return new FableModContainer(pluginContext, metadata, from, transformedResourceRoot);
	}

	@Override
	protected String nameOfType() {
		return "fable";
	}
}
