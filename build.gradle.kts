plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.github.tabsweep"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        
        pluginVerifier()
        zipSigner()
        instrumentationTools()
        
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
    
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.8.0")
}

intellijPlatform {
    pluginConfiguration {
        id = "com.github.tabsweep"
        name = "TabSweep"
        version = project.version.toString()
        description = """
            Sweep through your tabs with ease. A powerful tab management plugin for IntelliJ IDEA.

            Features:
            - View all open tabs in a unified popup
            - Fuzzy search to quickly find tabs
            - Select multiple tabs using checkboxes
            - Close selected tabs in bulk
            - Keyboard-navigable interface (Emacs-inspired)
            - De-duplicated file list (files shown once even if open in multiple splits)
        """.trimIndent()

        vendor {
            name = "TabSweep"
        }
        
        ideaVersion {
            sinceBuild = "243"
            untilBuild = "251.*"
        }
        
        changeNotes = """
            <h3>1.0.0</h3>
            <ul>
                <li>Initial release</li>
                <li>Tab selection and bulk close functionality</li>
                <li>Keyboard navigation support</li>
            </ul>
        """.trimIndent()
    }
    
    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }
    
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }
    
    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
    
    test {
        useJUnit()
    }
}
