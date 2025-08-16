package fun.xianlai.admax.module.iam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.iam.model.entity.Permission;

import java.util.Date;
import java.util.List;

/**
 * @author WyattLau
 * @date 2024/2/4
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    @Query("select distinct new Permission(p.id, p.module, p.type, p.identifier, p.name, p.activated, p.createTime, p.remark) " +
            " from UserRole ur " +
            "      inner join Role r on ur.roleId = r.id and r.activated = 1 " +
            "      inner join RolePermission rp on ur.roleId = rp.roleId " +
            "      inner join Permission p on rp.permissionId = p.id and p.activated = 1 " +
            " where ur.userId = ?1")
    List<Permission> findActivatedByUserId(Long userId);

    @Query(value = "select num " +
            " from (select @rownum \\:= @rownum + 1 as num, p.id as id, p.module, p.type, p.identifier " +
            "      from tb_iam_permission p, (select @rownum \\:= 0) n " +
            "      order by p.module asc, p.type asc, p.identifier asc) t " +
            " where t.id = ?1", nativeQuery = true)
    Long findRowNumById(Long id);

    @Query("select p.id " +
            " from RolePermission rp " +
            "      inner join Permission p on rp.permissionId = p.id" +
            " where rp.roleId = ?1")
    List<Long> findIdsByRoleId(Long roleId);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_iam_permission(id, module, type, identifier, name, activated, create_time, remark) " +
            " values (:id, :module, :type, :identifier, :name, :activated, :createTime, :remark)", nativeQuery = true)
    void insert(@Param("id") Long id,
                @Param("module") String module,
                @Param("type") Integer type,
                @Param("identifier") String identifier,
                @Param("name") String name,
                @Param("activated") Integer activated,
                @Param("createTime") Date createTime,
                @Param("remark") String remark);
}
