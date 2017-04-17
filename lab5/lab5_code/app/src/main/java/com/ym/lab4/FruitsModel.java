package com.ym.lab4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ym on 16-10-25.
 */

public class FruitsModel {
    static private FruitsModel instance;
    static public FruitsModel getInstance() {
        if (instance == null) {
            instance = new FruitsModel();
        }
        return instance;
    }

    public List<Fruit> data = new ArrayList<>();

    private FruitsModel() {
        data.add(new Fruit("apple", R.mipmap.apple));
        data.add(new Fruit("banana", R.mipmap.banana));
        data.add(new Fruit("cherry", R.mipmap.cherry));
        data.add(new Fruit("coco", R.mipmap.coco));
        data.add(new Fruit("kiwi", R.mipmap.kiwi));
        data.add(new Fruit("orange", R.mipmap.orange));
        data.add(new Fruit("pear", R.mipmap.pear));
        data.add(new Fruit("strawberry", R.mipmap.strawberry));
        data.add(new Fruit("watermelon", R.mipmap.watermelon));
    }

    public class Fruit {
        public String name;
        public int res;
        public Fruit(String name, int res) {
            this.name = name;
            this.res = res;
        }
    }
}
