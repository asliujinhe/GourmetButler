package cn.bwqsh.neu.homework.web

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


fun main() {
    println(BCryptPasswordEncoder().encode("123456"))
}