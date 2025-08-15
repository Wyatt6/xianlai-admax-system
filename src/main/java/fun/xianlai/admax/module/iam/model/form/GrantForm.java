package fun.xianlai.admax.module.iam.model.form;

import lombok.Data;

import java.util.List;

/**
 * @author Wyatt
 * @date 2024/3/16
 */
@Data
public class GrantForm {
    private Long roleId;
    private List<Long> grant;
    private List<Long> cancel;
}
