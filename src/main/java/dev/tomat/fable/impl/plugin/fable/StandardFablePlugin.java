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

import org.quiltmc.json5.exception.ParseException;
import org.quiltmc.loader.api.FasterFiles;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.gui.QuiltDisplayedError;
import org.quiltmc.loader.api.gui.QuiltLoaderGui;
import org.quiltmc.loader.api.gui.QuiltLoaderIcon;
import org.quiltmc.loader.api.gui.QuiltLoaderText;
import org.quiltmc.loader.api.plugin.ModLocation;
import org.quiltmc.loader.api.plugin.gui.PluginGuiTreeNode;
import org.quiltmc.loader.api.plugin.solver.ModLoadOption;
import org.quiltmc.loader.impl.metadata.qmj.InternalModMetadata;
import org.quiltmc.loader.impl.metadata.qmj.ModMetadataReader;
import org.quiltmc.loader.impl.plugin.BuiltinQuiltPlugin;
import org.quiltmc.loader.impl.util.QuiltLoaderInternal;
import org.quiltmc.loader.impl.util.QuiltLoaderInternalType;

import java.io.IOException;
import java.nio.file.Path;

@QuiltLoaderInternal(QuiltLoaderInternalType.NEW_INTERNAL)
public class StandardFablePlugin extends BuiltinQuiltPlugin {
	@Override
	public ModLoadOption[] scanZip(Path root, ModLocation location, PluginGuiTreeNode guiNode) throws IOException {
		Path parent = context().manager().getParent(root);

		if (!parent.getFileName().toString().endsWith(".jar")) {
			return null;
		}

		return scan0(root, QuiltLoaderGui.iconJarFile(), location, true, guiNode);
	}

	@Override
	public ModLoadOption[] scanFolder(Path folder, ModLocation location, PluginGuiTreeNode guiNode) throws IOException {
		return scan0(folder, QuiltLoaderGui.iconFolder(), location, false, guiNode);
	}

	private ModLoadOption[] scan0(Path root, QuiltLoaderIcon fileIcon, ModLocation location, boolean isZip, PluginGuiTreeNode guiNode) throws IOException {
		Path fmj = root.resolve("fable.mod.json");
		if (!FasterFiles.isRegularFile(fmj)) {
			return null;
		}

		try {
			InternalModMetadata meta = ModMetadataReader.read(fmj, context().manager(), guiNode);

			Path from = root;
			if (isZip) {
				from = context().manager().getParent(root);
			}

			jars: for (String jar : meta.jars()) {
				Path inner = root;
				for (String part : jar.split("/")) {
					if ("..".equals(part)) {
						continue jars;
					}
					inner = inner.resolve(part);
				}

				if (inner == from) {
					continue;
				}

				PluginGuiTreeNode jarNode = guiNode.addChild(QuiltLoaderText.of(jar), PluginGuiTreeNode.SortOrder.ALPHABETICAL_ORDER);
				context().addFileToScan(inner, jarNode, false);
			}

			// a mod needs to be remapped if we are in a development environment, and the mod
			// did not come from the classpath
			boolean requiresRemap = !location.onClasspath() && QuiltLoader.isDevelopmentEnvironment();
			return new ModLoadOption[] { new FableModOption(context(), meta, from, fileIcon, root, location.isDirect(), requiresRemap) };
		}
		catch (ParseException parse) {
			QuiltLoaderText title = QuiltLoaderText.translate("gui.text.invalid_metadata.title", "fable.mod.json", parse.getMessage());
			QuiltDisplayedError error = context().reportError(title);
			String describedPath = context().manager().describePath(fmj);
			error.appendReportText("Invalid 'fable.mod.json' metadata file:" + describedPath);
			error.appendDescription(QuiltLoaderText.translate("gui.text.invalid_metadata.desc.0", describedPath));
			error.appendThrowable(parse);
			context().manager().getRealContainingFile(root).ifPresent(real ->
					error.addFileViewButton(QuiltLoaderText.translate("button.view_file"), real)
							.icon(QuiltLoaderGui.iconJarFile().withDecoration(QuiltLoaderGui.iconFabric()))
			);

			guiNode.addChild(QuiltLoaderText.translate("gui.text.invalid_metadata", parse.getMessage()))//TODO: translate
					.setError(parse, error);
			return null;
		}
	}
}
