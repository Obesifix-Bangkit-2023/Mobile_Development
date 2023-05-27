package org.obesifix.obesifix.network.body

data class RegisterBody(
    var age: Int,
    var gender: String,
    var height: Float,
    var weight: Float,
    var activity: String,
    var food_type: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterBody

        if (age != other.age) return false
        if (gender != other.gender) return false
        if (height != other.height) return false
        if (weight != other.weight) return false
        if (activity != other.activity) return false
        if (!food_type.contentEquals(other.food_type)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = age
        result = 31 * result + gender.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + weight.hashCode()
        result = 31 * result + activity.hashCode()
        result = 31 * result + food_type.contentHashCode()
        return result
    }
}