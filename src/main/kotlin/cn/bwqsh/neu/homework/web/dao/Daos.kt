package cn.bwqsh.neu.homework.web.dao

import cn.bwqsh.neu.homework.web.entity.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal
import java.time.LocalDateTime

interface ProductRepository : JpaRepository<Product, Int> {
    // 分页查询商品列表
    //fun findAll(pageable: Pageable): Page<Product>
    // 组合查询商品列表
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:startTime IS NULL OR p.createdAt >= :startTime) " +
            "AND (:endTime IS NULL OR p.createdAt <= :endTime)")
    fun searchProducts(name: String?, minPrice: BigDecimal?, maxPrice: BigDecimal?,
                       startTime: LocalDateTime?, endTime: LocalDateTime?, pageable: Pageable): Page<Product>
}

interface StyleRepository : JpaRepository<Style, Int>

interface UserRepository : JpaRepository<User, Int> {
    // 根据用户名查询用户
    fun findByUsername(username: String): User?
}

interface LogRepository : JpaRepository<Log, Int> {
    // 查询指定用户的日志列表
    fun findByUser(user: User): List<Log>
}

interface CategoryRepository : JpaRepository<Category, Int>

interface ImageRepository : JpaRepository<Image, Int>