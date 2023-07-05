/*
 * Copyright 2023 Tomat and fable contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.loader.impl.util;

import java.util.function.Function;

/**
 * Included by Fable for backwards compatibility.
 */
@Deprecated
public final class ExceptionUtil {
	@Deprecated
	public static <T extends Throwable> T gatherExceptions(Throwable exc, T prev, Function<Throwable, T> mainExcFactory) throws T {
		return org.quiltmc.loader.impl.util.ExceptionUtil.gatherExceptions(exc, prev, mainExcFactory);
	}

	@Deprecated
	public static RuntimeException wrap(Throwable exc) {
		return org.quiltmc.loader.impl.util.ExceptionUtil.wrap(exc);
	}
}
