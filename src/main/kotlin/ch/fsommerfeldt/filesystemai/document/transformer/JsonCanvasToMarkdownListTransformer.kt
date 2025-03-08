package ch.fsommerfeldt.filesystemai.document.transformer

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.DocumentTransformer
import org.json.JSONArray
import org.json.JSONObject

class JsonCanvasToMarkdownListTransformer: DocumentTransformer {

    override fun transform(doc: Document): Document {

        val fileName = doc.metadata().getString("file_name")
        if (!fileName.endsWith(".canvas")) {
            return doc
        }

        val root = JSONObject(doc.text())
        val nodesJsonArray = root.getJSONArray("nodes")
        val edgesJsonArray = root.getJSONArray("edges")

        // TODO: Add support for node type group
        val textNodes = getTextNodes(nodesJsonArray)
        val edges = getEdges(edgesJsonArray)

        for (edge in edges) {
            val textNode = textNodes.find { textNode -> textNode.id ==  edge.fromNode}
            val child = textNodes.find { textNode -> textNode.id ==  edge.toNode }
            if (child != null && textNode != null) { // Happens for unsupported node types, like group
                textNode.children.add(child)
            }
        }

        val rootNodes = textNodes.filter { textNode -> textNode.isRootNode(edges) }

        var markdownText = ""
        rootNodes.map { node -> markdownText +=  node.toMarkdown()}

        return Document.from(markdownText, doc.metadata().copy())
    }

    private fun getEdges(edges: JSONArray) = edges
        .map { edge: Any? -> edge as JSONObject }
        .map { edge -> Edge(edge.getString("fromNode"), edge.getString("toNode")) }

    private fun getTextNodes(nodes: JSONArray) = nodes
        .map { node: Any? -> node as JSONObject }
        .filter { node -> node.getString("type") == "text" }
        .map { node -> TextNode(node.getString("id"), node.getString("text")) }

}