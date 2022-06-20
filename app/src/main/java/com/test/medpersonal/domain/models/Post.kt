package com.test.medpersonal.domain.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap

//@IgnoreExtraProperties
data class Post(
    var uid: String? = null,
    var author: String? = null,
    var to: String? = null,
    var body: String? = null,
    var date: String? = null,
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "uid" to uid,
                "author" to author,
                "to" to to,
                "body" to body,
                "date" to date,
        )
    }
}
