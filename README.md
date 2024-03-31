# Fable

**Fable** is a mod loader and fork of [quilt-loader](https://github.com/QuiltMC/quilt-loader) (which is a fork of
[https://github.com/FabricMC]). Fable aims to maximize compatibility: Fable maintains API compatibility with existing
Quilt mods, patches in compatibility for Fabric mods where necessary, and does not make any breaking changes that would
render previously-working mods broken.

Additionally, Fable introduces new APIs for any mods targeting Fable. These APIs allow for advanced game patching
scenarios, such as stubbing/mocking or reimplementing other APIs, as well as game- or version-agnostic bytecode patches.

## License

Fable is licensed under the GNU Affero General Public License, version 3 ("Affero GPL").

quilt-loader and fabric-loader are licensed under the Apache License, version 2. 

The `license.json` included in loader to generate SPDX license instances is licensed under Creative Commons Attribution
3.0 (SPDX License ID CC-BY-3.0) from SPDX.

## Contributing

When adding NEW classes (that you wrote yourself) they should use the Fable-only header file (`/codeformat/HEADER`)
When adding classes that contain code from quilt-loader they should use the modified quilt-loader header file
(`/codeformat/QUILT_MODIFIED_HEADER`)
When adding classes that contain code from fabric-loader they should use the modified fabric-loader header file
(`/codeformat/FABRIC_MODIFIED_HEADER`)

All files are expected to be encoded in UTF-8.

## Source folder layout

* `src/main/java` contains all "normal" loader source code.
    * `org.quiltmc.loader.api` quilt-loader's public api - public APIs quilt mods may interface with
    * `org.quiltmc.loader.impl` quilt-loader's internals - implementation details quilt mods may NOT[^1] interface with
* `src/test/java` contains test sources - these aren't built into the main jar file.
* `src/fabric/api/java` fabric-loader's apis - public APIs fabric mods may interface with
* `src/fabric/impl/java` fabric-loader's internal code - implementation details preserved for compatibility[^1]
* `src/fabric/legacy/java` fabric-loader's deprecated code, implementation details preserved for compatibility[^1]

When adding compatibility polyfills or stubs, they should be annotated as `@Deprecated` to inform users that these types
are not "officially" supported by upstream loaders, and so mods do not continue to accidentally use them (thereby making
the problem worse).~~~~
