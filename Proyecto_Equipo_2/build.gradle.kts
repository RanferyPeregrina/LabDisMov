// build.gradle.kts (nivel raíz)

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
