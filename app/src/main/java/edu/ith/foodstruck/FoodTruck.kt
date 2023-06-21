import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import edu.ith.foodstruck.Food

data class FoodTruck (var companyName: String? = null,
                      var info : String? =null,
                      var smallPicId: Int? = null,
                      var long : Double? = null,
                      var lat : Double?=null,
                      var userID :String?=null,
                      var userPicID:String?=null,
                      @DocumentId var documentId: String? = null,
                      @Exclude var menuList : MutableList<Food>? = null) {}