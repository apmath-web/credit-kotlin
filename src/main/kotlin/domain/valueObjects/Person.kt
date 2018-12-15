package domain.valueObjects

data class Person(override val firstName: String, override val lastName: String) : PersonInterface
