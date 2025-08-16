package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

import java.util.List;

/**
 * @author WyattLau
 * @date 2024/3/20
 */
@Data
public class BindForm {
    private Long userId;
    private List<Long> bind;
    private List<Long> cancel;
}
