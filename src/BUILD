load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "core",
    srcs = glob(["**/*.kt"]),
    neverlink = True,
    resource_strip_prefix = "src",
    resources = ["META-INF/services/javax.annotation.processing.Processor"],
)
