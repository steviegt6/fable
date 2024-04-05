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
