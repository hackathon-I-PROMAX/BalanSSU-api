package com.yourssu.balanssu.core.security

import org.springframework.core.MethodParameter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserInfoArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter) =
        parameter.getParameterAnnotation(Authenticated::class.java) != null &&
                parameter.parameterType == UserInfo::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authenticationToken = SecurityContextHolder.getContext().authentication
        return UserInfo(
            authenticationToken.principal.toString(),
            mapRolesFromAuthorities(authenticationToken.authorities)
        )
    }

    private fun mapRolesFromAuthorities(authorities: MutableCollection<out GrantedAuthority>) =
        authorities.map { UserRole.valueOf(it.authority) }.toList()
}
