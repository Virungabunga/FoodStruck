import android.net.Uri
import com.google.firebase.firestore.DocumentId

data class FoodTruck (var companyName: String? = null,
                      var info : String? =null,
                      var smallPicId: Int? = null,
                      var long : Double? = null,
                      var lat : Double?=null,
                      var userID :String?=null,
                      var userPicID:Uri?=null,
                      @DocumentId var documentId: String? = null ) {}

