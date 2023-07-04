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

package org.quiltmc.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.quiltmc.json5.JsonReader;
import org.quiltmc.json5.JsonToken;
import org.quiltmc.loader.api.VersionFormatException;
import org.quiltmc.loader.impl.metadata.qmj.SemanticVersionImpl;

public class VersionComparisonTests extends JsonTestBase {

	@Test
	public void testVersionComparisions() throws IOException, VersionFormatException {
		try (JsonReader json = get("testing/version/comparison.json")) {
			json.beginArray();
			while (true) {
				JsonToken token = json.peek();
				if (token == JsonToken.END_ARRAY) {
					json.endArray();
					return;
				}

				if (token != JsonToken.BEGIN_ARRAY) {
					throw new IOException(
						"Expected the end of the root array or the start of a new array, but got " + token
					);
				}
				json.beginArray();

				List<SemanticVersionImpl> versions = new ArrayList<>();

				while (true) {
					token = json.peek();
					if (token == JsonToken.END_ARRAY) {
						json.endArray();
						break;
					}
					if (token != JsonToken.STRING) {
						throw new IOException("Expected a string or the end of the array, but got " + token);
					}
					String version = json.nextString();
					versions.add(SemanticVersionImpl.of(version));
				}

				int count = versions.size();
				if (count < 2) {
					throw new IOException("Cannot compare less than two versions! " + versions);
				}

				for (int i = 0; i < count; i++) {
					SemanticVersionImpl versionA = versions.get(i);
					for (int j = 0; j < i; j++) {
						SemanticVersionImpl versionLower = versions.get(j);
						Assertions.assertTrue(versionA.compareTo(versionLower) > 0);
						Assertions.assertTrue(versionLower.compareTo(versionA) < 0);
					}

					for (int j = i + 1; j < count; j++) {
						SemanticVersionImpl versionUpper = versions.get(j);
						Assertions.assertTrue(versionA.compareTo(versionUpper) < 0, " ");
						Assertions.assertTrue(versionUpper.compareTo(versionA) > 0);
					}
				}
			}
		}
	}
}
