package io.github.tonyzzx.gspan.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

//构造图，不断探索能否加边
public class History extends ArrayList<Edge> {
    private static final long serialVersionUID = 1L;
    private Vector<Integer> edge;
    private Vector<Integer> vertex;

    public History(Graph g, PDFS p) {
        edge = new Vector<>();
        vertex = new Vector<>();
        build(g, p);
    }

    private void build(Graph graph, PDFS e) {//给图graph增加一条边e
        // first build history重置边的尺寸和点的尺寸为预先给定的这个图的尺寸
        clear();
        edge.clear();
        edge.setSize(graph.edge_size);
        vertex.clear();
        vertex.setSize(graph.size());//图的尺寸就是点的个数
//取边，当前边的处理，以及化为当前边的处理方法
        if (e != null) {//e代表新边
            add(e.edge);//增加一条新的边，那么就要重新设置边及两个点
            edge.set(e.edge.id, 1);
            vertex.set(e.edge.from, 1);
            vertex.set(e.edge.to, 1);

            for (PDFS p = e.prev; p != null; p = p.prev) {//p为按时间顺序在e之前的边，逐渐往前推
                add(p.edge); // this line eats 8% of overall instructions(!)
                edge.set(p.edge.id, 1);//重置p这条边的相关信息
                vertex.set(p.edge.from, 1);
                vertex.set(p.edge.to, 1);
            }
            Collections.reverse(this);//反向收集，组成一个图
        }
    }

    public boolean hasEdge(int id) { //判断是否有边
        if (edge.get(id) != null) {
            return edge.get(id) != 0;
        }
        else
            return  false;
    }

    public boolean hasVertex(int id) { //判断是否有点
        if (vertex.get(id) != null) {
            return vertex.get(id) != 0;
        }
        return false;
    }
}