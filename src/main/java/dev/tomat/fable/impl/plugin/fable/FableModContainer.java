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

import org.quiltmc.loader.api.plugin.ModMetadataExt;
import org.quiltmc.loader.api.plugin.QuiltPluginContext;
import org.quiltmc.loader.impl.plugin.base.InternalModContainerBase;
import org.quiltmc.loader.impl.util.QuiltLoaderInternal;
import org.quiltmc.loader.impl.util.QuiltLoaderInternalType;

import java.nio.file.Path;

@QuiltLoaderInternal(QuiltLoaderInternalType.NEW_INTERNAL)
public class FableModContainer extends InternalModContainerBase {
	public FableModContainer(QuiltPluginContext pluginContext, ModMetadataExt metadata, Path from, Path resourceRoot) {
		super(pluginContext, metadata, from, resourceRoot);
	}

	@Override
	public BasicSourceType getSourceType() {
		return BasicSourceType.OTHER;
	}

	@Override
	public String modType() {
		return "Fable";
	}
}
