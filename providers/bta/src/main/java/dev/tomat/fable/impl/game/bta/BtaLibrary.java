package dev.tomat.fable.impl.game.bta;

import net.fabricmc.api.EnvType;

import org.quiltmc.loader.impl.game.LibClassifier;

enum BtaLibrary implements LibClassifier.LibraryType {
	MC_CLIENT(EnvType.CLIENT, "net/minecraft/client/Minecraft.class"),
	MC_SERVER(EnvType.SERVER, "net/minecraft/server/MinecraftServer.class"),
	MC_COMMON("net/minecraft/server/MinecraftServer.class"),
	LOG4J_API("org/apache/logging/log4j/LogManager.class"),
	LOG4J_CORE("META-INF/services/org.apache.logging.log4j.spi.Provider", "META-INF/log4j-provider.properties"),
	LOG4J_CONFIG("log4j2.xml"),
	LOG4J_PLUGIN("com/mojang/util/UUIDTypeAdapter.class"), // in authlib
	LOG4J_PLUGIN_2("com/mojang/patchy/LegacyXMLLayout.class"), // in patchy
	LOG4J_PLUGIN_3("net/minecrell/terminalconsole/util/LoggerNamePatternSelector.class"), // in terminalconsoleappender, used by loom's log4j config
	SLF4J_API("org/slf4j/Logger.class"),
	SLF4J_CORE("META-INF/services/org.slf4j.spi.SLF4JServiceProvider");

	static final BtaLibrary[] LOGGING = { LOG4J_API, LOG4J_CORE, LOG4J_CONFIG, LOG4J_PLUGIN, LOG4J_PLUGIN_2, LOG4J_PLUGIN_3, SLF4J_API, SLF4J_CORE };

	private final EnvType env;
	private final String[] paths;

	BtaLibrary(String path) {
		this(null, new String[] { path });
	}

	BtaLibrary(String... paths) {
		this(null, paths);
	}

	BtaLibrary(EnvType env, String... paths) {
		this.paths = paths;
		this.env = env;
	}

	@Override
	public boolean isApplicable(EnvType env) {
		return this.env == null || this.env == env;
	}

	@Override
	public String[] getPaths() {
		return paths;
	}
}
