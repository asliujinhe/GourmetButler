package cn.bwqsh.neu.homework.web

import cn.bwqsh.neu.homework.web.dao.ImageRepository
import cn.bwqsh.neu.homework.web.dao.ProductRepository
import cn.bwqsh.neu.homework.web.entity.Image
import cn.bwqsh.neu.homework.web.entity.Product
import cn.bwqsh.neu.homework.web.service.ProductService
import com.alibaba.fastjson.JSON
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlin.random.Random


@SpringBootApplication
class WebHomeworkApplication{
    //springboot启动后自动执行的方法



}

fun main(args: Array<String>) {
    runApplication<WebHomeworkApplication>(*args)
}
data class ProductP(
    val name: String,
    val price: Double,
    val description: String,
    val imgUrl: String,
    val url: String
)
data class Description(
    val description: String,
    val ingredients: String,
    val steps: String
)

@Component
class Test(val productService: ProductService, val imageRepository: ImageRepository, val myHttpClient: MyHttpClient){
    @PostConstruct
    fun insertProduct() {
        return
        val file = File("src/main/resources/test.json")
        val array = JSON.parseArray(file.readText(), ProductP::class.java)
        array.forEach {
            println(it)
            val image = imageRepository.save(
                Image(
                    name = it.name,
                    content = myHttpClient.getImage(it.imgUrl)!!
                )
            )
            val product = Product(
                name = it.name,
                price = Random(System.currentTimeMillis()).nextDouble(10.0, 100.0).toBigDecimal(),
                description = it.description,
                imageId = image.id,
            )
            productService.createProduct(product)
        }
    }
}


@Component
class MyHttpClient(restTemplateBuilder: RestTemplateBuilder) {
    @Throws(IOException::class, InterruptedException::class)
    fun getImage(url: String): ByteArray? {
        val httpClient: HttpClient = HttpClient.newHttpClient()
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response: HttpResponse<ByteArray> =
            httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())
        return response.body()
    }
}

