package ch.fsommerfeldt.filesystemai.document.transformer

data class TextNode(val id: String, val text: String, val children: MutableList<TextNode> = arrayListOf()) {

    fun toMarkdown(level: Int = 0): String {
        var markdown = "- $text\n"

        for (child in children) {
            for (i in level downTo 0) {
                markdown += "\t"
            }
            markdown += child.toMarkdown(level + 1)
        }

        return markdown
    }

    fun isRootNode(edges: List<Edge>): Boolean {
        return edges.none { connection -> connection.toNode == this.id }
    }

}
