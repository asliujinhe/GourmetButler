package cn.bwqsh.neu.homework.web.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.LocalDateTime
import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var name: String = "undefined",
    var content: ByteArray = ByteArray(0)
) {
    @JsonIgnore
    fun getBase64(): String {
        return Base64.getEncoder().encodeToString(content)
    }
    fun fromBase64(base64: String) {
        content = Base64.getDecoder().decode(base64)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (name != other.name) return false
        return content.contentEquals(other.content)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + content.contentHashCode()
        return result
    }

}

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var name: String,
    var description: String?,
    var price: BigDecimal,
    var imageId: Int? = null,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var modifiedAt: LocalDateTime = LocalDateTime.now(),
    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    val categories: Set<Category> = emptySet()
)

@Entity
data class Style(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var name: String,
    @JsonIgnore
    @OneToOne
    var image: Image? = Image(),
    var description: String?,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product
)

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = 0,
    val username: String,
    var password: String = "",
    var email: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
):Serializable

@Entity
data class Log(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,
    val action: String,
    val description: String?,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var name: String,
    @JsonIgnore
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    val products: Set<Product> = emptySet()
)

@Entity
@Table(
    name = "product_category",
    uniqueConstraints = [UniqueConstraint(columnNames = ["product_id", "category_id"])]
)
data class ProductCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,
    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category
)
