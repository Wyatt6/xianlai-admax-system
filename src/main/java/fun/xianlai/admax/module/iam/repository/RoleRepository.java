package fun.xianlai.admax.module.iam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.iam.model.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select distinct new Role(r.id, r.identifier, r.name, r.activated, r.sortId, r.createTime, r.remark) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.activated = 1 " +
            " where ur.userId = ?1")
    List<Role> findActivatedByUserId(Long userId);

    @Query("select distinct new Role(r.id, r.identifier, r.name, r.activated, r.sortId, r.createTime, r.remark) " +
            " from Role r " +
            "      left join RolePermission rp on r.id = rp.roleId " +
            "      left join Permission p on rp.permissionId = p.id " +
            " where (?1 is null or r.identifier like %?1%) " +
            "      and (?2 is null or r.name like %?2%) " +
            "      and (?3 is null or r.activated = ?3) " +
            "      and (?4 is null or r.createTime >= ?4) " +
            "      and (?5 is null or r.createTime <= ?5) " +
            "      and (?6 is null or p.identifier like %?6%)")
    Page<Role> findConditionally(String identifier,
                                 String name,
                                 Integer activated,
                                 Date stTime,
                                 Date edTime,
                                 String permission,
                                 Pageable pageable);

    @Query("select r.id " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id" +
            " where ur.userId = ?1")
    List<Long> findIdsByUserId(Long userId);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, r.id as id, r.sort_id " +
            "      from tb_iam_role r, (select @rownum \\:= 0) n " +
            "      order by r.sort_id asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_iam_role(id, identifier, name, activated, sort_id, create_time, remark) " +
            " values (:id, :identifier, :name, :activated, :sortId, :createTime, :remark)", nativeQuery = true)
    void insert(@Param("id") Long id,
                @Param("identifier") String identifier,
                @Param("name") String name,
                @Param("activated") Integer activated,
                @Param("sortId") Long sortId,
                @Param("createTime") Date createTime,
                @Param("remark") String remark);
}
