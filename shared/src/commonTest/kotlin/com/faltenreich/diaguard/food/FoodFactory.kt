package com.faltenreich.diaguard.food

object FoodFactory {

    fun createByUser(): Food.User {
        return Food.User(
            name = "name",
            brand = "brand",
            ingredients = "ingredients",
            labels = "labels",
            carbohydrates = 20.0,
            energy = 1.0,
            fat = 2.0,
            fatSaturated = 3.0,
            fiber = 4.0,
            proteins = 5.0,
            salt = 6.0,
            sodium = 7.0,
            sugar = 8.0,
        )
    }
}