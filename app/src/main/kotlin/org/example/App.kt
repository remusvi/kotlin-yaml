package org.example

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Paths

data class Step(
    val name: String?,
    val run: String?
)

data class Job(
    val id: String,
    val runsOn: String?,
    val steps: MutableList<Step> = mutableListOf()
)

fun readYaml(yamlPath: String): Map<String, Any> {
    val file = File(yamlPath)
    if (!file.exists()) {
        throw IllegalArgumentException("YAML file not found at: $yamlPath")
    }
    val yaml = Yaml()
    return file.inputStream().use { inputStream ->
        yaml.load<Map<String, Any>>(inputStream) ?: emptyMap()
    }
}

fun executeStep(step: Step): Boolean {
    val command = step.run ?: return true
    println("\n==> Step: ${step.name ?: "Run Script"}")
    println("    $ $command")

    try {
        val isWindows = System.getProperty("os.name").lowercase().contains("win")
        val processBuilder = if (isWindows) {
            ProcessBuilder("cmd.exe", "/c", command)
        } else {
            ProcessBuilder("bash", "-c", command)
        }

        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        InputStreamReader(process.inputStream).use { reader ->
            reader.forEachLine { line ->
                println("    $line")
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            println("Step failed with exit code: $exitCode")
            return false
        }
    } catch (e: Exception) {
        println("Execution error: ${e.message}")
        return false
    }
    return true
}

fun handleJob(job: Job): Boolean {
    println("----------------------------------------")
    println("Starting Job: ${job.id} (Runner: ${job.runsOn ?: "ubuntu-latest"})")
    println("----------------------------------------")

    for (step in job.steps) {
        val success = executeStep(step)
        if (!success) {
            println("Job '${job.id}' failed.")
            return false
        }
    }
    println("Job '${job.id}' completed successfully.")
    return true
}

fun main(args: Array<String>) {
    // if (args.isEmpty()) {
    //     println("Usage: kotlin AppKt <path-to-workflow.yml>")
    //     return
    // }

    val yamlPath = "C:/Users/adafa/dev/school/kotlin-yaml/job-test.yaml"
    val cleanPath = Paths.get(yamlPath).normalize().toString()
    val workflowMap = readYaml(cleanPath)

    val jobsMap = workflowMap["jobs"] as? Map<*, *>
    if (jobsMap != null) {
        for ((jobIdObj, jobDetailsObj) in jobsMap) {
            val jobId = jobIdObj.toString()
            val jobDetails = jobDetailsObj as? Map<*, *> ?: continue

            val runsOn = jobDetails["runs-on"]?.toString()
            val job = Job(id = jobId, runsOn = runsOn)

            val rawSteps = jobDetails["steps"] as? List<*>
            if (rawSteps != null) {
                for (stepObj in rawSteps) {
                    val stepMap = stepObj as? Map<*, *> ?: continue
                    val stepName = stepMap["name"]?.toString()
                    val stepRun = stepMap["run"]?.toString()
                    job.steps.add(Step(name = stepName, run = stepRun))
                }
            }

            val success = handleJob(job)
            if (!success) {
                System.exit(1)
            }
        }
    } else {
        println("No 'jobs' section found in the YAML workflow file.")
    }
}
