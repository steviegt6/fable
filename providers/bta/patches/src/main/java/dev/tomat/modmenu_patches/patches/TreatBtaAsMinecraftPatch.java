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
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Patches ModMenu to compare mods to the `bta` name instead of `minecraft`, rendering the Quilt-provided BTA entry as
 * if it were Minecraft.
 * <p/>
 * This is necessary because our BTA provider registers the game as `bta` like Turnip Labs' BTA provider, instead of as
 * Minecraft like a regular Babric instance.
 */
public final class TreatBtaAsMinecraftPatch extends Patch {
	private static final String[] CANDIDATES = {
			"io.github.prospector.modmenu.util.BadgeRenderer::draw",
			"io.github.prospector.modmenu.ModMenu::onInitialize",
			"io.github.prospector.modmenu.gui.ModListEntry::createIcon",
			"io.github.prospector.modmenu.util.HardcodedUtil::initializeHardcodings",
	};

	@Override
	public void process(PatchContext context) {
		for (String candidate : CANDIDATES) {
			String[] parts = candidate.split("::");

			if (parts.length != 2) {
				continue;
			}

			if (context.getClassName().equals(parts[0])) {
				processCandidate(context, parts[1]);
			}
		}
	}

	private void processCandidate(PatchContext context, String methodName) {
		ClassNode badgeRenderer = context.getClassNode();
		MethodNode draw = findMethod(badgeRenderer, method -> method.name.equals(methodName));

		if (draw == null) {
			return;
		}

		boolean patched = false;
		AbstractInsnNode node;

		while ((node = findInsn(draw, insn -> insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst.equals("minecraft"), false)) instanceof LdcInsnNode) {
			((LdcInsnNode) node).cst = "bta";
			patched = true;
		}

		context.setPatched(patched);
	}
}
