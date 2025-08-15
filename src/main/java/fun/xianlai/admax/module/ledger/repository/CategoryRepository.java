package fun.xianlai.admax.module.ledger.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.ledger.model.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * @author Wyatt
 * @date 2024/4/18
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    void deleteByIdAndUserId(Long id, Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    Optional<Category> findByUserIdAndName(Long userId, String name);

    List<Category> findByUserId(Long userId, Sort sort);

    List<Category> findByUserIdAndParentId(Long userId, Long parentId);

    List<Category> findByUserIdAndParentId(Long userId, Long parentId, Sort sort);

    @Query(value = "with recursive sub_tree(id, user_id, name, parent_id, activated, sort_id) as (" +
            "    select t0.id, t0.user_id, t0.name, t0.parent_id, t0.activated, t0.sort_id " +
            "    from tb_ledger_category t0 " +
            "    where t0.id = :rootId " +
            "    union all " +
            "    select t1.id, t1.user_id, t1.name, t1.parent_id, t1.activated, t1.sort_id " +
            "    from tb_ledger_category t1, sub_tree t " +
            "    where t1.parent_id = t.id " +
            ")" +
            "select * from sub_tree", nativeQuery = true)
    List<Category> findSubTreeNodes(@Param("rootId") Long rootId);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_ledger_category(id, user_id, name, parent_id, activated, sort_id) " +
            " values (:id, :userId, :name, :parentId, :activated, :sortId)", nativeQuery = true)
    void insert(@Param("id") Long id,
                @Param("userId") Long userId,
                @Param("name") String name,
                @Param("parentId") Long parentId,
                @Param("activated") Integer activated,
                @Param("sortId") Long sortId);
}
