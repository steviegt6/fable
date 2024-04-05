package dev.tomat.fable.impl.game.bta;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

import org.quiltmc.loader.impl.QuiltLoaderImpl;
import org.quiltmc.loader.impl.entrypoint.EntrypointUtils;

import java.io.File;

public final class Hooks {
	public static final String INTERNAL_NAME = Hooks.class.getName().replace('.', '/');

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
