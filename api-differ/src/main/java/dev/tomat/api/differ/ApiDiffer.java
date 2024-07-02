package dev.tomat.api.differ;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ApiDiffer {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out.println("Usage: java -jar api-differ.jar <old-jar.jar> <new-jar.jar> <output.txt>");
			System.exit(1);
		}

		String oldJar = args[0];
		String newJar = args[1];
		String outputFile = args[2];

		var oldClasses = loadClassesFromJar(oldJar);
		var newClasses = loadClassesFromJar(newJar);

		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(outputFile)))) {
			writer.println("--- " + oldJar);
			writer.println("+++ " + newJar);

			Set<String> allClassNames = new HashSet<>();
			allClassNames.addAll(oldClasses.keySet());
			allClassNames.addAll(newClasses.keySet());

			List<String> addedClasses = new ArrayList<>();
			List<String> removedClasses = new ArrayList<>();
			List<String> modifiedClasses = new ArrayList<>();

			for (String className : allClassNames) {
				ClassNode oldClass = oldClasses.get(className);
				ClassNode newClass = newClasses.get(className);

				if (oldClass == null) {
					addedClasses.add(className);
				} else if (newClass == null) {
					removedClasses.add(className);
				} else {
					modifiedClasses.add(className);
				}
			}

			for (String className : addedClasses) {
				writer.println("+" + className);
			}

			for (String className : removedClasses) {
				writer.println("-" + className);
			}

			for (String className : modifiedClasses) {
				List<String> diffs = compareClasses(oldClasses.get(className), newClasses.get(className));

				if (diffs.isEmpty()) {
					continue;
				}

				writer.println(" " + className);

				for (String diff : diffs) {
					writer.println(diff);
				}
			}
		}
	}

	private static Map<String, ClassNode> loadClassesFromJar(String jarFile) throws IOException {
		Map<String, ClassNode> classes = new HashMap<>();

		try (JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile))) {
			JarEntry entry;

			while ((entry = jarInputStream.getNextJarEntry()) != null) {
				if (entry.getName().endsWith(".class")) {
					ClassReader reader = new ClassReader(jarInputStream);
					ClassNode node = new ClassNode();
					reader.accept(node, 0);
					classes.put(node.name, node);
				}
			}
		}

		return classes;
	}

	private static List<String> compareClasses(ClassNode oldClass, ClassNode newClass) {
		List<String> differences = new ArrayList<>();

		Set<String> oldMethods = new HashSet<>();
		Set<String> newMethods = new HashSet<>();

		for (MethodNode method : oldClass.methods) {
			oldMethods.add(method.name + method.desc);
		}

		for (MethodNode method : newClass.methods) {
			newMethods.add(method.name + method.desc);
		}

		produceDiffs(differences, oldMethods, newMethods);

		Set<String> oldFields = new HashSet<>();
		Set<String> newFields = new HashSet<>();

		for (FieldNode field : oldClass.fields) {
			oldFields.add(field.desc + " " + field.name);
		}

		for (FieldNode field : newClass.fields) {
			newFields.add(field.desc + " " + field.name);
		}

		produceDiffs(differences, oldFields, newFields);

		return differences;
	}

	private static void produceDiffs(List<String> differences, Set<String> oldMembers, Set<String> newMembers) {
		for (String field : oldMembers) {
			if (!newMembers.contains(field)) {
				differences.add("-    " + field);
			}
		}

		for (String field : newMembers) {
			if (!oldMembers.contains(field)) {
				differences.add("+    " + field);
			}
		}
	}
}
