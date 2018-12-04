package domain.data

inline class Money(val value: Long){
    override fun toString(): String {
        return value.toString()
    }
}
