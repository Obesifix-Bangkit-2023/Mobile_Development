package org.obesifix.obesifix.preference

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val picture: String,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Int,
    val activity: String,
    val foodType: Array<String>,
    val createdAt: String,
    val updatedAt: String,
    //need approval
    val isLogin: String,
    val token: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (email != other.email) return false
        if (picture != other.picture) return false
        if (age != other.age) return false
        if (gender != other.gender) return false
        if (height != other.height) return false
        if (weight != other.weight) return false
        if (activity != other.activity) return false
        if (!foodType.contentEquals(other.foodType)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + picture.hashCode()
        result = 31 * result + age
        result = 31 * result + gender.hashCode()
        result = 31 * result + height
        result = 31 * result + weight
        result = 31 * result + activity.hashCode()
        result = 31 * result + foodType.contentHashCode()
        return result
    }
}