package dev.tomat.modmenu_patches.patches;

import dev.tomat.fable.api.patching.Patch;
import dev.tomat.fable.api.patching.PatchContext;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public final class ModMenuPatch extends Patch {
	@Override
	public void process(PatchContext context) {
		if (!context.getClassName().equals("io.github.prospector.modmenu.util.BadgeRenderer")) {
			return;
		}

		ClassNode badgeRenderer = context.getClassNode();
		MethodNode draw = findMethod(badgeRenderer, method -> method.name.equals("draw"));

		if (draw == null) {
			return;
		}

		boolean patched = false;

		AbstractInsnNode node = findInsn(draw, insn -> insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst.equals("minecraft"), false);
		if (node instanceof LdcInsnNode) {
			((LdcInsnNode) node).cst = "bta";
			patched = true;
		}

		context.setPatched(patched);
	}
}
