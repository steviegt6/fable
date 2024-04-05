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

package dev.tomat.fable.impl.game.bta.patches;

import java.util.ListIterator;

import dev.tomat.fable.impl.game.bta.Hooks;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.quiltmc.loader.impl.entrypoint.GamePatch;
import org.quiltmc.loader.impl.entrypoint.GamePatchContext;
import org.quiltmc.loader.impl.launch.common.QuiltLauncher;

public final class EntrypointPatch extends GamePatch {
	@Override
	public void process(QuiltLauncher launcher, GamePatchContext context) {
		String entrypoint = launcher.getEntrypoint();

		if (!entrypoint.startsWith("net.minecraft.")) {
			return;
		}

		ClassNode mainClass = context.getClassNode(entrypoint);

		if (mainClass == null) {
			throw new RuntimeException("Could not load main class " + entrypoint + "!");
		}

		MethodNode initMethod = findMethod(mainClass, method -> method.name.equals("main"));

		if (initMethod == null) {
			throw new RuntimeException("Could not find init method in " + entrypoint + "!");
		}

		ListIterator<AbstractInsnNode> instructions = initMethod.instructions.iterator();
		instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.INTERNAL_NAME, "init", "()V", false));
		context.addPatchedClass(mainClass);
	}
}
