java_binary (
    name = "desktop",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**/*"]),
    main_class = "org.crosswire.flashcards.Quiz",
    deps = [
        "//flashcards", 
        "//lessons",
        "@maven//:com_formdev_flatlaf"
    ]
)
