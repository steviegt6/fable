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
