package dev.tomat.fable.api.patching;

import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Patch {
	public void process(PatchContext context) {
		throw new AbstractMethodError();
	}

	protected FieldNode findField(ClassNode node, Predicate<FieldNode> predicate) {
		return node.fields.stream().filter(predicate).findAny().orElse(null);
	}

	protected List<FieldNode> findFields(ClassNode node, Predicate<FieldNode> predicate) {
		return node.fields.stream().filter(predicate).collect(Collectors.toList());
	}

	protected MethodNode findMethod(ClassNode node, Predicate<MethodNode> predicate) {
		return node.methods.stream().filter(predicate).findAny().orElse(null);
	}

	protected AbstractInsnNode findInsn(MethodNode node, Predicate<AbstractInsnNode> predicate, boolean last) {
		if (last) {
			for (int i = node.instructions.size() - 1; i >= 0; i--) {
				AbstractInsnNode insn = node.instructions.get(i);

				if (predicate.test(insn)) {
					return insn;
				}
			}
		} else {
			for (int i = 0; i < node.instructions.size(); i++) {
				AbstractInsnNode insn = node.instructions.get(i);

				if (predicate.test(insn)) {
					return insn;
				}
			}
		}

		return null;
	}

	protected void moveAfter(ListIterator<AbstractInsnNode> it, int opcode) {
		while (it.hasNext()) {
			AbstractInsnNode node = it.next();

			if (node.getOpcode() == opcode) {
				break;
			}
		}
	}

	protected void moveBefore(ListIterator<AbstractInsnNode> it, int opcode) {
		moveAfter(it, opcode);
		it.previous();
	}

	protected void moveAfter(ListIterator<AbstractInsnNode> it, AbstractInsnNode targetNode) {
		while (it.hasNext()) {
			AbstractInsnNode node = it.next();

			if (node == targetNode) {
				break;
			}
		}
	}

	protected void moveBefore(ListIterator<AbstractInsnNode> it, AbstractInsnNode targetNode) {
		moveAfter(it, targetNode);
		it.previous();
	}

	protected void moveBeforeType(ListIterator<AbstractInsnNode> it, int nodeType) {
		while (it.hasPrevious()) {
			AbstractInsnNode node = it.previous();

			if (node.getType() == nodeType) {
				break;
			}
		}
	}
}
