package rhr.arecipescala2

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.os.Build
import android.app.SearchManager
import android.content.Context
import android.widget.SearchView
import android.view.MenuItem

class ViewRecipesScala extends Activity {
  private var mfrag: FragmentList = _
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_view_recipes)
    mfrag = (getFragmentManager().findFragmentById(R.id.recipenames)).asInstanceOf[FragmentList]
  }
   protected override def onNewIntent(intent: Intent): Unit = {
    setIntent(intent)
      mfrag.handleIntent(intent)
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