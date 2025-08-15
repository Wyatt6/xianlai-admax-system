package fun.xianlai.admax.module.ledger.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.ledger.model.entity.Channel;

import java.util.List;
import java.util.Optional;

/**
 * @author Wyatt
 * @date 2024/4/17
 */
@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByUserId(Long userId, Sort sort);

    Optional<Channel> findByIdAndUserId(Long id, Long userId);

    Optional<Channel> findByUserIdAndName(Long userId, String name);

    void deleteByIdAndUserId(Long id, Long userId);

    @Transactional
    @Modifying
    @Query(value = "insert into tb_ledger_channel(id, user_id, name, activated, sort_id) " +
            " values (:id, :userId, :name, :activated, :sortId)", nativeQuery = true)
    void insert(@Param("id") Long id,
                @Param("userId") Long userId,
                @Param("name") String name,
                @Param("activated") Integer activated,
                @Param("sortId") Long sortId);
}
