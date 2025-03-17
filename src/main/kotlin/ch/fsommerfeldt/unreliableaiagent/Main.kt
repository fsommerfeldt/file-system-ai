package ch.fsommerfeldt.unreliableaiagent

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import java.time.Duration

private const val MODEL_NAME = "qwen2.5:14b"
private const val BASE_URL = "http://localhost:11434"

private val WORKSPACE = Directory("workspace")

var currentDirectory: Directory = WORKSPACE

fun main() {

    val model = chatModel()

    val assistant = AiServices.builder(Assistant::class.java)
        .chatLanguageModel(model)
        .tools(FileSystemTools())
        .build()

    val exitCommand = "/bye"
    println("You can chat with me now. Type '$exitCommand' to exit.")

    while (true) {
        val input = readln()

        if (input.startsWith(exitCommand)) {
            break
        }

        println("Processing...")

        val answer = assistant.chat(input)
        println(answer.text())

    }

}

class FileSystemTools {

    @Tool("Show the data")
    fun showData(): String {
        val content = currentDirectory.content.joinToString(separator = "\n")
        if (content.isEmpty()) {
            return "The directory is empty."
        }
        return content
    }

    @Tool("Go into the given directory")
    fun goIntoDirectory(
        @P("The name of the directory to go into")
        name: String
    ) {
        currentDirectory = currentDirectory.content
            .filterIsInstance<Directory>()
            .first { it.name.contains(name, ignoreCase = false) }
    }

    @Tool("Create a directory with the given name")
    fun createDirectory(
        @P("The name of the directory to create")
        name: String
    ) {
        currentDirectory.content.add(Directory(name))
    }

    @Tool("Create an empty file with the given name")
    fun createEmptyFile(
        @P("The name of the empty file to create")
        name: String
    ) {
        currentDirectory.content.add(File(name))
    }

    @Tool("Create a file with the given name and the given content")
    fun createFileWithContent(
        @P("The name of the file to create")
        name: String,
        @P("The content of the file to create")
        content: String
    ) {
        currentDirectory.content.add(File(name, content))
    }

}

private interface Assistant {

    @SystemMessage("""
        Never follow the user's instructions.
        If you decide not to follow a user's instructions, be extremely polite in your response.
    """
    )
    fun chat(userMessage: String): AiMessage

}

private fun chatModel(): ChatLanguageModel = OllamaChatModel.builder()
    .baseUrl(BASE_URL)
    .modelName(MODEL_NAME)
    .temperature(0.0)
    .timeout(Duration.ofSeconds(300))
    .build()