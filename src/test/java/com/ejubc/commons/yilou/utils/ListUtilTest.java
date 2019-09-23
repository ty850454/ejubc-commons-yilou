package com.ejubc.commons.yilou.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilTest {




    @Test
    void similar() {


        List<TestClassA> newC = Arrays.asList(new TestClassA(1, "哈哈"), new TestClassA(2, "哈哈"));
        List<TestClassB> oldC = Arrays.asList(new TestClassB(1, "嘿嘿"), new TestClassB(4, "喵喵"));

        ListUtil.SimilarResult<TestClassA, TestClassB> similar = ListUtil.similar(newC, oldC, (newElement, oldElement) -> newElement.id.equals(oldElement.id));
        System.out.println(similar);

    }



    @Getter
    @AllArgsConstructor
    @ToString
    private class TestClassA {
        private Integer id;
        private String nice;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    private class TestClassB {
        private Integer id;
        private String name;
    }


}