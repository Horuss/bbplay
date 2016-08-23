package pl.horuss.bbplay.web.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.shared.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;
import org.vaadin.spring.security.shared.VaadinUrlAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;

import pl.horuss.bbplay.web.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
@EnableVaadinSharedSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String REMEMBERME_KEY = "bbplays-key";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserService userService;

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

		// auth.inMemoryAuthentication().withUser("user").password("user").roles("USER").and()
		// .withUser("admin").password("admin").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); // Use Vaadin's built-in CSRF protection instead
		http.authorizeRequests().antMatchers("/login/**").anonymous()
				.antMatchers("/vaadinServlet/UIDL/**").permitAll()
				.antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll().anyRequest()
				.authenticated();
		http.httpBasic().disable();
		http.formLogin().disable();
		http.logout().addLogoutHandler(new VaadinSessionClosingLogoutHandler())
				.logoutUrl("/logout").logoutSuccessUrl("/login?logout").permitAll();
		http.exceptionHandling().authenticationEntryPoint(
				new LoginUrlAuthenticationEntryPoint("/login"));
		// Spring Security must use the same RememberMeServices and
		// authentication strategy as Vaadin4Spring
		http.rememberMe().rememberMeServices(rememberMeServices()).key(REMEMBERME_KEY);
		http.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/VAADIN/**");
	}

	/**
	 * The {@link AuthenticationManager} must be available as a Spring bean for
	 * Vaadin4Spring.
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * The {@link RememberMeServices} must be available as a Spring bean for
	 * Vaadin4Spring.
	 */
	@Bean
	public RememberMeServices rememberMeServices() {
		return new TokenBasedRememberMeServices(REMEMBERME_KEY, userDetailsService());
	}

	/**
	 * The {@link SessionAuthenticationStrategy} must be available as a Spring
	 * bean for Vaadin4Spring.
	 */
	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new SessionFixationProtectionStrategy();
	}

	@Bean(name = VaadinSharedSecurityConfiguration.VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
	VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(HttpService httpService,
			VaadinRedirectStrategy vaadinRedirectStrategy) {
		return new VaadinUrlAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	// util for manually generating passwords
	public static void main(String[] args) {
		String password = "someNewPassword";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode(password));
	}

}