package cn.bwqsh.neu.homework.web.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)
