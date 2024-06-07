当使用Hibernate的JPA实现时，以下是一个完整的Services.kt文件示例，包含了ProductService、StyleService、UserService和LogService的定义，同时支持分页查询：

```kotlin
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository,
    private val styleRepository: StyleRepository,
    private val logService: LogService
) {

    fun getAllProducts(pageable: Pageable): Page<Product> {
        return productRepository.findAll(pageable)
    }

    fun searchProducts(
        name: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        startTime: LocalDateTime?,
        endTime: LocalDateTime?,
        pageable: Pageable
    ): Page<Product> {
        return productRepository.searchProducts(name, minPrice, maxPrice, startTime, endTime, pageable)
    }

    fun createProduct(product: Product) {
        productRepository.save(product)
        logService.logAction("创建商品", "创建了商品 ${product.name}")
    }

    fun updateProduct(product: Product) {
        productRepository.save(product)
        logService.logAction("更新商品", "更新了商品 ${product.name}")
    }

    fun deleteProduct(productId: Int) {
        val product = getProductById(productId)
        productRepository.delete(product)
        logService.logAction("删除商品", "删除了商品 ${product.name}")
    }

    fun getProductById(productId: Int): Product {
        return productRepository.findById(productId)
            .orElseThrow { RuntimeException("商品不存在") }
    }

    fun addStyleToProduct(productId: Int, style: Style) {
        val product = getProductById(productId)
        style.product = product
        styleRepository.save(style)
        logService.logAction("添加款式", "为商品 ${product.name} 添加了款式 ${style.name}")
    }

    // 其他商品相关的业务方法...

}

@Service
@Transactional
class StyleService(private val styleRepository: StyleRepository) {

    fun getAllStyles(): List<Style> {
        return styleRepository.findAll()
    }

    fun getStyleById(styleId: Int): Style {
        return styleRepository.findById(styleId)
            .orElseThrow { RuntimeException("款式不存在") }
    }

    fun createStyle(style: Style) {
        styleRepository.save(style)
    }

    fun updateStyle(style: Style) {
        styleRepository.save(style)
    }

    fun deleteStyle(styleId: Int) {
        val style = getStyleById(styleId)
        styleRepository.delete(style)
    }

    // 其他款式相关的业务方法...

}

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val logService: LogService
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(userId: Int): User {
        return userRepository.findById(userId)
            .orElseThrow { RuntimeException("用户不存在") }
    }

    fun createUser(user: User) {
        userRepository.save(user)
        logService.logAction("创建用户", "创建了用户 ${user.username}")
    }

    fun updateUser(user: User) {
        userRepository.save(user)
        logService.logAction("更新用户", "更新了用户 ${user.username}")
    }

    fun deleteUser(userId: Int) {
        val user = getUserById(userId)
        userRepository.delete(user)
        logService.logAction("删除用户", "删除了用户 ${user.username}")
    }

    // 其他用户相关的业务方法...

}

@Service
@Transactional
class LogService(private val logRepository:

 LogRepository, private val userRepository: UserRepository) {

    fun logAction(action: String, description: String) {
        val currentUser = getCurrentUser()
        val log = Log(user = currentUser, action = action, description = description)
        logRepository.save(log)
    }

    private fun getCurrentUser(): User {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication != null && authentication.isAuthenticated) {
            val principal = authentication.principal
            if (principal is UserPrincipal) {
                return principal.user
            }
        }
        throw RuntimeException("无法获取当前用户")
    }

    fun getAllLogs(pageable: Pageable): Page<Log> {
        return logRepository.findAll(pageable)
    }

    // 其他日志相关的业务方法...

}
```

上述示例中的Services.kt文件包含了ProductService、StyleService、UserService和LogService的定义，并支持分页查询。你可以根据你的实际需求进行调整和扩展这些服务类，以满足你的业务逻辑和分页查询需求。

请确保将相关的依赖项（如Repository和其他服务类）注入到相应的服务类中，并进行适当的配置，以使服务类能够正常工作。


当然！以下是一个完整的Controllers.kt示例，包含了ProductController、StyleController、CategoryController、UserController和LogController，同时实现了创建、更新、删除、查询（包括分页和排序）等功能：

```kotlin
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): ResponseEntity<List<Product>> {
        val sortedPageable = if (sortBy != null) pageable.withSort(Sort.by(sortBy)) else pageable
        val products = productService.getAllProducts(sortedPageable).content
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
        category

Service.createCategory(category)
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
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody user: User): ResponseEntity<Unit> {
        userService.registerUser(user)
        return ResponseEntity.created(URI("/api/users/${user.id}")).build()
    }

    // Add other user-related endpoints such as login, user profile, etc.
}

@RestController
@RequestMapping("/api/logs")
class LogController(private val logService: LogService) {

    @GetMapping
    fun getAllLogs(
        @RequestParam(required = false) sortBy: String?,
        pageable: Pageable
    ): ResponseEntity<List<Log>> {
        val sortedPageable = if (sortBy != null) pageable.withSort(Sort.by(sortBy)) else pageable
        val logs = logService.getAllLogs(sortedPageable).content
        return ResponseEntity(logs, HttpStatus.OK)
    }

    // Add other methods as needed.
}
```

在上述示例中，每个Controller包含了基本的增删改查方法，以及其他你可能需要的方法。还包含了分页查询和排序功能，你可以通过`getAllProducts()`和`getAllLogs()`方法中的`sortBy`参数指定排序字段。

请根据实际需求进行适当的调整和扩展，以满足你的业务需求。确保将相关的服务类（如ProductService、StyleService、CategoryService、UserService、LogService）注入到相应的Controller中，并进行适当的配置。