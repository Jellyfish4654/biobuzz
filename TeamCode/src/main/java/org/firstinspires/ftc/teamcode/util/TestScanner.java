package org.firstinspires.ftc.teamcode.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.pedropathing.tuning.autotune.TunerScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.frozenmilk.sinister.Scanner;
import dev.frozenmilk.sinister.targeting.SearchTarget;
import dev.frozenmilk.sinister.targeting.TeamCodeSearch;
import dev.frozenmilk.util.graph.Graph;
import dev.frozenmilk.util.graph.rule.AdjacencyRule;

/**Gets all annotated classes, makes an instance of them, and files in sets */
public class TestScanner implements Scanner {
    public static Map<ClassLoader, List<OnBotTest>> onBotTests = new HashMap<>();
    public static final TestScanner INSTANCE = new TestScanner();
    static final SearchTarget targets = new TeamCodeSearch();

    @Override
    public AdjacencyRule<Scanner, Graph<Scanner>> getLoadAdjacencyRule() {
        return Scanner.INDEPENDENT();
    }

    @Override
    public AdjacencyRule<Scanner, Graph<Scanner>> getUnloadAdjacencyRule() {
        return Scanner.INDEPENDENT();
    }
    @Override
    public SearchTarget getTargets(){
        return targets;
    }
    @Override
    public void beforeScan(ClassLoader loader){
        onBotTests.putIfAbsent(loader,new ArrayList<OnBotTest>());
    }

    @Override
    public void scan(ClassLoader loader, Class<?> cls){
        if(cls.isAnnotationPresent(OnBotTestInfo.class) && cls.getSuperclass() == OnBotTest.class && onBotTests.containsKey(loader)){
            try {
                Log.i("OnBotTest",cls.getName());
                onBotTests.get(loader).add(cls.asSubclass(OnBotTest.class).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterScan(ClassLoader loader){}


    @Override
    public void unload(ClassLoader loader, Class<?> cls){}

    @Override
    public void afterUnload(ClassLoader loader){
        onBotTests.remove(loader);
    }


}