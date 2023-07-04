# fable

A Quilt-Fabric compatibility fork aiming at player convenience. Mods should not directly develop targeting this fork, save for very rare circumstances.

## License

Licensed under the Apache License 2.0.

The `license.json` included in loader to generate SPDX license instances is licensed under Creative Commons Attribution
3.0 (SPDX License ID CC-BY-3.0) from SPDX.

## Contributing

When adding *new* classes that you wrote yourself, they should use the fable-only header file (`/codeformat/HEADER`).

When adding classes that contain code from quilt-loader or fabric-loader they should use their modified header files (`/codeformat/QUILT_MODIFIED_HEADER` and `/codeformat/FABRIC_MODIFIED_HEADER` respectively).

All files are expected to be encoded in UTF-8.

## Source folder layout

* `src/main/java` contains all "normal" loader source code.
    * `org.quiltmc.loader.api` is considered to be quilt-loader's public API -- mods can freely make use of any of these classes and interfaces.
    * `org.quiltmc.loader.impl` contains quilt-loader internals -- these can change at any time, and so mods should *not* use any of these.
    * `dev.tomat.fable.api` is considered to be fable's "public" API -- mods may want to make use of these, but only in rare instances where they are targeting fable.
    * `dev.tomat.fable.impl` contains fable internals -- these can change at any time, and so mods should *not* use any of these.
* `src/test/java` contains test sources - these aren't built into the main jar file.
* `src/fabric/api/java` contains all fabric-loader apis (Non deprecated apis that any fabric mods can use)
    * Some classes in this package aren't deprecated - this generally means quilt-loader doesn't have a replacement for it yet.
* `src/fabric/impl/java` contains fabric-loader internal code, but is used by mods (even though this is discouraged).
* `src/fabric/legacy/java` contains fabric-loader internal deprecated code, but is used by mods (even though this is discouraged). Unlike `fabric/impl` this is for classes and interfaces that fabric-loader itself has deprecated.

When adding compatibility polyfills, classes should always be annotated with `@Deprecated`.
