package dev.tomat.fable.tweaks.fabric.patches.mods.bigbeacons;

import dev.tomat.fable.api.entrypoint.ClassTweak;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BigBeaconQslCompatibilityFixPatch extends ClassTweak {
	@Override
	public ClassNode process(String className, ClassReader classReader) {
		return switch (className) {
			case "easton.bigbeacons.mixin.LivingEntityMixin" -> patchBigBeaconsMixin(classReader);
			case "org.quiltmc.qsl.entity.effect.mixin.LivingEntityMixin" -> patchQslMixin(classReader);
			default -> null;
		};
	}

	private ClassNode patchBigBeaconsMixin(ClassReader classReader) {
		ClassNode classNode = readClass(classReader);

		// Remove @Redirect annotation.
		MethodNode dontRemoveFlight = findMethod(classNode, method -> method.name.equals("dontRemoveFlight"));
		dontRemoveFlight.visibleAnnotations.clear();

		return classNode;
	}

	private ClassNode patchQslMixin(ClassReader classReader) {
		ClassNode classNode = readClass(classReader);

		MethodNode quilt$removeWithUpgradeApplyingReason = findMethod(classNode, method -> method.name.equals("quilt$removeWithUpgradeApplyingReason"));

		LabelNode methodHead = new LabelNode();
		InsnList instructions = new InsnList();

		instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
		instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "easton/bigbeacons/BigBeacons", "FLIGHT", "Leaston/bigbeacons/BigBeacons;"));
		instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false));
		instructions.add(new JumpInsnNode(Opcodes.IFNE, methodHead));
		instructions.add(new InsnNode(Opcodes.RETURN));
		instructions.add(methodHead);

		quilt$removeWithUpgradeApplyingReason.instructions.insertBefore(
			quilt$removeWithUpgradeApplyingReason.instructions.getFirst(),
			instructions
		);

		return classNode;
	}
}
