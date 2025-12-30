package com.ayush.habittracker.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ayush.habittracker.model.User;
import com.ayush.habittracker.repository.UserRepository;
import com.ayush.habittracker.security.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository userRepo;

	private String extractTokenFromCookie(HttpServletRequest req) {
		if(req.getCookies()==null)return null;
		
		for(Cookie cookie:req.getCookies()) {
			if("AUTH_TOKEN".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = extractTokenFromCookie(request);
		String email = null;

		// Check header exists + starts with "Bearer "
		if (token!=null) {
			try {
				email = jwtUtil.extractEmail(token);
			} catch (Exception e) {
				// invalid token → skip authentication
				return;
			}
		}

		// If email extracted & no existing authentication
		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			User user = userRepo.findByEmail(email);

			if (user != null && jwtUtil.validateToken(token, user)) {

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
						List.of(() -> user.getRole().name()));

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);

				// Add email to request for /auth/me
				request.setAttribute("email", email);
				Long userId = jwtUtil.extractClaim(token, claims -> claims.get("userId", Long.class));
				request.setAttribute("userId", userId);

			}
		}

		filterChain.doFilter(request, response);
	}
}
