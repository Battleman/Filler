package bcn.hackupc.filler.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, bcn.hackupc.filler.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, bcn.hackupc.filler.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, bcn.hackupc.filler.domain.User.class.getName());
            createCache(cm, bcn.hackupc.filler.domain.Authority.class.getName());
            createCache(cm, bcn.hackupc.filler.domain.User.class.getName() + ".authorities");
            createCache(cm, bcn.hackupc.filler.domain.CustomEvent.class.getName());
            createCache(cm, bcn.hackupc.filler.domain.CustomEvent.class.getName() + ".preferences");
            createCache(cm, bcn.hackupc.filler.domain.Preference.class.getName());
            createCache(cm, bcn.hackupc.filler.domain.Preference.class.getName() + ".preferenceCategories");
            createCache(cm, bcn.hackupc.filler.domain.PreferenceCategory.class.getName());
            createCache(cm, bcn.hackupc.filler.domain.Preference.class.getName() + ".users");
            createCache(cm, bcn.hackupc.filler.domain.Preference.class.getName() + ".customEvents");
            createCache(cm, bcn.hackupc.filler.domain.CustomEvent.class.getName() + ".users");
            createCache(cm, bcn.hackupc.filler.domain.PreferenceCategory.class.getName() + ".preferences");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
