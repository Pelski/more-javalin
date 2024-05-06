object VeryImportantStorage {

    private val storage = listOf(
        "Java",
        "Kotlin",
        "Scala",
        "Groovy",
        "Clojure",
        "JS, ty nie"
    )

    private var index = 0

    fun getNext(): String {
        val next = storage[index]
        index = (index + 1) % storage.size
        return next
    }

}
