package viewModels

import java.util.*
import models.CreditInterface as CreditModelInterface
import valueObjects.PersonInterface as PersonValueObjectInterface


interface CreditInterface : ViewModelInterface {
    fun hydrate(credit: CreditModelInterface)
    fun getPerson(): PersonValueObjectInterface
    fun getAmount(): Currency
    fun getAgreementAt(): Date
    fun getCurrency(): data.Currency
    fun getDuration(): Int
    fun getPercent(): Int
}
