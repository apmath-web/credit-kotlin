package domain.valueObjects

class Person(firstName: String, lastName: String) : PersonInterface {

    override val firstName: String = firstName

    override val lastName: String = lastName
}
