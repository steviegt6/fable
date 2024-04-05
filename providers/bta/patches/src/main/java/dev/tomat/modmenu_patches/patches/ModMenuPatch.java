package dev.tomat.modmenu_patches.patches;

import dev.tomat.fable.api.patching.Patch;
import dev.tomat.fable.api.patching.PatchContext;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public final class ModMenuPatch extends Patch {
	private static final String[] CANDIDATES = {
			"io.github.prospector.modmenu.util.BadgeRenderer::draw",
			"io.github.prospector.modmenu.ModMenu::onInitialize",
			"io.github.prospector.modmenu.gui.ModListEntry::createIcon",
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
