package fun.xianlai.admax.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * 主键ID生成器
 * 使用FakeSnowflakeUtil中定义的伪雪花算法
 *
 * @author WyattLau
 * @date 2024/1/30
 */
public class PrimaryKeyGenerator extends IdentityGenerator {
    private final FakeSnowflakeUtil fakeSnowflakeUtils = new FakeSnowflakeUtil();

    synchronized public long next() {
        return fakeSnowflakeUtils.nextId();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return next();
    }
}
