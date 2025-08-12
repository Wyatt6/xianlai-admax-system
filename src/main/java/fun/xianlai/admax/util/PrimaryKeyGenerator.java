package fun.xianlai.admax.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * 主键ID生成器
 * 使用FakeSnowflakeUtil中定义的伪雪花算法
 *
 * @author Wyatt6
 * @date 2025/8/12
 */
public class PrimaryKeyGenerator implements IdentifierGenerator {
    private final FakeSnowflakeUtil fakeSnowflakeUtils = new FakeSnowflakeUtil();

    synchronized public long next() {
        return fakeSnowflakeUtils.nextId();
    }


    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        return IdentifierGenerator.super.generate(session, owner, currentValue, eventType);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return next();
    }
}
