package com.test.medpersonal.domain.models.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String? = "",
    var email: String? = ""
)
