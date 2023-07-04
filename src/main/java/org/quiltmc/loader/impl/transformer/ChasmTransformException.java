/*
 * Copyright 2023 QuiltMC
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

package org.quiltmc.loader.impl.transformer;

import org.quiltmc.loader.impl.discovery.ModResolutionException;
import org.quiltmc.loader.impl.util.QuiltLoaderInternal;
import org.quiltmc.loader.impl.util.QuiltLoaderInternalType;

/** Thrown when something goes wrong with chasm. */
@QuiltLoaderInternal(QuiltLoaderInternalType.NEW_INTERNAL)
public class ChasmTransformException extends ModResolutionException {

	public ChasmTransformException(String format, Object... args) {
		super(format, args);
	}

	public ChasmTransformException(String s, Throwable t) {
		super(s, t);
	}

	public ChasmTransformException(String s) {
		super(s);
	}

	public ChasmTransformException(Throwable t) {
		super(t);
	}
}
