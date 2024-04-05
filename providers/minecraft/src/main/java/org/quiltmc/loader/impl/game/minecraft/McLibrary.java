/*
 * Fable - quilt-/fabric-loader fork; <https://github.com/steviegt6/fable>
 * Copyright (C) 2024  Tomat et al.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.quiltmc.loader.impl.game.minecraft;

import net.fabricmc.api.EnvType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.quiltmc.loader.impl.game.LibClassifier;

enum McLibrary implements LibClassifier.LibraryType {
	MC_CLIENT(EnvType.CLIENT, "net/minecraft/client/main/Main.class", "net/minecraft/client/MinecraftApplet.class", "com/mojang/minecraft/MinecraftApplet.class"),
	MC_SERVER(EnvType.SERVER, "net/minecraft/server/Main.class", "net/minecraft/server/MinecraftServer.class", "com/mojang/minecraft/server/MinecraftServer.class"),
	MC_COMMON("net/minecraft/server/MinecraftServer.class"),
	MC_ASSETS_ROOT("assets/.mcassetsroot"),
	MC_BUNDLER(EnvType.SERVER, "net/minecraft/bundler/Main.class"),
	REALMS(EnvType.CLIENT, "realmsVersion", "com/mojang/realmsclient/RealmsVersion.class"),
	MODLOADER("ModLoader"),
	LOG4J_API("org/apache/logging/log4j/LogManager.class"),
	LOG4J_CORE("META-INF/services/org.apache.logging.log4j.spi.Provider", "META-INF/log4j-provider.properties"),
	LOG4J_CONFIG("log4j2.xml"),
	LOG4J_PLUGIN("com/mojang/util/UUIDTypeAdapter.class"), // in authlib
	LOG4J_PLUGIN_2("com/mojang/patchy/LegacyXMLLayout.class"), // in patchy
	LOG4J_PLUGIN_3("net/minecrell/terminalconsole/util/LoggerNamePatternSelector.class"), // in terminalconsoleappender, used by loom's log4j config
	GSON("com/google/gson/TypeAdapter.class"), // used by log4j plugins
	SLF4J_API("org/slf4j/Logger.class"),
	SLF4J_CORE("META-INF/services/org.slf4j.spi.SLF4JServiceProvider"),
	AUTHLIB("com/mojang/authlib/GameProfile.class", "yggdrasil_session_pubkey.der"),
	BRIGADIER("com/mojang/brigadier/Command.class"),
	DATA_FIXER_UPPER("com/mojang/datafixers/schemas/Schema.class"),
	JAVA_BRIDGE("com/mojang/bridge/Bridge.class");

	static final McLibrary[] GAME = { MC_CLIENT, MC_SERVER, MC_BUNDLER };
	static final McLibrary[] LOGGING = { LOG4J_API, LOG4J_CORE, LOG4J_CONFIG, LOG4J_PLUGIN, LOG4J_PLUGIN_2, LOG4J_PLUGIN_3, GSON, SLF4J_API, SLF4J_CORE };

	/** Libraries which mods can transform with mixin or chasm. */
	static final Map<String, McLibrary> MINECRAFT_SPECIFIC;

	static {
		Map<String, McLibrary> map = new HashMap<>();
		map.put("authlib", AUTHLIB);
		map.put("brigadier", BRIGADIER);
		map.put("datafixerupper", DATA_FIXER_UPPER);
		map.put("java_bridge", JAVA_BRIDGE);
		MINECRAFT_SPECIFIC = Collections.unmodifiableMap(map);
	}

	private final EnvType env;
	private final String[] paths;

	McLibrary(String path) {
		this(null, new String[] { path });
	}

	McLibrary(String... paths) {
		this(null, paths);
	}

	McLibrary(EnvType env, String... paths) {
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
