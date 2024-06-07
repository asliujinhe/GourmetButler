package cn.bwqsh.neu.homework.web.security

import cn.bwqsh.neu.homework.web.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val user: User
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        // 返回用户的权限列表
        // 这里可以根据实际情况进行实现
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true // 帐户是否未过期
    }

    override fun isAccountNonLocked(): Boolean {
        return true // 帐户是否未锁定
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true // 凭证是否未过期
    }

    override fun isEnabled(): Boolean {
        return true // 是否启用
    }
}
