package io.github.tonyzzx.gspan;

import java.util.ArrayList;  // 动态数组
import io.github.tonyzzx.gspan.model.Edge;
import io.github.tonyzzx.gspan.model.Graph;
import io.github.tonyzzx.gspan.model.History;
import io.github.tonyzzx.gspan.model.Vertex;
public class Misc {
    public static boolean getForwardRoot(Graph g, Vertex v, ArrayList<Edge> result) {//是否的得到前向跟
        //布尔型的函数，子图g,节点v,结果图的表示形式，表示能不能往前探索
        result.clear();
        for (Edge it : v.edge) {
            assert (it.to >= 0 && it.to < g.size());
            if (v.label <= g.get(it.to).label)
                result.add(it);//it就是DFS码
        }
        return !result.isEmpty();
    }
    public static Edge getBackward(Graph graph, Edge e1, Edge e2, History history) {//后向边
        if (e1 == e2)
            return null;
        assert (e1.from >= 0 && e1.from < graph.size());
        assert (e1.to >= 0 && e1.to < graph.size());
        assert (e2.to >= 0 && e2.to < graph.size());
        for (Edge it : graph.get(e2.to).edge) {
            if (history.hasEdge(it.id))
                continue;

            if ((it.to == e1.from) && ((e1.eLabel < it.eLabel)
                    || (e1.eLabel == it.eLabel) && (graph.get(e1.to).label <= graph.get(e2.to).label))) {
                return it;
            }
        }
        return null;
    }
    //前向路径，判断能否继续向前增加边
    public static boolean getForwardPure(Graph graph, Edge e, int minLabel, History history, ArrayList<Edge> result) {
        result.clear();
        assert (e.to >= 0 && e.to < graph.size());
        for (Edge it : graph.get(e.to).edge) {
            // -e. [e.to] -it. [it.to]
            assert (it.to >= 0 && it.to < graph.size());
            if (minLabel > graph.get(it.to).label || history.hasVertex(it.to))
                continue;
            result.add(it);
        }
        return !result.isEmpty();
    }
    public static boolean getForwardRmPath(Graph graph, Edge e, int minLabel, History history, ArrayList<Edge> result) {
        result.clear();
        assert (e.to >= 0 && e.to < graph.size());
        assert (e.from >= 0 && e.from < graph.size());
        int toLabel = graph.get(e.to).label;
        for (Edge it : graph.get(e.from).edge) {
            int toLabel2 = graph.get(it.to).label;
            if (e.to == it.to || minLabel > toLabel2 || history.hasVertex(it.to))
                continue;
            if (e.eLabel < it.eLabel || (e.eLabel == it.eLabel && toLabel <= toLabel2))
                result.add(it);
        }
        return !result.isEmpty();
    }
}