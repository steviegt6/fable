package dev.tomat.fable.impl.game.bta;

import java.io.File;
import java.util.ListIterator;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.quiltmc.loader.impl.QuiltLoaderImpl;
import org.quiltmc.loader.impl.entrypoint.EntrypointUtils;
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
		instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, EntrypointPatch.class.getName().replace('.', '/'), "hookInit", "()V", false));
		context.addPatchedClass(mainClass);
	}

	public static void init() {
		QuiltLoaderImpl.INSTANCE.prepareModInit(new File(".").toPath(), QuiltLoaderImpl.INSTANCE.getGameInstance());
		EntrypointUtils.invoke("main", ModInitializer.class, it -> it.onInitialize());

		if (QuiltLoaderImpl.INSTANCE.getEnvironmentType() == EnvType.CLIENT) {
			EntrypointUtils.invoke("client", ClientModInitializer.class, it -> it.onInitializeClient());
		} else {
			EntrypointUtils.invoke("server", DedicatedServerModInitializer.class, it -> it.onInitializeServer());
		}
	}
}
