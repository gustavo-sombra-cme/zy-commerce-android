pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ZY-Commerce Android"

include(":app")
include(":core:common")
include(":core:designsystem")
include(":core:network")
include(":core:database")
include(":core:storage")
include(":domain:auth")
include(":domain:catalog")
include(":data:auth")
include(":data:catalog")
include(":feature:auth")
include(":feature:catalog")
include(":feature:productadmin")
