package domain.data

inline class Money(val value: Long) {
    operator fun plus(money: Money): Money {
        return Money(value + money.value)
    }

    operator fun times(money: Money): Money {
        return Money(value * money.value)
    }

    operator fun div(money: Money): Money {
        return Money(value / money.value)
    }

    operator fun minus(money: Money): Money {
        return Money(value - money.value)
    }

    operator fun compareTo(money: Money): Int {
        return value.compareTo(money.value)
    }

}
