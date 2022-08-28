# NYN project

## Build

For build this project you need install Bazel.

```bash
bazel build //src:core
```

## How use?

### Install

Build jar file and edit `build.gradle` file

```gradle
dependencies {
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.4.1'
    implementation files('path/to/build_project/bazel-bin/src/core.jar')
    kapt files('path/to/build_project/bazel-bin/src/core.jar')
}
```
