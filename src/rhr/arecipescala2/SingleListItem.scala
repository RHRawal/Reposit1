package rhr.arecipescala2

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.app.SearchManager
import android.widget.SearchView
import android.os.Build
import android.content.Intent
import android.view.MenuItem
import android.content.Context
import android.content.res.Configuration

class SingleListItem extends Activity {
override def onCreate(savedInstanceState:Bundle):Unit = {
  super.onCreate(savedInstanceState)
		if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
			finish()
			return
		}
		
			 if (savedInstanceState == null) {
	                // During initial setup, plug in the details fragment.
	                val details:DetailsFragment = new DetailsFragment()
	                details.setArguments(getIntent().getExtras())
	                getFragmentManager().beginTransaction().add(android.R.id.content, details).commit()
	            }
}
 override def onCreateOptionsMenu(menu: Menu): Boolean = {
    val inflater: MenuInflater = getMenuInflater()
    inflater.inflate(R.menu.options_menu, menu)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE).asInstanceOf[SearchManager]
      val searchView: SearchView = menu.findItem(R.id.search).getActionView().asInstanceOf[SearchView]
      searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()))
      searchView.setIconifiedByDefault(false)
      searchView.setSubmitButtonEnabled(true)
      getActionBar.setHomeButtonEnabled(true)
    }
    return true
  }
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId() match {
      case R.id.search => onSearchRequested(); return true
      case android.R.id.home =>
        val intent: Intent = new Intent(this, classOf[ViewRecipesScala])
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("SENTHOME", "senthome")
        startActivity(intent)
        return true
      case _ => return false
    }
  }
}