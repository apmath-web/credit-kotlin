package viewModels

import valueObjects.PersonInterface as PersonValueObjectInterface

interface PersonInterface : ViewModelInterface {
    fun hydrate(person: PersonValueObjectInterface)
    val firstName: String?
    val lastName: String?
}
