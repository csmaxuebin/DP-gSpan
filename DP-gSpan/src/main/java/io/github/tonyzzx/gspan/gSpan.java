package io.github.tonyzzx.gspan;

import java.io.BufferedReader;  // 用BufferedReader类读写文件
import java.io.FileReader;  // 读入写出文件
import java.io.FileWriter;
import java.io.IOException;  // 检测输入输出异常
import java.math.BigDecimal;  // 精确计算浮点数
import java.util.*;
import java.util.Map.Entry;  // 在遍历map的时候用到
import java.util.regex.Matcher;  // Matcher 对象是对输入字符串进行解释和匹配操作的引擎
import java.util.regex.Pattern;  // pattern 对象是一个正则表达式的编译表示

import io.github.tonyzzx.gspan.model.DFSCode;
import io.github.tonyzzx.gspan.model.Edge;
import io.github.tonyzzx.gspan.model.Graph;
import io.github.tonyzzx.gspan.model.History;
import io.github.tonyzzx.gspan.model.PDFS;
import io.github.tonyzzx.gspan.model.Projected;
import io.github.tonyzzx.gspan.model.Vertex;

public class gSpan {
    private ArrayList<Graph> TRANS;
    private DFSCode DFS_CODE;
    private DFSCode DFS_CODE_IS_MIN;
    private Graph GRAPH_IS_MIN;
    private long ID;
    private double minSup;
    private long maxPat_min;
    private long maxPat_max;
    private boolean directed;
    private FileWriter os;
    int zid=0;
    // Singular vertex handling stuff [graph][vertexLabel] = count.
    private NavigableMap<Integer, NavigableMap<Integer, Integer>> singleVertex;
    private NavigableMap<Integer, Integer> singleVertexLabel;

    public gSpan() {
        TRANS = new ArrayList<>();
        DFS_CODE = new DFSCode();
        DFS_CODE_IS_MIN = new DFSCode();
        GRAPH_IS_MIN = new Graph();
        singleVertex = new TreeMap<>();
        singleVertexLabel = new TreeMap<>();
    }

