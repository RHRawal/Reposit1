package rhr.arecipescala2

import android.app.ListFragment
import android.content.Intent
import android.app.SearchManager
import java.util.ArrayList
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import android.os.Bundle
import android.widget.ArrayAdapter
import android.view.View
import android.widget.ListView
import android.widget.AbsListView
import android.app.FragmentTransaction
import java.io.IOException
import android.database.SQLException
import android.util.Log
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.util.{Success,Failure}


class FragmentList extends ListFragment {
var mDualPane:Boolean = _
var mCurCheckPosition:Int = 0
var myDbHelper:Helper = _
var recipenames:ArrayList[String] = new ArrayList[String]()

override def onActivityCreated(savedInstanceState:Bundle):Unit = {
  super.onActivityCreated(savedInstanceState)
  setListAdapter(new ArrayAdapter[String](getActivity(),
                android.R.layout.simple_list_item_activated_1, recipenames))
       myDbHelper = new Helper(getActivity().getApplicationContext())
 
  var detailsFrame:View = getActivity().findViewById(R.id.recipedetails)
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE
        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0)
        }
 
    if (mDualPane) getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
    handleIntent(getActivity().getIntent())
}
 override def onListItemClick(l:ListView, v:View, position:Int, id:Long):Unit = {
    	showRecDetails(position)
        }
 def showRecDetails(index:Int):Unit = {
	   mCurCheckPosition = index
	   if ((getListView().getAdapter()==null)||(getListView().getAdapter().getCount()<=index)){
		   return
	   }
	   if (mDualPane) getListView().setItemChecked(index, true)
	   var strRecipeName:String =  getListView().getItemAtPosition(index).asInstanceOf[String]
	   val mspecfuture = Future {
	     myDbHelper.getspecRecipe(strRecipeName)
	   }
	   mspecfuture.onComplete {
	     case Success(mval) => 
           var mydbspecRecipe:String = mval
           if (mydbspecRecipe == null) mydbspecRecipe = ""
             if (mDualPane) {
				var details:DetailsFragment= getFragmentManager().findFragmentById(R.id.recipedetails).asInstanceOf[DetailsFragment]
				   if (details == null || details.getShownIndex() != mCurCheckPosition) {
		        	   details = DetailsFragment.newInstance(mCurCheckPosition,mydbspecRecipe)
		               var ft:FragmentTransaction = getFragmentManager().beginTransaction()
		               ft.replace(R.id.recipedetails, details)
		               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		               ft.commit()
		           }
			   } else {
				   var intent:Intent = new Intent()
		           intent.setClass(getActivity(), classOf[SingleListItem])
		           intent.putExtra("index", mCurCheckPosition)
		           intent.putExtra("recproduct", mydbspecRecipe)
		           startActivity(intent)
			   }
	     case Failure(e)  =>   Log.w("RHR","error in showresultspecific onComplete")
	   }
	
   }
 
 
 def handleIntent(intent:Intent) : Unit = {
    var squery:String = ""
      if (Intent.ACTION_VIEW.equals(intent.getAction())){
        Log.w("RHR","in handleintent ACTION_VIEW branch")
      } 
      else if (Intent.ACTION_SEARCH.equals(intent.getAction())){
        squery = intent.getStringExtra(SearchManager.QUERY)
            showResults(squery)
          }
      else if (Intent.ACTION_MAIN.equals(intent.getAction())){
        squery = ""
              showResults(squery)
          } else if (intent.getStringExtra("SENTHOME").trim().equals("senthome")){
        squery = "" 
              showResults(squery)
          }
    
  }
 def showResults(mq:String):Unit ={
   Log.w("RHR","showresults entered.")
   val mresfuture = Future {
     val yy: ArrayList[String]= new ArrayList[String]
     
     if (mq.isEmpty) {
          val xx:List[String] = myDbHelper.getAllRecipeNames
          yy.addAll((xx.reverse).asJava);yy
   } else {
      val xx:List[String] = myDbHelper.searchRecipeNames(mq)
          yy.addAll((xx.reverse).asJava);yy
   }
   }
   mresfuture.onComplete{
     case Success(mval) =>
         recipenames.clear() 
     recipenames.addAll(mval)
        if (mDualPane) showRecDetails(mCurCheckPosition)
     refresh()
     case Failure(e) =>  Log.w("RHR","error in showresults onComplete")
     
   }
 }
  
  
  def refresh():Unit =  {
		val ad:ArrayAdapter[String]  =  getListView().getAdapter().asInstanceOf[ArrayAdapter[String]]
		ad.notifyDataSetChanged()
	}
  
}