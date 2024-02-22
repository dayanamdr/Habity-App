package com.example.habity.feature_habit.domain.util

sealed class HabitOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): HabitOrder(orderType)
    class Label(orderType: OrderType): HabitOrder(orderType)
    class Date(orderType: OrderType): HabitOrder(orderType)

    fun copy(orderType: OrderType): HabitOrder {
        return when(this) {
            is Name -> Name(orderType)
            is Label -> Label(orderType)
            is Date -> Date(orderType)
        }
    }
}