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

package org.quiltmc.loader.impl.game.minecraft.applet;

/**
 * PLEASE NOTE:
 *
 * <p>This class is originally copyrighted under Apache License 2.0
 * by the MCUpdater project (https://github.com/MCUpdater/MCU-Launcher/).
 *
 * <p>It has been adapted here for the purposes of the Fabric loader.
 */
class AppletForcedShutdownListener implements Runnable {
	private final long duration;

	AppletForcedShutdownListener(long duration) {
		this.duration = duration;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		System.out.println("~~~ Forcing exit! ~~~");
		System.exit(0);
	}
}
