package rhr.arecipescala2
import android.content.Context
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.StatusLine
import org.apache.http.HttpStatus
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.util.{Success,Failure}
import android.util.Log
import org.json.JSONArray
import org.apache.http.HttpEntity
import org.apache.http.client.ResponseHandler
import org.apache.http.impl.client.BasicResponseHandler
import android.util.Log
import org.json.JSONObject

class Helper(context:Context) {

 import Helper._ 

def jRecipeNames(mstr:String): List[String] = {
  val jr:JSONArray = new JSONArray(mstr)
var ret = List[String](); var i:Int=0
 while (i<jr.length()) {ret= jr.get(i).asInstanceOf[String]::ret; i=i+1}
ret
}
def jRecipeDetails(mstr:String):String = {
 
   val jr:JSONArray = new JSONArray(mstr)
  val jo:JSONObject = jr.getJSONObject(0)
  val mid:Int=jo.getInt("id")
  val mrecname=jo.getString("recname")
  val mkeypoints=jo.getString("keypoints")
  val mingred=jo.getString("ingred")
  val mmethod=jo.getString("method")
  Log.w("RHE","in jrecipedetails method "+mmethod)
  val ret=mrecname+"\n Key Points"+"\n"+mkeypoints+"\n Ingredients"+"\n"+mingred+"\n Method"+"\n"+mmethod
  ret
}
def searchRecipeNames(sq:String): List[String] = {
    val httpClient:HttpClient = new DefaultHttpClient()
val handler:ResponseHandler[String]    = new BasicResponseHandler();  
  try {
  val result =httpClient.execute(new HttpGet(BASEURL+SRCHURL+"/"+sq),handler)
   val ret=jRecipeNames(result)
  ret
} catch {case e:Exception => Log.w("RHR","error in getallnames "+e);null}
}
def getAllRecipeNames():List[String] = {
  val httpClient:HttpClient = new DefaultHttpClient()
val handler:ResponseHandler[String]    = new BasicResponseHandler();  
  try {
  val result =httpClient.execute(new HttpGet(BASEURL+ALLRECIPESURL),handler)
   val ret=jRecipeNames(result)
  ret
} catch {case e:Exception => Log.w("RHR","error in getallnames "+e);null}
}
 

def getspecRecipe(mrec:String):String = {
  val httpClient:HttpClient = new DefaultHttpClient()
val handler:ResponseHandler[String]    = new BasicResponseHandler();  
  try {
  val result =httpClient.execute(new HttpGet(BASEURL+ADDSPEC+"/"+mrec),handler)
  return jRecipeDetails(result)
} catch {case e:Exception => Log.w("RHR","error in getallnames "+e);null}
}
}
object Helper {
 val BASEURL= "http://10.0.2.2:8080"
val ALLRECIPESURL = "/recipes"
val ADDSPEC = "/recipeof" 
val SRCHURL = "/recipelike" 
}