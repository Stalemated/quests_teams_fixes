import dev.kikugie.stonecutter.build.StonecutterBuildExtension

val sc = extensions.getByType<StonecutterBuildExtension>()
val loader = sc.current.project.substringAfter('-')

data class FtbConfigurations(val compileOnly: String, val runtimeOnly: String)

val loaderConfigurations = mapOf(
    "fabric" to FtbConfigurations("modCompileOnly", "modLocalRuntime"),
    "forge" to FtbConfigurations("modCompileOnly", "modRuntimeOnly"),
    "neoforge" to FtbConfigurations("compileOnly", "runtimeOnly"),
)

val loaderConfiguration = requireNotNull(loaderConfigurations[loader]) {
    "loader must be one of ${loaderConfigurations.keys.toList()}, got '$loader'"
}

val ftbMods = listOf("deps.ftb_library", "deps.ftb_teams", "deps.ftb_quests").map { sc.properties.get<String>(it) }
val architectury = sc.properties.getOrNull<String>("deps.architectury")?.let { "dev.architectury:architectury-$loader:$it" }

repositories {
    exclusiveContent {
        forRepository { maven("https://cursemaven.com") { name = "CurseMaven" } }
        filter { includeGroup("curse.maven") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.ftb.dev/releases") { name = "FTB" } }
        filter { includeGroup("dev.ftb.mods") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.architectury.dev/") { name = "Architectury" } }
        filter { includeGroup("dev.architectury") }
    }
}

dependencies {
    ftbMods.forEach {
        add(loaderConfiguration.compileOnly, it) { isTransitive = false }
        add(loaderConfiguration.runtimeOnly, it) { isTransitive = false }
    }
    architectury?.let { add(loaderConfiguration.runtimeOnly, it) }
}
