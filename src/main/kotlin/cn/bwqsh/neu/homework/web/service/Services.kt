package cn.bwqsh.neu.homework.web.service
import cn.bwqsh.neu.homework.web.entity.*
import cn.bwqsh.neu.homework.web.dao.*
import cn.bwqsh.neu.homework.web.security.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("用户 '$username' 不存在")
        return UserPrincipal(user)
    }
}


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
        println(product)
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
class StyleService(private val styleRepository: StyleRepository, private val logService: LogService) {

    fun getAllStyles(): List<Style> {
        return styleRepository.findAll()
    }

    fun getStyleById(styleId: Int): Style {
        return styleRepository.findById(styleId)
            .orElseThrow { RuntimeException("款式不存在") }
    }

    fun createStyle(style: Style) {
        styleRepository.save(style)
        logService.logAction("创建款式", "创建了款式 ${style.name}")
    }

    fun updateStyle(style: Style) {
        styleRepository.save(style)
        logService.logAction("更新款式", "更新了款式 ${style.name}")
    }

    fun deleteStyle(styleId: Int) {
        val style = getStyleById(styleId)
        styleRepository.delete(style)
        logService.logAction("删除款式", "删除了款式 ${style.name}")
    }

    // 其他款式相关的业务方法...

}
@Service
@Transactional
class CategoryService(private val categoryRepository: CategoryRepository, private val logService: LogService) {

    fun getAllCategories(): List<Category> {
        return categoryRepository.findAll()
    }

    fun getCategoryById(categoryId: Int): Category {
        return categoryRepository.findById(categoryId)
            .orElseThrow { RuntimeException("分类不存在") }
    }

    fun createCategory(category: Category) {
        categoryRepository.save(category)
        logService.logAction("创建分类", "创建了分类 ${category.name}")
    }

    fun updateCategory(category: Category) {
        categoryRepository.save(category)
        logService.logAction("更新分类", "更新了分类 ${category.name}")
    }

    fun deleteCategory(categoryId: Int) {
        val category = getCategoryById(categoryId)
        categoryRepository.delete(category)
        logService.logAction("删除分类", "删除了分类 ${category.name}")
    }

    // 其他分类相关的业务方法...

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
class ImageService(private val imageRepository: ImageRepository,
                   private val productRepository: ProductRepository,
                   private val styleRepository: StyleRepository,
                   private val logService: LogService) {

    fun getImageById(imageId: Int): Image {
        return imageRepository.findById(imageId)
            .orElseThrow { RuntimeException("图片不存在") }
    }

    fun createImage(image: Image) {
        imageRepository.save(image)
        logService.logAction("创建图片", "创建了图片 ${image.name}")
    }

    fun getImageByProductId(productId: Int): Image? {
        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("商品不存在") }
        return getImageById(product.imageId!!)
    }

    fun getImageByStyleId(styleId: Int): Image? {
        val style = styleRepository.findById(styleId)
            .orElseThrow { RuntimeException("款式不存在") }
        return style.image
    }



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
        return User(username = "匿名用户")
    }

    fun getAllLogs(pageable: Pageable): Page<Log> {
        return logRepository.findAll(pageable)
    }

    // 其他日志相关的业务方法...

}

@Service
@Transactional
class CountService(private val productRepository: ProductRepository,
                   private val styleRepository: StyleRepository,
                   private val categoryRepository: CategoryRepository,
                   private val imageRepository: ImageRepository,
                   private val userRepository: UserRepository,
                   private val logRepository: LogRepository) {

    fun countProducts(): Long {
        return productRepository.count()
    }

    fun countStyles(): Long {
        return styleRepository.count()
    }

    fun countCategories(): Long {
        return categoryRepository.count()
    }

    fun countImages(): Long {
        return imageRepository.count()
    }

    fun countUsers(): Long {
        return userRepository.count()
    }

    fun countLogs(): Long {
        return logRepository.count()
    }

    fun counts(): Map<String, Long> {
        val counts = HashMap<String, Long>()
        counts["products"] = countProducts()
        counts["styles"] = countStyles()
        counts["categories"] = countCategories()
        counts["images"] = countImages()
        counts["users"] = countUsers()
        counts["logs"] = countLogs()
        return counts
    }

    // 其他统计相关的业务方法...

}