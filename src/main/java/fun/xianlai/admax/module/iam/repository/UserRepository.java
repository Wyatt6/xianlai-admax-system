package fun.xianlai.admax.module.iam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.iam.model.entity.User;

import java.util.Date;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByPhone(String phone);

    User findByEmail(String email);

    @Query("select distinct new User(u.id, u.username, u.password, u.salt, u.phone, u.email, u.activated, u.createTime) " +
            " from User u " +
            "      left join UserRole ur on u.id = ur.userId " +
            "      left join Role r on ur.roleId = r.id " +
            " where (?1 is null or u.username like %?1%) " +
            "      and (?2 is null or u.phone like %?2%) " +
            "      and (?3 is null or u.email like %?3%) " +
            "      and (?4 is null or u.activated = ?4) " +
            "      and (?5 is null or u.createTime >= ?5) " +
            "      and (?6 is null or u.createTime <= ?6) " +
            "      and (?7 is null or r.identifier like %?7%)")
    Page<User> findConditionally(String username,
                                 String phone,
                                 String email,
                                 Integer activated,
                                 Date stTime,
                                 Date edTime,
                                 String role,
                                 Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_iam_user(id, username, password, salt, phone, email, activated, create_time) " +
            " values (:id, :username, :password, :salt, :phone, :email, :activated, :createTime)", nativeQuery = true)
    void insert(@Param("id") Long id,
                @Param("username") String username,
                @Param("password") String password,
                @Param("salt") String salt,
                @Param("phone") String phone,
                @Param("email") String email,
                @Param("activated") Integer activated,
                @Param("createTime") Date createTime);
}
