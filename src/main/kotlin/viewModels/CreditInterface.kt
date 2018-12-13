package viewModels

import domain.data.Money
import domain.data.Currency
import java.time.LocalDate
import domain.models.CreditInterface as CreditModelInterface
import domain.valueObjects.PersonInterface as PersonValueObjectInterface


interface CreditInterface : ViewModelInterface {
    val person: PersonInterface?
    val amount: Money?
    val agreementAt: LocalDate?
    val currency: Currency?
    val duration: Int?
    val percent: Int?

    fun hydrate(credit: CreditModelInterface)
}
