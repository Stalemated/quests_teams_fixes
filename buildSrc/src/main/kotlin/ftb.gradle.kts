import dev.kikugie.stonecutter.build.StonecutterBuildExtension

val sc = extensions.getByType<StonecutterBuildExtension>()
val loader = sc.current.project.substringAfter('-')

data class FtbConfigurations(val compileOnly: String, val runtimeOnly: String)

val loaderConfigurations = mapOf(
    "fabric" to FtbConfigurations("modCompileOnly", "modLocalRuntime"),
    "forge" to FtbConfigurations("modCompileOnly", "modRuntimeOnly"),
)

val loaderConfiguration = requireNotNull(loaderConfigurations[loader]) {
    "loader must be one of ${loaderConfigurations.keys.toList()}, got '$loader'"
}

val ftbMods = listOf("deps.ftb_library", "deps.ftb_teams", "deps.ftb_quests").map { "curse.maven:${sc.properties.get<String>(it)}" }
val architectury = "dev.architectury:architectury-$loader:${sc.properties.get<String>("deps.architectury")}"

repositories {
    exclusiveContent {
        forRepository { maven("https://cursemaven.com") { name = "CurseMaven" } }
        filter { includeGroup("curse.maven") }
    }
    exclusiveContent {
        forRepository { maven("https://maven.architectury.dev/") { name = "Architectury" } }
        filter { includeGroup("dev.architectury") }
    }
}

dependencies {
    ftbMods.forEach {
        add(loaderConfiguration.compileOnly, it)
        add(loaderConfiguration.runtimeOnly, it)
    }
    add(loaderConfiguration.runtimeOnly, architectury)
}
