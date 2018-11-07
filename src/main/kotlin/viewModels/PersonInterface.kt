package viewModels

import models.PersonInterface as PersonModelInterface


interface PersonInterface : ViewModelInterface {
    fun hydrate(person: PersonModelInterface)
    fun getFirstName(): String
    fun getFirstName(): String
}
