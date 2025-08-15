package fun.xianlai.admax.module.ledger.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fun.xianlai.admax.module.ledger.model.entity.Journal;
import fun.xianlai.admax.module.ledger.model.enums.AccountType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Wyatt
 * @date 2023/10/7
 */
@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    @Query("select distinct new Journal( " +
            "      j.id, " +
            "      j.userId, " +
            "      j.acctDate, " +
            "      j.type, " +
            "      j.amount, " +
            "      j.categoryId, " +
            "      j.channelId, " +
            "      j.description ) " +
            " from Journal j " +
            " where (j.userId = :userId) " +
            "      and (:stDate is null or j.acctDate >= :stDate) " +
            "      and (:edDate is null or j.acctDate < :edDate) " +
            "      and (:type is null or j.type = :type) " +
            "      and (coalesce(:categoryIds, null) is null or j.categoryId in (:categoryIds)) " +
            "      and (:channelId is null or j.channelId = :channelId) " +
            "      and (:description is null or j.description like %:description%)")
    Page<Journal> findConditionally(@Param("userId") Long userId,
                                    @Param("stDate") Date stDate,
                                    @Param("edDate") Date edDate,
                                    @Param("type") AccountType type,
                                    @Param("categoryIds") List<Long> categoryIds,
                                    @Param("channelId") Long channelId,
                                    @Param("description") String description,
                                    Pageable pageable);

    @Query("select sum(j.amount) " +
            " from Journal j" +
            " where (j.userId = :userId) " +
            "      and (:stDate is null or j.acctDate >= :stDate) " +
            "      and (:edDate is null or j.acctDate < :edDate) " +
            "      and (:type is null or j.type = :type) " +
            "      and (coalesce(:categoryIds, null) is null or j.categoryId in (:categoryIds)) " +
            "      and (:channelId is null or j.channelId = :channelId) " +
            "      and (:description is null or j.description like %:description%) " +
            "      and (j.type = :selectType)")
    BigDecimal sumDebitOrCreditAmountConditionally(@Param("userId") Long userId,
                                                   @Param("stDate") Date stDate,
                                                   @Param("edDate") Date edDate,
                                                   @Param("type") AccountType type,
                                                   @Param("categoryIds") List<Long> categoryIds,
                                                   @Param("channelId") Long channelId,
                                                   @Param("description") String description,
                                                   @Param("selectType")AccountType selectType);

    void deleteByIdAndUserId(Long id, Long userId);

    Optional<Journal> findByIdAndUserId(Long id, Long userId);

    @Transactional
    @Modifying
    @Query(value = "update tb_ledger_journal set channel_id = :newChannelId " +
            "where user_id = :userId and channel_id = :channelId", nativeQuery = true)
    void setChannelIdByUserIdAndChannelId(
            @Param("userId") Long userId,
            @Param("channelId") Long channelId,
            @Param("newChannelId") Long newChannelId);

    @Transactional
    @Modifying
    @Query(value = "update tb_ledger_journal set category_id = :newCategoryId " +
            "where user_id = :userId and category_id = :categoryId", nativeQuery = true)
    void setCategoryIdByUserIdAndCategoryId(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("newCategoryId") Long newCategoryId);
}
