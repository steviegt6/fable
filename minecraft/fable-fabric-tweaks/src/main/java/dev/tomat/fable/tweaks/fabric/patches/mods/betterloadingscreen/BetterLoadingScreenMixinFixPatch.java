package dev.tomat.fable.tweaks.fabric.patches.mods.betterloadingscreen;

import dev.tomat.fable.api.entrypoint.ClassTweak;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class BetterLoadingScreenMixinFixPatch extends ClassTweak {
	@Override
	public ClassNode process(String className, ClassReader classReader) {
		if (!className.equals("me.shedaniel.betterloadingscreen.mixin.fabric.MixinMinecraft")) {
			return null;
		}

		ClassNode mixinMinecraft = readClass(classReader);
		MethodNode initMethod = mixinMinecraft.methods.stream().filter(method -> method.name.equals("init")).findFirst().orElseThrow(NullPointerException::new);
		AnnotationNode redirectAnnotation = initMethod.visibleAnnotations.stream().filter(annotation -> annotation.desc.equals("Lorg/spongepowered/asm/mixin/injection/Redirect;")).findFirst().orElseThrow(NullPointerException::new);
		int atIndex = redirectAnnotation.values.indexOf("at");
		AnnotationNode atNode = (AnnotationNode) redirectAnnotation.values.get(atIndex + 1);
		int targetIndex = atNode.values.indexOf("target");
		String target = (String) atNode.values.get(targetIndex + 1);
		target = target.replace("net/fabricmc/loader/impl/game/minecraft/Hooks", "org/quiltmc/loader/impl/game/minecraft/Hooks");
		atNode.values.set(targetIndex + 1, target);

		return mixinMinecraft;
	}
}
