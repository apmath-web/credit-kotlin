package viewModels

import models.CreditInterface as CreditModelInterface
import valueObjects.PersonInterface as PersonValueObjectInterface


interface CreditInterface : ViewModelInterface {
    fun hydrate(credit: CreditModelInterface)
    fun getPerson(): PersonValueObjectInterface
    fun getAmount(): String
    fun getAgreementAt(): String
    fun getCurrency(): String
    fun getDuration(): Int
    fun getPercent(): Int
}