    void run(FileReader reader, FileWriter writers, double minSup, long maxNodeNum, long minNodeNum) throws IOException {
        os = writers;
        ID = 0;
        this.minSup = minSup;
        maxPat_min = minNodeNum;
        maxPat_max = maxNodeNum;
        directed = false;

        read(reader);
        runIntern();
        //long startTime = System.currentTimeMillis();    //获取开始时间
        read1();
        //long endTime = System.currentTimeMillis();    //获取结束时间
        //System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }

    private void read(FileReader is) throws IOException {
        BufferedReader read = new BufferedReader(is);
        while (true) {
            Graph g = new Graph(directed);
            read = g.read(read);
            if (g.isEmpty())
                break;
            TRANS.add(g);
        }
        read.close();
    }

    //BufferedReader
    private void read1() throws IOException {
        FileReader fr = new FileReader("D:\\data\\yjs\\work\\githubcode\\邢江娜-一种基于模式增长的差分隐私频繁子图挖掘算法\\代码\\Algorithm1\\test_result");
        BufferedReader bfr = new BufferedReader(fr);
        //BufferedReader writer = new BufferedReader(is1);
        ArrayList<String> test_result = new ArrayList<>();

        String line;
        int count1 = 0;
        int count2 = 0;
        int[] str = new int[32257];
        HashMap<Integer, Integer> hm1 = new HashMap<Integer, Integer>();//创建对象，id,Ci
        HashMap<Integer, Double> hm2 = new HashMap<Integer, Double>();//创建对象，id,nsup
        LinkedHashMap<Integer, Double> hm3 = new LinkedHashMap<>();//创建对象，id,sup
        LinkedHashMap<Integer, Double> hm3new = new LinkedHashMap<>();//创建对象，id,sup
        LinkedHashMap<Integer, Double> hm3new3 = new LinkedHashMap<>();//创建对象，id,sup
        Integer ID1 = 0;
        Double sup = 0.0;
        int I = 0;
        double nsup = 0.0;
        String strid = "";
        String strid2 = "";

        while ((line = bfr.readLine()) != null) {
            test_result.clear();
            String[] splitRead = line.split(" ");
            Collections.addAll(test_result, splitRead);

            if (test_result.get(0).equals("t")) {
                count1++;
                //System.out.println("-------------------------------------");
                count2 = 0;
                String regex = "#[0-9]*\\*";
                String regex2 = "(\\d+\\.\\d)";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(line);
                Pattern p2 = Pattern.compile(regex2);
                Matcher m2 = p2.matcher(line);
                //System.out.println(line);
                //System.out.println(m.find());
                while (m.find()) {
                    //System.out.println(m.group().length());//使用匹配器获取匹配到的字符
                    //ID = Integer.parseInt(m.group());//把噪声支持度转变为double类型
                    for (int i = 0; i < m.group().length(); i++) {//3,4
                        if (m.group().charAt(i) == '*') {
                            //System.out.println(strid);
                            ID1 = Integer.parseInt(strid);
                            strid = "";
                            // strid2="";
                        }
                        if (m.group().charAt(i) >= 48 && m.group().charAt(i) <= 57) {
                            strid += (m.group().toString().charAt(i));
                            //System.out.print(m.group().charAt(i));
                        }
                    }
                    while (m2.find()) {
                        //System.out.println(m.group());
                        sup = Double.parseDouble(m2.group());//把噪声支持度转变为double类型
                        //System.out.println(nsup);
                    }
                    //System.out.print(m.group().charAt(i));
                }
            } else if (test_result.get(0).equals("e")) {
                count2++;
                str[count1 - 1] = count2;
            }


            //后期插入的位置
            if (test_result.get(0).equals("C")) {
                //System.out.print("-------------");
                String regex2 = "[1-9][0-9]*";
                Pattern p = Pattern.compile(regex2);
                Matcher m = p.matcher(line);
                //System.out.println(line);
                while (m.find()) {
                    //System.out.println(m.group());
                    I = Integer.parseInt(m.group());//把噪声支持度转变为double类型
                }
                //System.out.print(m.find());
            } else if (test_result.get(0).equals("&")) {
                I = count2;
                String regex3 = "(\\d+(\\.\\d+)?)";//整数+小数
                Pattern p = Pattern.compile(regex3);
                Matcher m = p.matcher(line);
                //System.out.println(line);
                //System.out.println(m.find());
                while (m.find()) {
                    //System.out.println(m.group());
                    nsup = Double.parseDouble(m.group());
                    //System.out.println(nsup);
                }
            }
            hm1.put(ID1, I);
            hm2.put(ID1, nsup);
            //实验时改这里
            hm3.put(ID1, sup);
            //hm3new.put(ID1,sup);
            hm3new3.put(ID1,sup);
        }


        int i = 0;
        int count3 = 0;
        ArrayList<Integer> lef = new ArrayList();
        //System.out.println("总图的个数：" + count1);
        for (i = 0; i < count1; i++)
            //System.out.println("第" + i + "个图的边数为：" + str[i]);
        for (int p = 0; p < count1 - 1; p++) {
            if (str[p] > str[p + 1] || str[p] == str[p + 1]) {
                count3++;
                lef.add(p);//lef[n] = p;
                //System.out.println("叶子节点的id为：" + p);
                //n++;
            }
        }
        lef.add(count1 - 1);//lef[n] = count1 - 1;
        //System.out.println("叶子节点的id为：" + lef.get(lef.size() - 1));//lef[n]
        //System.out.println("叶子节点的总数为：" + (count3 + 1));
        //深度
        int deep[] = new int[count3 + 1];
        for (int r = 0; r < (count3 + 1); r++) {
            deep[r] = str[lef.get(r)]; //deep[r] = str[lef[r]];
            //System.out.println("第" + r + "分支的深度为：" + deep[r]);
        }

        //剪枝
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i1 = 0; i1 < deep.length; i1++) {
            if (map.get(deep[i1]) != null) {
                map.put(deep[i1], map.get(deep[i1]) + 1);
            } else {
                map.put(deep[i1], 1);
            }
        }

        //得到map中所有的键
        Set<Integer> keyset = map.keySet();
        //创建set集合的迭代器
        Iterator<Integer> it = keyset.iterator();
        //加噪
        //double noised[]=new double[count3+1];
        double value1 = 0.000;
        int l = 0;
        while (it.hasNext()) {
            Integer key = it.next();
            Integer value = map.get(key);
            //System.out.println("深度为" + key + "的共有" + value + "个分支  ");
            //创建双向链表


            //加噪
            //double noise = laplace(0.06, 1);//敏感度1，隐私预算0.1//1,1
            //value1 += ((double) value + noise) / (double) (count3 + 1);
            value1 += ((double) value ) / (double) (count3 + 1);
            //System.out.println(value1);//value1
            if (value1 >= 0.85) {//0.85
                l = key;
                //System.out.println("-------------------------");
                //System.out.println(l);
                break;
            } else {
                l = key;
            }
        }


        int[] del = new int[count1];//存储被删除的图的id//26
        //List<Integer> dellist= new ArrayList<Integer>();
        FileWriter out = new FileWriter("D:\\data\\yjs\\work\\githubcode\\邢江娜-一种基于模式增长的差分隐私频繁子图挖掘算法\\代码\\Algorithm1\\test_result1");
        int i1 = 0;
        for (int r1 = 0; r1 < (count3 + 1); r1++) {
            if (deep[r1] > l) {
                //System.out.println("-------------------------");
                int temp = deep[r1] - l;
                for (int i2 = lef.get(r1); i2 >= lef.get(r1) - temp + 1; i2--) {
                    del[i1] = i2;
                    //dellist.get(i1)=i2;
                    i++;
                    //System.out.println("删除的节点id为：" + del[i1]);
                    try {
                        out.write(del[i1] + " ");
                    } catch (IOException e) {
                    }
                    count1--;
                }
                deep[r1] = l;
                lef.set(r1, lef.get(r1) - temp);
                if (r1 != 0) {
                    if (lef.get(r1 - 1) <= lef.get(r1) && lef.get(r1) <= (lef.get(r1 - 1) + temp)) {
                        deep[r1] = 0;
                        lef.set(r1, -1);
                    }
                }
            }
        }
        out.close();

        //文档1中的
        //String s="";
        int SZ = 0;
        FileReader fr1 = new FileReader("D:\\data\\yjs\\work\\githubcode\\邢江娜-一种基于模式增长的差分隐私频繁子图挖掘算法\\代码\\Algorithm1\\test_result1");
        BufferedReader bfr1 = new BufferedReader(fr1);
        ArrayList<Integer> test_result1 = new ArrayList<>();
        if (test_result1 != null) {
            while ((line = bfr1.readLine()) != null) {
                //System.out.println(line);
                String[] c = line.split(" ");
                //System.out.println(c);
                for (int sz = 0; sz < c.length; sz++) {
                    SZ = Integer.parseInt(c[sz]);
                    hm3.remove(SZ);
                }
            }
        }

        //将叶子节点为-1的删掉
        for (int q1 = 0; q1 < lef.size(); q1++) {
            if (lef.get(q1) == -1) {
                lef.remove(q1);
                q1--;
            }
        }



        List<Integer> dell= new ArrayList<Integer>();
        Set<Integer> settfz = hm3.keySet();
        for (Integer key2fz : settfz) {
            Double value2fz=hm3.get(key2fz);
            hm3.put(key2fz, add(value2fz,laplace(0.1, hm3.size())).doubleValue());
        }
        //System.out.println(hm3.size());
                    Set<Integer> sett = hm3.keySet();
                    for (Integer key2 : sett) {
                        Double value = hm3.get(key2);
                        if(value<minSup){
                           dell.add(key2);
                }
            }
       for(int ttt=0;ttt<dell.size();ttt++){
           hm3.remove(dell.get(ttt));
       }
        //System.out.println(hm3.size());
        //选前K个
        //排序从大到小，按支持度由大到小排序
        //这里将map.entrySet()转换成list
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(hm3.entrySet());
        //List<Map.Entry<Integer, Double>> listnew = new ArrayList<Map.Entry<Integer, Double>>(hm3new.entrySet());
        //然后通过比较器来实现排序
       /* Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            //升序排序
            public int compare(Entry<Integer, Double> o1,
                               Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });*/
////////////////////////////////////////////////////////////////
        //然后通过比较器来实现排序
       /* Collections.sort(listnew, new Comparator<Map.Entry<Integer, Double>>() {
            //升序排序
            public int compare(Entry<Integer, Double> o1,
                               Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });*/

        //int i4 = 0;

        //int k = 20;
        //List<Integer> listF_score2 = new ArrayList<Integer>();//hm3new的key
        FileWriter out1 = new FileWriter("D:\\data\\yjs\\work\\githubcode\\邢江娜-一种基于模式增长的差分隐私频繁子图挖掘算法\\代码\\Algorithm1\\test_result2");
        //选出前k=15个
        for (Map.Entry<Integer, Double> mapping : list) {//在hm3中选出前k个
            //i4++;
            //System.out.println("经历第一次加躁，截断，指数机制筛选后的hm3: "+mapping.getKey() + ":" + mapping.getValue());


          /*  if (i4 > k) {
                try {
                    out1.write((mapping.getKey() + " "));
                    hm3.remove(mapping.getKey());//删除键值对中的键为i4的键值对
                } catch (IOException e) {
                }
            }*/
        }
        //选出前k=15个
        out1.close();

        //System.out.println("hm3: "+hm3);
        //System.out.println("hm3new: "+hm3new);
        //System.out.println(hm3.get(0));
        int tt = lef.size();
        for (int t = 0; t <= lef.size() - 2; t++) {//统计出有效的分支数,
            if (lef.get(t) == -1 || lef.get(t) == lef.get(t + 1) || lef.get(lef.size() - 1) == -1) {
                tt -= 1;
                lef.remove(t);
            }
            //System.out.print(lef.get(t) + " ");
        }
        //System.out.println();
        //System.out.println(lef.size());
        ArrayList[] lists = new ArrayList[tt];//count3 + 1
        lists[0] = new ArrayList<HashMap>();
        for (int i10 = 1; i10 <= tt - 1; i10++) {
            lists[i10] = new ArrayList<HashMap>();
        }

        int i7 = 1;
        //boolean b=false;
        Set<Integer> set = hm3.keySet();
        Iterator<Map.Entry<Integer, Double>> it1 = hm3.entrySet().iterator();//键值对全部都输出
        for (Integer key : set) {
            if (it1.hasNext()) {
                if (key < (lef.get(0) + 1)) {
                    lists[0].add(key);
                } else if (lef.get(0) < key) {
                    if (lef.get(i7 - 1) < key && key < (lef.get(i7) + 1)) {
                        lists[i7].add(key);
                        key = -1;
                    } else {
                        do {
                            i7++;
                            if (key <= lef.get(i7)) {
                                lists[i7].add(key);
                                key = -1;
                            }
                        } while (key != -1);//为真则继续循环
                    }
                }
            }
        }
        for (int i12 = 0; i12 < tt; i12++) {//遍历tt个分支
           // System.out.println(lists[i12]);
           // System.out.println(lists[i12].size());
        }
        //System.out.println(" ------------------------------");


        //对lists[].size从小到大排序
        LinkedHashMap<ArrayList, Integer> hm4 = new LinkedHashMap<>();//创建对象，lists.size(),list
        for (int i8 = 0; i8 < tt; i8++) {
            hm4.put(lists[i8], lists[i8].size());
            if (hm4.get(lists[i8]) == 0) {
                hm4.remove(lists[i8]);
            }
        }
       // System.out.print("hm4:"+hm4 + " ");
        //System.out.println();

        //排序从小到da，按键由小到大排序
        //这里将map.entrySet()转换成list
        List<Map.Entry<ArrayList, Integer>> list123 = new ArrayList<Map.Entry<ArrayList, Integer>>(hm4.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list123, new Comparator<Map.Entry<ArrayList, Integer>>() {
            //升序排序
            public int compare(Entry<ArrayList, Integer> o1,
                               Entry<ArrayList, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        //System.out.print("list123:"+list123 + " ");
        //System.out.println();
        //转换成新map输出
        LinkedHashMap<ArrayList, Integer> hm5 = new LinkedHashMap<ArrayList, Integer>();
        List<Integer> fz=new ArrayList();
        for (Map.Entry<ArrayList, Integer> newMap : list123) {
            hm5.put(newMap.getKey(), newMap.getValue());

            fz.add(newMap.getValue());//把值存入fz列表中
        }
        //System.out.println("hm5:"+hm5);
//权重
        //System.out.println("---------------------------------------------");
        List<BigDecimal> w=new ArrayList();
        //int m=hm5.size();//从1开始算,剩下多少列
        // 记录数据个数
        Map<Integer,Integer> www = new HashMap<>();
        for(Integer str1:fz){
            //计数器，用来记录数据的个数
            Integer ifz = 1;
            if(www.get(str1) != null){
                ifz=www.get(str1)+1;
            }
            www.put(str1,ifz);
        }
        //System.out.println("list中各数据的存在个数："+www);
        //去除重复的元素
        Set setys = new  HashSet();
        List<Integer> newList = new  ArrayList();
        for (Integer cd:fz) {
            if (setys.add(cd)) {//否则计数
                //如果不重复则添加到newList中(setys.add(cd))
                newList.add(cd);
            }
        }
        //System.out.println( "去重后的个数： " + newList.size());
        //System.out.print( "深度分别为： " + newList);

        int m=newList.size();
        for(int fs=1;fs<=newList.size();fs++){
            //int wz=hm5.get(fs);
            w.add(div(1.0,(m-(fs-1)),3));//权重(m-(fs-1))
        }
        //System.out.println("每列分配的权重："+w);
//存储深度和对应得权重
        LinkedHashMap<Integer, BigDecimal> ww = new LinkedHashMap<Integer, BigDecimal>();
        for(int newi=0;newi<newList.size();newi++){
            ww.put(newList.get(newi),w.get(newi));
        }
        //W
        BigDecimal W =new BigDecimal("0");
        for(int w1=0;w1<newList.size();w1++){
            W=W.add(w.get(w1));//bignum3 =  bignum1.add(bignum2);
            //System.out.println(W);
        }
        //System.out.println("总权重："+W);//总权重

        LinkedList<Double> lREList = new LinkedList<Double>();
        double dRelativeError=0.0;
        int z1=0;
        double dRE=0.0;
        int num3=0;
        int num1=0;
           Set<ArrayList> set12 = hm5.keySet();
            for (ArrayList key12 : set12) {
                Integer value12 = hm5.get(key12);
                //for (int i14 = 1; i14 <= l; i14++) {
                Set<Integer> set123 = ww.keySet();
                for (Integer key123 : set123) {

                    Set<Integer> set1234 =www.keySet();//新加的
                    for (Integer key1234: set1234) {

                    if (value12.equals(key123)&&value12.equals(key1234)) {
                        //System.out.println("id列表为：" + key12);
                        for (int i13 = 0; i13 < key12.size(); i13++) {//个数为key12.size()
                            double dStandSupport = 0.0;
                            dStandSupport = hm3new3.get(key12.get(i13));
                            //System.out.print("对应的真实支持度为：" + dStandSupport + " ");//输出真实支持度
                            num1++;
                            if(dStandSupport>=minSup){
                                num3++;}
                           // System.out.print("minSup:"+minSup);
                            double dResultSupport = 0.0;
                            dResultSupport = add(hm3new3.get(key12.get(i13)), laplace(div(div((ww.get(key123)).doubleValue(), www.get(key1234).doubleValue(), 3).doubleValue(), (W).doubleValue(), 3).doubleValue() * 0.098, key12.size())).doubleValue();
                            //System.out.print("对应的噪声支持度为：" + dResultSupport + " ");
                            //System.out.println("值为：" + www.get(key1234));
                            //.setScale(3)
                            dRelativeError = Math.abs(dResultSupport - dStandSupport);
                            dRelativeError /= Math.abs(dStandSupport);
                            lREList.add(dRelativeError);
                            //System.out.println();
                        }//div(1.0,(m-(fs-1)),3)
                        //z1++;
                        //System.out.println();
                        ////ystem.out.println(" ======"+hm4.get(0));
                    }
                }
                    z1++;
            }
               // }
               // z1++;
            }




        Collections.sort( lREList );

        //dRE = lREList.get( (int)(lREList.size()/2) );
        BigDecimal b = new BigDecimal(dRE);
        //double f1 = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        //System.out.println("RE:"+f1);
        //System.out.println("RE:"+lREList);
        //System.out.println("F-score:"+fscore3);
        //System.out.println("num1:"+num1);
        //System.out.println("num2:"+20);//真实挖掘出来的频繁子图的个数
        //System.out.println("num3:"+num3);
        int num2=20;
        //计算F-score
        BigDecimal fscore1;
        BigDecimal fscore2;
        BigDecimal fscore3;
        fscore1=div(2*num3*num3,num1*num2,3);
        fscore2=div((num2*num3)+(num3*num1),num1*num2,3);
        fscore3=div( fscore1.doubleValue(), fscore2.doubleValue(),3);
        //对筛选后的删除
        System.out.println("F-SCORE:"+fscore3);
        fr.close();
    }
//求除法
    public static BigDecimal div(double d1, double d2,int c) {// 进行除法运算
    BigDecimal b1 = new BigDecimal(d1);
    BigDecimal b2 = new BigDecimal(d2);
    return b1.divide(b2,c,BigDecimal.ROUND_HALF_UP);
}
//求和
    public BigDecimal add(double v1, double v2) {
        BigDecimal b1=new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    public static List  getKey( HashMap hm1, int value){//获取id的列表
        List<Integer> keyList = new ArrayList<>();
        Set<Integer> keys=hm1.keySet();
        for(Integer key:keys ){
            if(hm1.get(key).equals(value)){
                keyList.add(key);
            }
        }
        return keyList;
    }
    public static double laplace(double pro, double k) {//pro隐私预算，k敏感度
        pro = k / pro;
        double _para = 0.5;
        Random rd = new Random();
        double a = rd.nextDouble();
        double result = 0;
        double temp = 0;
        if (a < _para) {
            temp = pro * Math.log(2 * a);
            result = temp;
        } else if (a > _para) {
            temp = -pro * Math.log(2 - 2 * a);
            result = temp;
        } else
            result = 0;

        return result;
    }
    private void runIntern() throws IOException {

        // In case 1 node sub-graphs should also be mined for, do this as pre-processing step.
        if (maxPat_min <= 1) {
            for (int id = 0; id < TRANS.size(); ++id) {
                for (int nid = 0; nid < TRANS.get(id).size(); ++nid) {
                    int key = TRANS.get(id).get(nid).label;
                    singleVertex.computeIfAbsent(id, k -> new TreeMap<>());
                    if (singleVertex.get(id).get(key) == null) {
                        // number of graphs it appears in
                        singleVertexLabel.put(key, Common.getValue(singleVertexLabel.get(key)) + 1);
                    }

                    singleVertex.get(id).put(key, Common.getValue(singleVertex.get(id).get(key)) + 1);
                }
            }
        }
        ArrayList<Edge> edges = new ArrayList<>();
        NavigableMap<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> root = new TreeMap<>();
        for (int id = 0; id < TRANS.size(); ++id) {
            Graph g = TRANS.get(id);
            for (int from = 0; from < g.size(); ++from) {
                if (Misc.getForwardRoot(g, g.get(from), edges)) {
                    for (Edge it : edges) {
                        int key_1 = g.get(from).label;
                        NavigableMap<Integer, NavigableMap<Integer, Projected>> root_1 = root.computeIfAbsent(key_1, k -> new TreeMap<>());
                        int key_2 = it.eLabel;
                        NavigableMap<Integer, Projected> root_2 = root_1.computeIfAbsent(key_2, k -> new TreeMap<>());
                        int key_3 = g.get(it.to).label;
                        Projected root_3 = root_2.get(key_3);
                        if (root_3 == null) {
                            root_3 = new Projected();
                            root_2.put(key_3, root_3);
                        }
                        root_3.push(id, it, null);
                    }
                }
            }
        }
        for (Entry<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> fromLabel : root.entrySet()) {
            for (Entry<Integer, NavigableMap<Integer, Projected>> eLabel : fromLabel.getValue().entrySet()) {
                for (Entry<Integer, Projected> toLabel : eLabel.getValue().entrySet()) {
                    // Build the initial two-node graph. It will be grown recursively within project.
                    DFS_CODE.push(0, 1, fromLabel.getKey(), eLabel.getKey(), toLabel.getKey());
                    /*int sup = support(toLabel.getValue());
                    if (!isMin()) {
                        return;
                    }
                    if (sup < minSup) {
                        count1++;
                        return;
                    }*/
                    project(toLabel.getValue());
                    DFS_CODE.pop();//IndexOutOfBoundsException
                }
            }
        }
    }
    private void report(double sup,double nsup) throws IOException {
        // Filter to small/too large graphs.
        if (maxPat_max > maxPat_min && DFS_CODE.countNode() > maxPat_max)
            return ;
        if (maxPat_min > 0 && DFS_CODE.countNode() < maxPat_min)
            return;
        Graph g = new Graph(directed);
        DFS_CODE.toGraph(g);
        os.write("t #" + ID + "*" + sup + System.getProperty("line.separator"));

        g.write(os);
        os.write( "& "+nsup+ System.getProperty("line.separator"));
        ++ID;
    }
    private void project(Projected projected) throws IOException {

        // Check if the pattern is frequent enough.
        //double nsup = support(projected);
        double sup = support(projected)[1];
        double nsup = support(projected)[0];
        //if (sup > minSup&&isMin()) {num2++;}
        if (nsup < minSup) {
            return;
        }
        if (!isMin()) {
            return;
        }
        // Output the frequent substructure
        //double sup = support(projected)[1];//真实支持度
        report(sup,nsup);
        if (maxPat_max > maxPat_min && DFS_CODE.countNode() > maxPat_max)
            return;
        ArrayList<Integer> rmPath = DFS_CODE.buildRMPath();
        int minLabel = DFS_CODE.get(0).fromLabel;
        int maxToc = DFS_CODE.get(rmPath.get(0)).to;

        NavigableMap<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> new_fwd_root = new TreeMap<>();
        NavigableMap<Integer, NavigableMap<Integer, Projected>> new_bck_root = new TreeMap<>();
        ArrayList<Edge> edges = new ArrayList<>();
        for (PDFS aProjected : projected) {
            int id = aProjected.id;
            History history = new History(TRANS.get(id), aProjected);
            // backward
            for (int i = rmPath.size() - 1; i >= 1; --i) {
                Edge e = Misc.getBackward(TRANS.get(id), history.get(rmPath.get(i)), history.get(rmPath.get(0)),
                        history);
                if (e != null) {
                    int key_1 = DFS_CODE.get(rmPath.get(i)).from;
                    NavigableMap<Integer, Projected> root_1 = new_bck_root.computeIfAbsent(key_1, k -> new TreeMap<>());
                    int key_2 = e.eLabel;
                    Projected root_2 = root_1.get(key_2);
                    if (root_2 == null) {
                        root_2 = new Projected();
                        root_1.put(key_2, root_2);
                    }
                    root_2.push(id, e, aProjected);
                }
            }
            if (Misc.getForwardPure(TRANS.get(id), history.get(rmPath.get(0)), minLabel, history, edges)) {
                for (Edge it : edges) {
                    NavigableMap<Integer, NavigableMap<Integer, Projected>> root_1 = new_fwd_root.computeIfAbsent(maxToc, k -> new TreeMap<>());
                    int key_2 = it.eLabel;
                    NavigableMap<Integer, Projected> root_2 = root_1.computeIfAbsent(key_2, k -> new TreeMap<>());
                    int key_3 = TRANS.get(id).get(it.to).label;
                    Projected root_3 = root_2.get(key_3);
                    if (root_3 == null) {
                        root_3 = new Projected();
                        root_2.put(key_3, root_3);
                    }
                    root_3.push(id, it, aProjected);
                }
            }
            // backtracked forward
            for (Integer aRmPath : rmPath)
                //System.out.println(aRmPath);
                if (Misc.getForwardRmPath(TRANS.get(id), history.get(aRmPath), minLabel, history, edges)) {
                    for (Edge it : edges) {
                        int key_1 = DFS_CODE.get(aRmPath).from;
                        NavigableMap<Integer, NavigableMap<Integer, Projected>> root_1 = new_fwd_root.computeIfAbsent(key_1, k -> new TreeMap<>());
                        int key_2 = it.eLabel;
                        NavigableMap<Integer, Projected> root_2 = root_1.computeIfAbsent(key_2, k -> new TreeMap<>());
                        int key_3 = TRANS.get(id).get(it.to).label;
                        Projected root_3 = root_2.get(key_3);
                        if (root_3 == null) {
                            root_3 = new Projected();
                            root_2.put(key_3, root_3);
                        }
                        root_3.push(id, it, aProjected);
                    }

                }
        }

        // backward内边
        for (Entry<Integer, NavigableMap<Integer, Projected>> to : new_bck_root.entrySet()) {
            for (Entry<Integer, Projected> eLabel : to.getValue().entrySet()) {
                DFS_CODE.push(maxToc, to.getKey(), -1, eLabel.getKey(), -1);
                project(eLabel.getValue());
                DFS_CODE.pop();
            }
        }
        // forward
        for (Entry<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> from : new_fwd_root.descendingMap()
                .entrySet()) {
            for (Entry<Integer, NavigableMap<Integer, Projected>> eLabel : from.getValue().entrySet()) {
                for (Entry<Integer, Projected> toLabel : eLabel.getValue().entrySet()) {
                    DFS_CODE.push(from.getKey(), maxToc + 1, -1, eLabel.getKey(), toLabel.getKey());
                    project(toLabel.getValue());
                    DFS_CODE.pop();
                }
            }
        }
    }
//double jh[]=new double [2];//存放nsup和sup
double jh[]=new double [2];//存放nsup和sup
    private double[] support(Projected projected) {
        int oid = 0xffffffff;
        double size = 0;

        for (PDFS cur : projected) {
            if (oid != cur.id) {
                ++size;
            }
            oid = cur.id;
        }
        double noise=laplace(0.002,1);
        double nsize=size+noise;

        jh[0]=nsize;
        jh[1]=size;
        return  jh;
    }

    private boolean isMin() {
        if (DFS_CODE.size() == 1)
            return (true);

        DFS_CODE.toGraph(GRAPH_IS_MIN);
        DFS_CODE_IS_MIN.clear();

        NavigableMap<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> root = new TreeMap<>();
        ArrayList<Edge> edges = new ArrayList<>();

        for (int from = 0; from < GRAPH_IS_MIN.size(); ++from)
            if (Misc.getForwardRoot(GRAPH_IS_MIN, GRAPH_IS_MIN.get(from), edges))
                for (Edge it : edges) {
                    int key_1 = GRAPH_IS_MIN.get(from).label;
                    NavigableMap<Integer, NavigableMap<Integer, Projected>> root_1 = root.computeIfAbsent(key_1, k -> new TreeMap<>());
                    int key_2 = it.eLabel;
                    NavigableMap<Integer, Projected> root_2 = root_1.computeIfAbsent(key_2, k -> new TreeMap<>());
                    int key_3 = GRAPH_IS_MIN.get(it.to).label;
                    Projected root_3 = root_2.get(key_3);
                    if (root_3 == null) {
                        root_3 = new Projected();
                        root_2.put(key_3, root_3);
                    }
                    root_3.push(0, it, null);
                }

        Entry<Integer, NavigableMap<Integer, NavigableMap<Integer, Projected>>> fromLabel = root.firstEntry();
        Entry<Integer, NavigableMap<Integer, Projected>> eLabel = fromLabel.getValue().firstEntry();
        Entry<Integer, Projected> toLabel = eLabel.getValue().firstEntry();

        DFS_CODE_IS_MIN.push(0, 1, fromLabel.getKey(), eLabel.getKey(), toLabel.getKey());

        return isMinProject(toLabel.getValue());
    }

    private boolean isMinProject(Projected projected) {
        ArrayList<Integer> rmPath = DFS_CODE_IS_MIN.buildRMPath();
        int minLabel = DFS_CODE_IS_MIN.get(0).fromLabel;
        int maxToc = DFS_CODE_IS_MIN.get(rmPath.get(0)).to;

        {
            NavigableMap<Integer, Projected> root = new TreeMap<>();
            boolean flg = false;
            int newTo = 0;

            for (int i = rmPath.size() - 1; !flg && i >= 1; --i) {
                for (PDFS cur : projected) {
                    History history = new History(GRAPH_IS_MIN, cur);
                    Edge e = Misc.getBackward(GRAPH_IS_MIN, history.get(rmPath.get(i)), history.get(rmPath.get(0)),
                            history);
                    if (e != null) {
                        int key_1 = e.eLabel;
                        Projected root_1 = root.get(key_1);
                        if (root_1 == null) {
                            root_1 = new Projected();
                            root.put(key_1, root_1);
                        }
                        root_1.push(0, e, cur);
                        newTo = DFS_CODE_IS_MIN.get(rmPath.get(i)).from;
                        flg = true;
                    }
                }
            }

            if (flg) {
                Entry<Integer, Projected> eLabel = root.firstEntry();
                DFS_CODE_IS_MIN.push(maxToc, newTo, -1, eLabel.getKey(), -1);
                if (DFS_CODE.get(DFS_CODE_IS_MIN.size() - 1)
                        .notEqual(DFS_CODE_IS_MIN.get(DFS_CODE_IS_MIN.size() - 1)))
                    return false;
                return isMinProject(eLabel.getValue());
            }
        }
        {
            boolean flg = false;
            int newFrom = 0;
            NavigableMap<Integer, NavigableMap<Integer, Projected>> root = new TreeMap<>();
            ArrayList<Edge> edges = new ArrayList<>();
            for (PDFS cur : projected) {
                History history = new History(GRAPH_IS_MIN, cur);
                if (Misc.getForwardPure(GRAPH_IS_MIN, history.get(rmPath.get(0)), minLabel, history, edges)) {
                    flg = true;
                    newFrom = maxToc;
                    for (Edge it : edges) {
                        int key_1 = it.eLabel;
                        NavigableMap<Integer, Projected> root_1 = root.computeIfAbsent(key_1, k -> new TreeMap<>());
                        int key_2 = GRAPH_IS_MIN.get(it.to).label;
                        Projected root_2 = root_1.get(key_2);
                        if (root_2 == null) {
                            root_2 = new Projected();
                            root_1.put(key_2, root_2);
                        }
                        root_2.push(0, it, cur);
                    }
                }
            }
            for (int i = 0; !flg && i < rmPath.size(); ++i) {
                for (PDFS cur : projected) {
                    History history = new History(GRAPH_IS_MIN, cur);
                    if (Misc.getForwardRmPath(GRAPH_IS_MIN, history.get(rmPath.get(i)), minLabel, history, edges)) {
                        flg = true;
                        newFrom = DFS_CODE_IS_MIN.get(rmPath.get(i)).from;
                        for (Edge it : edges) {
                            int key_1 = it.eLabel;
                            NavigableMap<Integer, Projected> root_1 = root.computeIfAbsent(key_1, k -> new TreeMap<>());
                            int key_2 = GRAPH_IS_MIN.get(it.to).label;
                            Projected root_2 = root_1.get(key_2);
                            if (root_2 == null) {
                                root_2 = new Projected();
                                root_1.put(key_2, root_2);
                            }
                            root_2.push(0, it, cur);
                        }
                    }
                }
            }
            if (flg) {
                Entry<Integer, NavigableMap<Integer, Projected>> eLabel = root.firstEntry();
                Entry<Integer, Projected> toLabel = eLabel.getValue().firstEntry();
                DFS_CODE_IS_MIN.push(newFrom, maxToc + 1, -1, eLabel.getKey(), toLabel.getKey());
                if (DFS_CODE.get(DFS_CODE_IS_MIN.size() - 1)
                        .notEqual(DFS_CODE_IS_MIN.get(DFS_CODE_IS_MIN.size() - 1)))
                    return false;
                return isMinProject(toLabel.getValue());
            }
        }
        return true;
    }
}
