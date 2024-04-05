package dev.tomat.fable.api.patching;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public interface PatchContext {
	String getClassName();

	ClassReader getClassReader();

	ClassNode getClassNode();

	boolean getPatched();

	void setPatched(boolean patched);
}
