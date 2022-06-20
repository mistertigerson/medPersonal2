package com.test.medpersonal.domain.models

import com.google.firebase.database.Exclude

data class Users(
    var lastName: String? = null,
    var phone: String? = null,
    var name: String? = null,
    var secondName: String? =  null,
    var specific : String? = null,
    var company : String? = null,
    var uid: String? = null,

) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "lastName" to lastName,
            "phone" to phone,
            "name" to name,
            "secondName" to secondName,
            "specific" to specific,
            "company" to company,
            "uid" to uid
        )

    }
}



