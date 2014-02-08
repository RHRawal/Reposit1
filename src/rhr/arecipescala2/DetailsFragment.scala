package rhr.arecipescala2

import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import android.util.TypedValue
import android.view.ViewGroup

class DetailsFragment extends Fragment {
    @Override
    override def onCreateView(inflater:LayoutInflater,container:ViewGroup,
            savedInstanceState:Bundle):View = {
        if (container == null) return null
        var  scroller:ScrollView = new ScrollView(getActivity())
        var text:TextView = new TextView(getActivity())
        val padding:Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics()).asInstanceOf[Int]
        text.setPadding(padding, padding, padding, padding)
        scroller.addView(text)
        text.setText(getShownStrAtIndex())
        return scroller
    }
  
  def getShownStrAtIndex():String  = {
    	getArguments().getString("recproduct","");
    }
  def getShownIndex():Int = {
        return getArguments().getInt("index", 0);
    }
}
object DetailsFragment extends Fragment {
 def  newInstance(index:Int,strprod:String):DetailsFragment = {
    	var f:DetailsFragment = new DetailsFragment()
        var args:Bundle  = new Bundle()
        args.putInt("index",index)
        args.putString("recproduct", strprod)
        f.setArguments(args)
        return f
    }
  
}