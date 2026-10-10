# Overview

As a software engineer, my goal in building this project was to explore low-level systems automation and configuration parsing by building a custom YAML-driven workflow and task runner engine. By implementing this tool, I aimed to deepen my understanding of Kotlin's interoperability with JVM libraries, robust type casting, and operating-system-level process management.

[Software Demo Video](http://youtube.link.goes.here)

# Development Environment

The development environment was structured around modern, efficient tooling optimized for JVM and systems engineering:
*   **Integrated Development Environment & Editors:** IntelliJ IDEA Ultimate and Neovim configured with custom Lua keymaps and the Kotlin Language Server (LSP) for rapid code navigation and refactoring.
*   **Build System:** Gradle with the Kotlin DSL (`build.gradle.kts`) for declarative dependency management.
*   **Version Control & Containerization:** Git for version tracking and Docker for local test execution.

The core programming language used was **Kotlin**, chosen for its conciseness, null safety, and clean data class structures. The project leveraged key libraries:
*   **SnakeYAML:** For parsing and loading nested YAML configuration maps.
*   **Java NIO & Standard Library:** Utilizing `ProcessBuilder`, `InputStreamReader`, and `Paths` for cross-platform process spawning and filesystem normalization.

# Useful Websites
The following resources were invaluable during the design and implementation of this project:

- [Kotlin Official Documentation](https://kotlinlang.org/docs/home.html)
- [SnakeYAML Repository & Documentation](https://bitbucket.org/snakeyaml/snakeyaml/src/master/)
- [Java ProcessBuilder API Reference](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/ProcessBuilder.html)

# Future Work

To further expand the capabilities of this workflow engine, I have outlined the following roadmap:

- Add support for environment variable injection and secret masking across individual job steps.
- Implement parallel job execution using Kotlin Coroutines to run independent jobs concurrently rather than     sequentially.
- Introduce robust error handling and step retry policies (e.g., `continue-on-error` and `max-retries`).
