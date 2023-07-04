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
