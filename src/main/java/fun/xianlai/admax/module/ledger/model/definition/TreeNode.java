package fun.xianlai.admax.module.ledger.model.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * @author Wyatt
 * @date 2023/10/8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<ID, T> {
    private ID id;  // 唯一标识，一般是data.id
    private T data;
    private ArrayList<TreeNode<ID, T>> children;
    private long count = 1;

    public TreeNode(ID id, T data) {
        this.id = id;
        this.data = data;
    }

    public TreeNode(ID id, ArrayList<TreeNode<ID, T>> children) {
        this.id = id;
        this.children = children;
    }

    public TreeNode(ID id, T data, ArrayList<TreeNode<ID, T>> children) {
        this.id = id;
        this.data = data;
        this.children = children;
    }

    public ArrayList<TreeNode<ID, T>> accessChildrenInstance() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public long countNodes() {
        if (children != null) {
            for (TreeNode<ID, T> child : children) {
                count += child.countNodes();
            }
        }
        return count;
    }
}
