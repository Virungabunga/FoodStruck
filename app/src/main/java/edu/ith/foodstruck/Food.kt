package edu.ith.foodstruck

import com.google.firebase.firestore.DocumentId

class Food( var name : String? = null,
            var description: String? = null,
            var price : Int? = null,
            var picture: String? = null ,
            var userId : String? = null,
            @DocumentId var documentId: String?= null
) {
}