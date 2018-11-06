package valueobjects

import java.util.*


interface PaymentInterface {
    fun getPayment(): Currency
    fun getType()
    fun getCurrency()
    fun getDate(): Date

    fun getState()
}
