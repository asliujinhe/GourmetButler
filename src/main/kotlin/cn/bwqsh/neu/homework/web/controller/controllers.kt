package cn.bwqsh.neu.homework.web.controller

import cn.bwqsh.neu.homework.web.dto.*
import cn.bwqsh.neu.homework.web.entity.*
import cn.bwqsh.neu.homework.web.service.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.net.URI
import java.time.LocalDateTime

@Controller
class LoginController {
    @GetMapping("/login")
    fun login(@RequestParam(defaultValue = "") error: String, model: Model): String {
        model.addAttribute("error", error)
        return "login"
    }
}

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): ResponseEntity<Page<Product>> {
        val sortedPageable = if (sortBy != null) {
            val list = sortBy.split(",").let {
                it.map { map->map.split(":") }.map { i->
                    Sort.Order(Sort.Direction.fromString(i[1]),i[0])
                }
            }
            val sort = Sort.by(sortBy)
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        } else {
            pageable
        }
        val products = productService.getAllProducts(sortedPageable)
        return ResponseEntity(products, HttpStatus.OK)
    }
    @GetMapping("/search")
    fun searchProducts(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) minPrice: BigDecimal?,
        @RequestParam(required = false) maxPrice: BigDecimal?,
        @RequestParam(required = false) startTime: LocalDateTime?,
        @RequestParam(required = false) endTime: LocalDateTime?,
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): ResponseEntity<Page<Product>> {
        val sortedPageable = if (sortBy != null && sortBy != "") {
            println(sortBy)
            val list = buildList {
                sortBy.split(",").forEach {
//                    println(it)
                    if(it.isNotEmpty())it.split(":").let {sort->
                        add(Sort.Order(Sort.Direction.fromString(sort[1]),sort[0]))
                    }
                }
            }
            val sort = Sort.by(list)
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        } else {
            pageable
        }
        var searchName = name
        if(searchName.isNullOrEmpty())searchName = null
        val products = productService.searchProducts(searchName, minPrice, maxPrice, startTime, endTime, sortedPageable)
        return ResponseEntity(products, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Int): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return ResponseEntity(product, HttpStatus.OK)
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Unit> {
        productService.createProduct(product)
        return ResponseEntity.created(URI("/api/products/${product.id}")).build()
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Int, @RequestBody product: Product): ResponseEntity<Unit> {
        val existingProduct = productService.getProductById(id)
        product.id = existingProduct.id // Ensure the correct ID is set
        println(product.imageId)
        if (product.imageId == null) {
            product.imageId = existingProduct.imageId
        }
        product.modifiedAt = LocalDateTime.now()
        product.createdAt = existingProduct.createdAt
        productService.updateProduct(product)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Int): ResponseEntity<Unit> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }

    // Add other methods such as searchProducts, addStyleToProduct, etc.
}

@RestController
@RequestMapping("/api/styles")
class StyleController(private val styleService: StyleService) {

    @GetMapping
    fun getAllStyles(): ResponseEntity<List<Style>> {
        val styles = styleService.getAllStyles()
        return ResponseEntity(styles, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getStyleById(@PathVariable id: Int): ResponseEntity<Style> {
        val style = styleService.getStyleById(id)
        return ResponseEntity(style, HttpStatus.OK)
    }

    @PostMapping
    fun createStyle(@RequestBody style: Style): ResponseEntity<Unit> {
        styleService.createStyle(style)
        return ResponseEntity.created(URI("/api/styles/${style.id}")).build()
    }

    @PutMapping("/{id}")
    fun updateStyle(@PathVariable id: Int, @RequestBody style: Style): ResponseEntity<Unit> {
        val existingStyle = styleService.getStyleById(id)
        style.id = existingStyle.id // Ensure the correct ID is set
        styleService.updateStyle(style)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteStyle(@PathVariable id: Int): ResponseEntity<Unit> {
        styleService.deleteStyle(id)
        return ResponseEntity.noContent().build()
    }

    // Add other methods as needed.
}

@RestController
@RequestMapping("/api/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<Category>> {
        val categories = categoryService.getAllCategories()
        return ResponseEntity(categories, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Int): ResponseEntity<Category> {
        val category = categoryService.getCategoryById(id)
        return ResponseEntity(category, HttpStatus.OK)
    }

    @PostMapping
    fun createCategory(@RequestBody category: Category): ResponseEntity<Unit> {
        categoryService.createCategory(category)
        return ResponseEntity.created(URI("/api/categories/${category.id}")).build()
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Int, @RequestBody category: Category): ResponseEntity<Unit> {
        val existingCategory = categoryService.getCategoryById(id)
        category.id = existingCategory.id // Ensure the correct ID is set
        categoryService.updateCategory(category)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Int): ResponseEntity<Unit> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }

    // Add other methods as needed.
}

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<Unit> {
        val encodedPassword = passwordEncoder.encode(user.password)
        user.password = encodedPassword
        userService.createUser(user)
        return ResponseEntity.created(URI("/api/users/${user.id}")).build()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Int, @RequestBody user: User): ResponseEntity<Unit> {
        val existingUser = userService.getUserById(id)
        user.id = existingUser.id // Ensure the correct ID is set
        userService.updateUser(user)
        return ResponseEntity.noContent().build()
    }

    // Add other user-related endpoints as needed.
}


@RestController
@RequestMapping("/api/images")
class ImageController(private val imageService: ImageService){
    @GetMapping("/{id}")
    fun getImageById(@PathVariable id: Int): ResponseEntity<Image> {
        val image = imageService.getImageById(id)
        return ResponseEntity(image, HttpStatus.OK)
    }
    @PostMapping
    fun createImage(@RequestBody image: Image): ResponseEntity<Int> {
        imageService.createImage(image)
        return ResponseEntity(image.id, HttpStatus.CREATED)
    }
    @GetMapping("/product/{id}")
    fun getImageByProductId(@PathVariable id: Int): ResponseEntity<Image> {
        val image = imageService.getImageByProductId(id)
        return ResponseEntity(image, HttpStatus.OK)
    }
    @GetMapping("/style/{id}")
    fun getImageByStyleId(@PathVariable id: Int): ResponseEntity<Image> {
        val image = imageService.getImageByStyleId(id)
        return ResponseEntity(image, HttpStatus.OK)
    }

}


@RestController
@RequestMapping("/api/logs")
class LogController(private val logService: LogService) {

    @GetMapping
    fun getAllLogs(
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): ResponseEntity<Page<Log>> {
        val sortedPageable = if (sortBy != null) {
            val sort = Sort.by(sortBy)
            PageRequest.of(pageable.pageNumber, pageable.pageSize, sort)
        } else {
            pageable
        }
        val logs = logService.getAllLogs(sortedPageable)
        return ResponseEntity(logs, HttpStatus.OK)
    }


    // Add other methods as needed.
}

@RestController
@RequestMapping("/api/count")
class CountController(private val countService: CountService) {
    @GetMapping
    fun getCount(): ResponseEntity<Map<String,Long>> {
        val count = countService.counts()
        return ResponseEntity(count, HttpStatus.OK)
    }
}