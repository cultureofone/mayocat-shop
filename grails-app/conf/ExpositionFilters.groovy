import org.mayocat.shop.viewmodel.builder.CartViewModelBuilder
import org.mayocat.shop.viewmodel.builder.CategoryViewModelBuilder
import org.mayocat.shop.viewmodel.builder.PageViewModelBuilder
import org.mayocat.shop.viewmodel.builder.ProductViewModelBuilder
import org.mayocat.shop.grails.Category
import org.mayocat.shop.grails.Page
import org.mayocat.shop.grails.Shop
import org.mayocat.shop.grails.Product
import org.mayocat.shop.grails.PackageManagement

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class ExpositionFilters {

  def filters = {

    expositionFilter(controller:'*', controllerExclude:'imageSet', action:'expose*') {

      after = { viewModel ->
        // Enhancing returned viewModel with API available to all pages :
        //

        // The shop instance
        viewModel["shop"] = Shop.list()[0] ?: new Shop(packageManagement: new PackageManagement())

        // Base (prefix) for storefront assets. FIXME Find a way to get rid of this
        viewModel["assets_base"] = "/storefronts/" + viewModel["shop"].storefront

        // fragment = /path/to/template + '.html' extension
        viewModel["fragment"] = "/storefronts/" + viewModel["shop"].storefront + "/" + (viewModel["template"] ?: "") + ".html"

        if (!viewModel) {
          viewModel = [:]
        }
        def taglib = new ApplicationTagLib()

        // Cart
        if (session["cart"] == null) {
          session["cart"] = [:]
        }
        viewModel["cart"] = new CartViewModelBuilder().build(session["cart"])

        // Categories
        def categoriesViewModel = [:]
        def categories = Category.findAll();
        def categoryBuilder = new CategoryViewModelBuilder()
        for (category in categories) {
          categoriesViewModel[category.byname] = categoryBuilder.build(category)
        }
        viewModel["categories"] = categoriesViewModel

        // All products
        def productsViewModel = [:]
        def products = Product.list(sort:"dateCreated", order:"desc", max:24)
        def productBuilder = new ProductViewModelBuilder()
        for (product in products) {
          productsViewModel[product.byname] = productBuilder.build(product)
        }
        viewModel["products"] = productsViewModel

        // Pages
        def pagesViewModel = [:]
        def pages = Page.findAll();
        def pageBuilder = new PageViewModelBuilder()
        for (page in pages) {
          pagesViewModel[page.byname] = pageBuilder.build(page)
        }
        viewModel["pages"] = pagesViewModel

        // Links
        viewModel["links"] = [
          'home' : taglib.createLink(controller:'home', action:'expose')
         ,'all_products' : taglib.createLink(controller:'product', action:'all')
         ,'cart' : taglib.createLink(controller:'cart', action:'expose')
         ,'checkout' : taglib.createLink(controller:'checkout' /* , action:'expose' see http://jira.grails.org/browse/GRAILS-8945 */)
         ,'add_to_cart' : taglib.createLink(controller:'cart', action:'add')
         ,'remove_from_cart' : taglib.createLink(controller:'cart', action:'remove')
        ]

      }
    }
  }

}
