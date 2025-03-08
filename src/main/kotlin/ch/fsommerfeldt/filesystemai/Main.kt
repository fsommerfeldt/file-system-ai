package ch.fsommerfeldt.filesystemai

import ch.fsommerfeldt.filesystemai.document.transformer.JsonCanvasToMarkdownListTransformer
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import dev.langchain4j.store.embedding.EmbeddingStore
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import java.time.Duration
import kotlin.io.path.Path
import kotlin.io.path.isDirectory

private const val MODEL_NAME = "qwen2.5:14b"
private const val BASE_URL = "http://localhost:11434"

fun main() {

    val model = chatModel()

    println("Please give me the absolute path to the directory containing the documents I am allowed to read.")
    val absolutePath = readln()
    val workDir = Path(absolutePath)

    assert(workDir.isAbsolute) { "The given path is not absolute." }
    assert(workDir.isDirectory()) { "The given path is not a directory." }

    println("Load documents...")
    var docs = FileSystemDocumentLoader.loadDocumentsRecursively(workDir)
    println("${docs.size} documents found.")

    println("I can filter the documents by certain keywords. This way only documents containing one of the keywords " +
            "will be considered. If you would like this, please give me a comma-separated list. (code,requirements," +
            "good practices)")
    val keywordsString = readln()

    if (keywordsString.isNotBlank()) {
        docs = docs.filterDocsByKeywords(keywordsString)
        println("Reduced the number of documents to ${docs.size}.")
    }

    val embeddingStore = embeddingStore(docs)

    val assistant = AiServices.builder(Assistant::class.java)
        .chatLanguageModel(model)
        .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
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



private interface Assistant {

    @SystemMessage("I have given you documents that will give you additional context to the messages I will be sending you.")
    fun chat(userMessage: String): AiMessage

}

private fun chatModel(): ChatLanguageModel = OllamaChatModel.builder()
    .baseUrl(BASE_URL)
    .modelName(MODEL_NAME)
    .temperature(0.0)
    .timeout(Duration.ofSeconds(300))
    .build()

private fun embeddingStore(docs: List<Document>): EmbeddingStore<TextSegment> {
    val transformer = JsonCanvasToMarkdownListTransformer()

    val embeddingStore = InMemoryEmbeddingStore<TextSegment>()
    val ingestor = EmbeddingStoreIngestor.builder()
        .embeddingStore(embeddingStore)
        .documentTransformer(transformer)
        .build()

    ingestor.ingest(docs)

    return embeddingStore
}

private fun List<Document>.filterDocsByKeywords(keywordsString: String): List<Document> {
    val keywords = if (keywordsString.contains(",")) {
        keywordsString.split(',')
    } else {
        listOf(keywordsString)
    }

    val filteredDocs = mutableListOf<Document>()
    keywords.forEach {
            keyword -> filteredDocs.addAll(
        this.filter { doc -> doc.text().contains(keyword, ignoreCase = true)
                || doc.metadata().getFileName().contains(keyword, ignoreCase = true) }
    )
    }

    return filteredDocs
}

private fun Metadata.getFileName(): String {
    return getString("file_name")
}
