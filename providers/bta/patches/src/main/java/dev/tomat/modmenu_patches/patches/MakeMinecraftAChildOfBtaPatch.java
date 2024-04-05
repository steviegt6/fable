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

import com.google.common.collect.LinkedListMultimap;
import dev.tomat.fable.api.patching.Patch;
import dev.tomat.fable.api.patching.PatchContext;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;
import java.util.ListIterator;

/**
 * Patches ModMenu to interpret `minecraft` as a child of `bta` and to register `mixinextras` as a library.
 */
public final class MakeMinecraftAChildOfBtaPatch extends Patch {
	@Override
	public void process(PatchContext context) {
		if (!context.getClassName().equals("io.github.prospector.modmenu.util.HardcodedUtil")) {
			return;
		}

		ClassNode hardcodedUtil = context.getClassNode();
		MethodNode hardcodeModuleMetadata = findMethod(hardcodedUtil, method -> method.name.equals("hardcodeModuleMetadata"));

		if (hardcodeModuleMetadata == null) {
			return;
		}

		ListIterator<AbstractInsnNode> instructions = hardcodeModuleMetadata.instructions.iterator();
		instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "io/github/prospector/modmenu/ModMenu", "PARENT_MAP", "Lcom/google/common/collect/LinkedListMultimap;"));
		instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "io/github/prospector/modmenu/ModMenu", "LIBRARY_MODS", "Ljava/util/List;"));
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
		instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
		instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, MakeMinecraftAChildOfBtaPatch.class.getName().replace('.', '/'), "addMinecraftToBta", "(Lcom/google/common/collect/LinkedListMultimap;Ljava/util/List;Lnet/fabricmc/loader/api/ModContainer;Ljava/lang/String;)V", false));
		context.setPatched(true);
	}

	public static void addMinecraftToBta(LinkedListMultimap<ModContainer, ModContainer> parentMap, List<String> libraryMods, ModContainer mod, String id) {
		if (id.equals("minecraft")) {
			parentMap.put(FabricLoader.getInstance().getModContainer("bta").get(), mod);
		}

		if (id.equals("mixinextras")) {
			libraryMods.add(id);
		}
	}
}
