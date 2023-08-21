package dev.tomat.fable.tweaks.fabric.patches.mods.armoredelytra;

import dev.tomat.fable.api.entrypoint.ClassTweak;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public class ArmoredElytraMixinPriorityPatch extends ClassTweak {
	@Override
	public ClassNode process(String className, ClassReader classReader) {
		if (!className.equalsIgnoreCase("xyz.mrmelon54.armoredelytra.mixin.MixinArmorFeatureRenderer")) {
			return null;
		}

		ClassNode mixinArmorFeatureRenderer = readClass(classReader);
		AnnotationNode mixinAnnotation = mixinArmorFeatureRenderer.invisibleAnnotations.stream().filter(annotation -> annotation.desc.equals("Lorg/spongepowered/asm/mixin/Mixin;")).findFirst().orElseThrow(NullPointerException::new);
		int priorityIndex = mixinAnnotation.values.indexOf("priority");
		if (priorityIndex == -1) {
			mixinAnnotation.values.add("priority");
			mixinAnnotation.values.add(999);
		}
		else {
			int priority = (int) mixinAnnotation.values.get(priorityIndex + 1);
			if (priority != 999) {
				mixinAnnotation.values.set(priorityIndex + 1, 999);
			}
		}

		return mixinArmorFeatureRenderer;
	}
}
