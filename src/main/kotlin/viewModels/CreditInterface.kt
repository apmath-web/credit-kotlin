package viewModels

import java.util.*
import models.CreditInterface as CreditModelInterface
import valueObjects.PersonInterface as PersonValueObjectInterface


interface CreditInterface : ViewModelInterface {
    fun hydrate(credit: CreditModelInterface)
    val person: PersonValueObjectInterface?
    val amount: data.Money?
    val agreementAt: Date?
    val currency: data.Currency?
    val duration: Int?
    val percent: Int?
}
