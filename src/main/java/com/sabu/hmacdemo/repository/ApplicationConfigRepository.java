package com.sabu.hmacdemo.repository;

import com.sabu.hmacdemo.entities.ApplicationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
@Repository
public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, Long> {

    @Query(value = "SELECT * FROM APPLICATION_CONFIG AC WHERE AC.CONFIG_KEY=:configKey",nativeQuery = true)
    ApplicationConfig findByConfigKey(@Param("configKey") String configKey);
}
