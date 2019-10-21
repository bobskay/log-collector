package wang.wangby.web.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.tree.TreeNode;

import java.util.*;
import java.util.function.Function;

@Data
@Remark("菜单数据")
public class MenuInfo implements TreeNode {

    @Remark("节点唯一标识")
    private String id;
    @Remark("下级节点")
    private List<MenuInfo> nodes;
    @Remark("显示文本")
    private String text;
    @Remark("url")
    private String url;
    @Remark("上级节点id")
    private String parentId;
    @Remark("显示序号")
    private Integer index;
    @Remark("显示图标")
    private String icon;
    @Remark("节点状态")
    //expanded 是否展开
    private Map<String,String>  state=new HashMap<>();

    public void setExpanded(boolean bool){
        state.put("expanded",bool+"");
    }

    public static MenuInfo newInstance(String id, String text, String url,String icon) {
        MenuInfo view = new MenuInfo();
        view.id = id;
        view.text = text;
        view.url = url;
        view.icon=icon;
        return view;
    }

    public void addNode(String id, String text, String url,String icon) {
        if(nodes==null){
            nodes=new ArrayList<>();
        }
        MenuInfo node = newInstance(id, text, url,icon);
        this.nodes.add(node);
    }

    public static <T> List<MenuInfo> createTrees(List<T> list, Function<T, List<MenuInfo>> toBootstrapTreeView) {
        Map<String, MenuInfo> all = new TreeMap<>();
        //将对象转为BootstrapTreeView并放入map
        for (T t : list) {
            List<MenuInfo> views = toBootstrapTreeView.apply(t);
            for(MenuInfo view:views){
                if (view.index == null) {
                    view.index = 0;
                }
                all.put(view.id, view);
            }
        }
        return formate(all);
    }

    //将list组装成树,并排序
    private static List<MenuInfo> formate(Map<String, MenuInfo> all) {
        List<MenuInfo> roots = new ArrayList();
        //按上下级组装成树,没有上级的当做跟节点
        for (MenuInfo view : all.values()) {
            MenuInfo parent = all.get(view.getParentId());
            if (parent == null || parent == view) {
                roots.add(view);
                continue;
            }
            if (parent.nodes == null) {
                parent.nodes = new ArrayList<>();
            }
            parent.nodes.add(view);
        }
        //排序
        Comparator<MenuInfo> comparator=(v1, v2)->{
            return v1.index - v2.index;
        };
        for (MenuInfo view : all.values()) {
            if (view.getNodes() != null) {
                Collections.sort(view.getNodes(),comparator);
            }
        }
        Collections.sort(roots,comparator);
        return roots;
    }

    public static <T> List<MenuInfo> createTree(List<T> list, Function<T, MenuInfo> toBootstrapTreeView) {
        Map<String, MenuInfo> all = new TreeMap<>();
        //将对象转为BootstrapTreeView并放入map
        for (T t : list) {
            MenuInfo view = toBootstrapTreeView.apply(t);
            if (view.index == null) {
                view.index = 0;
            }
            all.put(view.id, view);
        }
        return formate(all);
    }


    @Override
    public List getChildren() {
       return nodes;
    }

    @Override
    public void setChildren(List children) {
    this.nodes=children;
    }
}
