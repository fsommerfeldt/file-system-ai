package ch.fsommerfeldt.unreliableaiagent

interface Data {
    fun type(): Int
    fun name(): String
}

data class Directory (val name: String, val content: MutableSet<Data> = mutableSetOf()) : Data {
    override fun type(): Int = 0
    override fun name(): String = name
    fun content(): Set<Data> = content
}

data class File (val name: String, val content: String = "") : Data {
    override fun type(): Int = 1
    override fun name(): String = name
    fun content(): String = content
}
