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

package dev.tomat.modmenu_patches.patches;

import dev.tomat.fable.api.patching.Patch;
import dev.tomat.fable.api.patching.PatchContext;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Fixes ModMenu's parent entries not being expandable.
 */
public final class FixParentEntriesNotBeingExpandablePatch extends Patch {
	@Override
	public void process(PatchContext context) {
		if (!context.getClassName().equals("io.github.prospector.modmenu.gui.EntryListWidget")) {
			return;
		}

		ClassNode entryListWidget = context.getClassNode();
		MethodNode mouseClicked = findMethod(entryListWidget, method -> method.name.equals("mouseClicked"));

		if (mouseClicked == null) {
			return;
		}

		// TODO: I don't feel like it.
	}
}
