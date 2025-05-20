package capstoneds2.creditcard_module;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "capstoneds2.creditcard_module.Repository",
        entityManagerFactoryRef = "creditCardEntityManagerFactory",
        transactionManagerRef = "creditCardTransactionManager"
)
public class CreditCardModuleConfig {

    @Primary
    @Profile("!test")
    @Bean(name = "creditCardDataSource")
    public DataSource creditCardDataSource() {
        HikariConfig config = new HikariConfig();
        String host = System.getenv().getOrDefault("DB_HOST", "localhost");
        String port = System.getenv().getOrDefault("DB_PORT", "3306");
        String db   = System.getenv().getOrDefault("DB_NAME", "card_dev");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String pass = System.getenv().getOrDefault("DB_PASS", "root");

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db + "?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC");
        config.setUsername(user);
        config.setPassword(pass);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPoolName("CreditCardHikariPool");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        return new HikariDataSource(config);
    }
    @Primary
    @Profile("!test")
    @Bean(name = "creditCardEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean creditCardEntityManagerFactory(
            @Qualifier("creditCardDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("capstoneds2.creditcard_module.Model");
        em.setPersistenceUnitName("creditCardPU");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "none");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.jdbc.time_zone", "UTC");
        em.setJpaProperties(props);

        return em;
    }

    @Primary
    @Profile("!test")
    @Bean(name = "creditCardTransactionManager")
    public PlatformTransactionManager creditCardTransactionManager(
            @Qualifier("creditCardEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}