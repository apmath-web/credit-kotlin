package viewModels

import valueObjects.PersonInterface as PersonValueObjectInterface

interface PersonInterface : ViewModelInterface {
    fun hydrate(person: PersonValueObjectInterface)
    fun getFirstName(): String
    fun getLastName(): String
}
