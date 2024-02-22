package com.example.habity.feature_habit.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}