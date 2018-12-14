package viewModels

import domain.valueObjects.PersonInterface as PersonValueObjectInterface

interface PersonInterface : ViewModelInterface {
    val firstName: String?
    val lastName: String?

    fun hydrate(person: PersonValueObjectInterface)
}
